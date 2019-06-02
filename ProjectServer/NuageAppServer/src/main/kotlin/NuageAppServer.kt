import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.AndroidNotification
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message
import org.apache.activemq.ActiveMQConnectionFactory
import java.util.*
import javax.jms.*
import javax.naming.Context
import javax.naming.InitialContext

//TODO put all this in a config file in properties
const val JMS_IP = "124.252.253.50"
const val JMS_USER = "jmsclient@csp"
const val JMS_PASSWORD = "jmsclient"
//const val JMS_IP = "124.252.253.138"
//const val JMS_USER = "jms@csp"
//const val JMS_PASSWORD = "jms"
const val JMS_PORT = 61616

class NuageAppServer(
    private val clientid: String,
    private val jms_ip: String,
    private val jms_port: Int,
    private val jms_user: String,
    private val jms_password: String
) : ExceptionListener {
    private var options: FirebaseOptions = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .setDatabaseUrl("https://nuagealarms.firebaseio.com/")
        .build()
    private var url: String = "tcp://$jms_ip:$jms_port"
    private var ctx: InitialContext

    init {
        ctx = getJndiConfig(jms_ip)
    }

    private fun getJndiConfig(url: String): InitialContext {
        val env = Properties()
        env[Context.INITIAL_CONTEXT_FACTORY] = "org.apache.activemq.jndi.ActiveMQInitialContextFactory"
        env[InitialContext.URL_PKG_PREFIXES] = "org.jboss.naming:org.jnp.interfaces"
        val lProviderUrlFormat = "tcp://%s:%d?wireFormat.cacheEnabled=false&wireFormat.tightEncodingEnabled=false"
        env[Context.PROVIDER_URL] = String.format(lProviderUrlFormat, jms_ip, jms_port)

        return InitialContext(env)
    }

    fun redirectMessages(){

        val nuageApp = FirebaseApp.initializeApp(options)
        val fm = FirebaseMessaging.getInstance(nuageApp)
        val fmsg = Message.builder()
        FirebaseMessaging.getInstance(nuageApp).send(Message.builder()
            .setAndroidConfig(AndroidConfig.builder()
                .setNotification(
                    AndroidNotification.builder().setTitle("Server Notification!").setBody("New notification from middleman server!").build()
                ).build())
            .setTopic("Alarms")
            .build())
        //println(FirebaseApp.getApps())
    }

    //messageSelector can be: message_selector = "entityType='vport' AND eventType='CREATE'"
    fun receiveMessages(topicName: String, messageSelector: String){
        //val connectionFactory = ActiveMQConnectionFactory(jms_user, jms_password, url)
        val connectionFactory = ctx.lookup("ConnectionFactory") as TopicConnectionFactory
        val connection = connectionFactory.createConnection(jms_user, jms_password)
        //connection.clientID = clientid

        val session = (connection as TopicConnection).createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE)
        connection.start()
        connection.exceptionListener = this

        val topic = ctx.lookup(topicName) as Topic
        //val topic = session.createTopic(topicName)
        //val subscriber = session.createDurableSubscriber(topicaux, clientid, messageSelector, true)
        val subscriber = session.createSubscriber(topic)
        subscriber.messageListener = MessageListener {
            println(it)
        }

    }

    override fun onException(exception: JMSException){
        println(exception)
    }
}

fun main(args: Array<String>){
    //if(args.size != 1) { println("Server IP needed."); return }
    //NuageAppServer().redirectMessages()
    //TODO create only once
    val clientid = UUID.randomUUID().toString()
    val topic = "jms/topic/CNAAlarms"
    val messageSelector = ""
    val nas = NuageAppServer(clientid, JMS_IP, JMS_PORT, JMS_USER, JMS_PASSWORD)
    nas.receiveMessages(topic, messageSelector)

}