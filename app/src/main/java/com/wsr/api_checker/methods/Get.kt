package com.wsr.api_checker.methods

import com.wsr.api_checker.view_model.SetValueViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import java.net.UnknownHostException

class Get(override val client: OkHttpClient) : HttpMethod() {

    //Urlを渡したところにリクエストを送る関数
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getRequest(setValueViewModel: SetValueViewModel): Pair<Boolean, String> = withContext(Dispatchers.IO){

        try{

            //Url作成
            val url = setValueViewModel.url
                .toHttpUrlOrNull()
                ?.newBuilder()

            //パラメーターを設定
            setValueViewModel.parameters.forEach {
                url?.addQueryParameter(it.key, it.value)
            }

            //Urlを生成できればリクエストを投げる
            url?.let{
                val request = Request
                    .Builder()
                    .url(it.build())
                    .build()

                val response = client.newCall(request).execute()

                return@withContext Pair(true, response.body?.string().orEmpty())
            }

            //urlがnullであれば例外を投げる
            throw UnknownHostException()
        }
        catch (e: Exception){
            return@withContext errorHandling(e)
        }
    }
}