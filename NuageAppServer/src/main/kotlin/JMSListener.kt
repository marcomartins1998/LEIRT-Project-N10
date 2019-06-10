import java.util.*
import javax.jms.*
import javax.naming.Context
import javax.naming.InitialContext

class JMSListener(
    private val jms_ip: String,
    private val jms_port: Int,
    private val jms_user: String,
    private val jms_password: String
) : ExceptionListener {
    //private var url: String = "tcp://$jms_ip:$jms_port"
    private var ctx: InitialContext = getJndiConfig()

    private fun getJndiConfig(): InitialContext {
        val env = Properties()
        env[Context.INITIAL_CONTEXT_FACTORY] = "org.apache.activemq.jndi.ActiveMQInitialContextFactory"
        env[InitialContext.URL_PKG_PREFIXES] = "org.jboss.naming:org.jnp.interfaces"
        val lProviderUrlFormat = "tcp://%s:%d?wireFormat.cacheEnabled=false&wireFormat.tightEncodingEnabled=false"
        env[Context.PROVIDER_URL] = String.format(lProviderUrlFormat, jms_ip, jms_port)
        return InitialContext(env)
    }

    //messageSelector can be: message_selector = "entityType='vport' AND eventType='CREATE'"
    fun receiveMessages(topicName: String, messageSelector: String, messageFunc: (Message) -> Unit) {
        val connectionFactory = ctx.lookup("ConnectionFactory") as TopicConnectionFactory
        val connection = connectionFactory.createConnection(jms_user, jms_password)

        val session = (connection as TopicConnection).createTopicSession(false, TopicSession.AUTO_ACKNOWLEDGE)
        connection.start()
        connection.exceptionListener = this

        val topic = ctx.lookup(topicName) as Topic
        val subscriber = (session as TopicSession).createSubscriber(topic, messageSelector, true)
        //val subscriber = session.createSubscriber(topic)  <-- WORKS WITH THIS ONE!!!
        /*subscriber.messageListener = MessageListener {
            println(it)
        }*/
        subscriber.messageListener = MessageListener(messageFunc)
    }

    override fun onException(exception: JMSException) {
        //TODO Maybe log errors to file somewhere for better error management in the future
        println(exception)
    }
}