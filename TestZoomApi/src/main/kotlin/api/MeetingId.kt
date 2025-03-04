package main.kotlin.api
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients

fun getMeetingId(accessToken: String): String? {
    val httpClient: CloseableHttpClient = HttpClients.createDefault()
    val request = HttpGet("https://api.zoom.us/v2/users/me/meetings").apply {
        addHeader("Authorization", "Bearer $accessToken")
        addHeader("Content-Type", "application/json")
    }

    httpClient.use { client ->
        val response = client.execute(request)
        val json = response.entity.content.reader().readText()
        println("Zoom Meetings Response: $json")

        return Regex("\"id\":(\\d+)").find(json)?.groups?.get(1)?.value
    }
}