package main.kotlin.config

import org.apache.hc.client5.http.classic.methods.HttpPost
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients
import org.apache.hc.core5.http.io.entity.StringEntity
import java.net.URLEncoder
import java.nio.charset.StandardCharsets
import main.kotlin.config.ZoomConfig;

object ZoomAuth {
    fun getAccessToken(clientId: String, clientSecret: String, authorizationCode: String, redirectUri: String): String {
        val httpClient: CloseableHttpClient = HttpClients.createDefault()
        val encodedRedirectUri = URLEncoder.encode(redirectUri, StandardCharsets.UTF_8.toString())

        val request = HttpPost(ZoomConfig.TOKEN_URL).apply {
            addHeader("Content-Type", "application/x-www-form-urlencoded")
            val body = "grant_type=authorization_code" +
                    "&code=$authorizationCode" +
                    "&redirect_uri=$encodedRedirectUri" +
                    "&client_id=$clientId" +
                    "&client_secret=$clientSecret"

            entity = StringEntity(body, StandardCharsets.UTF_8)
        }

        httpClient.use { client ->
            val response = client.execute(request)
            val json = response.entity.content.reader().readText()
            println("Zoom OAuth Response: $json")

            return Regex("\"access_token\":\"(.*?)\"").find(json)?.groups?.get(1)?.value ?: ""
        }
    }
}
