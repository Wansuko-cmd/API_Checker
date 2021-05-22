package com.wsr.api_checker.methods

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class Delete(override val client: OkHttpClient) : HttpMethod() {

    override suspend fun getRequest(url: String): Pair<Boolean, String> = withContext(Dispatchers.IO){

        try{
            val request = Request.Builder().url(url).build()

            val response = client.newCall(request).execute()

            return@withContext Pair(true, response.toString())
        }catch (e: Exception){
            return@withContext errorHandling(e)
        }
    }
}