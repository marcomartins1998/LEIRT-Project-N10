import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseOptions
import java.io.FileInputStream
import java.util.*

//const val JMS_IP = "124.252.253.138"
//const val JMS_USER = "jms@csp"
//const val JMS_PASSWORD = "jms"

class NuageAppServer

fun main() {
    NukeSSLCerts.nuke()
    // Get app configurations to use
    val rootPath = NuageAppServer::class.java.getResource("app.properties").path
    val appProps = Properties()
    appProps.load(FileInputStream(rootPath))

    val options: FirebaseOptions = FirebaseOptions.Builder()
        .setCredentials(GoogleCredentials.getApplicationDefault())
        .setDatabaseUrl("https://nuagealarms.firebaseio.com/")
        .build()
    val fh = FirebaseHelper(options)

    val topic = "jms/topic/CNAAlarms"
    val messageSelector = ""
    val jmsl = JMSListener(
        appProps.getProperty("JMS_IP"),
        appProps.getProperty("JMS_PORT").toInt(),
        appProps.getProperty("JMS_USER"),
        appProps.getProperty("JMS_PASSWORD")
    )
    val nhh = NuageHttpHelper(appProps)
    jmsl.receiveMessages(topic, messageSelector) {
        if (it.getStringProperty("eventType") != "DELETE") {
            val map = nhh.getAlarm(it.getStringProperty("ID"))
            println(map)
            if (!map.isNullOrEmpty()) fh.sendPushNotifications("Alarms-${appProps.getProperty("JMS_IP")}", map)
        }
    }

}