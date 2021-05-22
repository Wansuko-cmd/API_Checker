package com.wsr.api_checker.methods

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class Post(override val client: OkHttpClient) : HttpMethod {

    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getRequest(url: String): Pair<Boolean, String> = withContext(Dispatchers.IO){
        val formBodyBuilder = FormBody.Builder()

        val request = Request.Builder()
            .url(url)
            .post(formBodyBuilder.build())
            .build()

        val response = client.newCall(request).execute()

        return@withContext Pair(true, response.body?.string().orEmpty())
    }
}