package main.kotlin.api

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.apache.hc.client5.http.classic.methods.HttpGet
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient
import org.apache.hc.client5.http.impl.classic.HttpClients

object ZoomApiClient {
    private val objectMapper = jacksonObjectMapper()

    fun getUserInfo(accessToken: String): String {
        val httpClient: CloseableHttpClient = HttpClients.createDefault()
        val request = HttpGet("https://api.zoom.us/v2/users/me").apply {
            addHeader("Authorization", "Bearer $accessToken")
        }

        httpClient.use { client ->
            val response = client.execute(request)
            return response.entity.content.reader().readText()
        }
    }
}
