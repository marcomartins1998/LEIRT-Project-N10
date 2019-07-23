import com.fasterxml.jackson.databind.ObjectMapper
import khttp.get
import java.util.*
import kotlin.collections.HashMap


class NuageHttpHelper(
    val appProps: Properties
) {
    lateinit var apikey: String
    var apikeyexpiry: Long = Date().time
    val mapper = ObjectMapper()

    fun getAuthorization(): String? {
        if (Date().time < apikeyexpiry) return apikey
        val authurl =
            "https://${appProps.getProperty("VSD_IP")}:${appProps.getProperty("VSD_PORT")}/nuage/api/${appProps.getProperty(
                "VSD_VERSION"
            )}/me"
        val headers = mapOf(
            "X-Nuage-Organization" to "csp",
            "Authorization" to "Basic " + Base64.getEncoder().encodeToString(
                "${appProps.getProperty("JMS_AUTH_USER")}:${appProps.getProperty(
                    "JMS_PASSWORD"
                )}".toByteArray()
            )
        )
        val r = get(url = authurl, headers = headers)
        if (r.statusCode == 200) {
            val mapresp = mapper.readValue(r.jsonArray.get(0).toString(), HashMap::class.java)
            apikey = mapresp["APIKey"] as String
            apikeyexpiry = mapresp["APIKeyExpiry"] as Long
            return apikey
        }
        return null
    }

    fun getAlarm(alarmId: String): Map<String, String>? {
        val alarmurl =
            "https://${appProps.getProperty("VSD_IP")}:${appProps.getProperty("VSD_PORT")}/nuage/api/${appProps.getProperty(
                "VSD_VERSION"
            )}/alarms/$alarmId"
        val headers = mapOf(
            "X-Nuage-Organization" to "csp",
            "Authorization" to "Basic " + Base64.getEncoder().encodeToString("${appProps.getProperty("JMS_AUTH_USER")}:${getAuthorization()}".toByteArray())
        )

        val r = get(url = alarmurl, headers = headers)
        if (r.statusCode == 200) {
            val mapresp = mapper.readValue(r.jsonArray.get(0).toString(), Map::class.java)
            return mapresp.map { it.key as String to it.value.toString() }.toMap()
        }
        return null
    }
}