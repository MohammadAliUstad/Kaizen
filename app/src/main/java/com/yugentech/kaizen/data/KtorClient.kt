package com.yugentech.kaizen.data

import com.yugentech.kaizen.data.model.QuoteResponse
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class KtorClient {

    private val client = HttpClient(Android) {
        install(ContentNegotiation) {
            json(
                Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                }
            )
        }
    }

    companion object {
        private const val BASE_URL = "https://favqs.com/api"
    }

    suspend fun getQuote(): QuoteResponse {
        return client.get("$BASE_URL/qotd").body()
    }

    fun close() {
        client.close()
    }
}