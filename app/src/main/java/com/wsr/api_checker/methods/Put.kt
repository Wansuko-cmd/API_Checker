package com.wsr.api_checker.methods

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class Put(override val client: OkHttpClient) : HttpMethod() {

    //Urlを渡したところにリクエストを送る関数
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getRequest(url: String): Pair<Boolean, String> = withContext(Dispatchers.IO) {

        try{
            val formBodyBuilder = FormBody.Builder()

            val request = Request.Builder()
                .url(url)
                .put(formBodyBuilder.build())
                .build()

            val response = client.newCall(request).execute()

            return@withContext Pair(true, response.body?.string().orEmpty())
        }
        catch (e: Exception){
            return@withContext errorHandling(e)
        }
    }
}