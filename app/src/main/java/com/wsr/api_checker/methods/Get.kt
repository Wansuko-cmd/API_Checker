package com.wsr.api_checker.methods

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class Get(override val client: OkHttpClient) : HttpMethod {



    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getRequest(url: String): String = withContext(Dispatchers.IO){
        val request = Request.Builder().url(url).build()

        val response = client.newCall(request).execute()

        return@withContext response.body?.string().orEmpty()
    }
}