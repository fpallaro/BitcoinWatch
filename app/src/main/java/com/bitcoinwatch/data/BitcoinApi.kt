package com.bitcoinwatch.data

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object BitcoinApi {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(10, TimeUnit.SECONDS)
        .build()

    suspend fun getBitcoinPrice(): Pair<Double, Double> = withContext(Dispatchers.IO) {
        val request = Request.Builder()
            .url("https://api.coingecko.com/api/v3/simple/price?ids=bitcoin&vs_currencies=usd&include_24hr_change=true")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw Exception("API Error: ${response.code}")

            val json = JSONObject(response.body?.string() ?: throw Exception("Empty response"))
            val bitcoin = json.getJSONObject("bitcoin")
            val price = bitcoin.getDouble("usd")
            val change24h = bitcoin.getDouble("usd_24h_change")

            Pair(price, change24h)
        }
    }
}
