import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.messaging.AndroidConfig
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.Message

class FirebaseHelper(val options: FirebaseOptions, val app: FirebaseApp = FirebaseApp.initializeApp(options)) {

    fun sendPushNotifications(topic: String, map: Map<String, String?>) {
        val fm = FirebaseMessaging.getInstance(app)
        val fmsg = Message.builder()
        FirebaseMessaging.getInstance(app).send(
            Message.builder().setTopic(topic).setAndroidConfig(
                AndroidConfig.builder()
                    .setTtl(18000 * 3600)
                    /*.setNotification(
                        AndroidNotification.builder()
                            .setTitle(title)
                            .setBody(description)
                            .setClickAction("MAIN_ACTIVITY")
                            .build()
                        )*/.build()
            )
                //.putData("title", title)
                //.putData("description", description)
                .putAllData(map)
                .build()
        )
    }
}