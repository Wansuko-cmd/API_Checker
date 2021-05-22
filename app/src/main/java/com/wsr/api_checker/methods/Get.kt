package com.wsr.api_checker.methods

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.lang.IllegalArgumentException
import java.lang.reflect.InvocationTargetException
import java.net.ConnectException
import java.net.UnknownHostException

class Get(override val client: OkHttpClient) : HttpMethod() {



    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getRequest(url: String): Pair<Boolean, String> = withContext(Dispatchers.IO){

        try{
            val request = Request.Builder().url(url).build()

            val response = client.newCall(request).execute()

            return@withContext Pair(true, response.body?.string().orEmpty())
        }
        catch (e: Exception){
            return@withContext errorHandling(e)
        }
    }
}