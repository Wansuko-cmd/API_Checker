package com.wsr.api_checker.methods

import com.wsr.api_checker.view_model.SetValueViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request

class Put(override val client: OkHttpClient) : HttpMethod() {

    //Urlを渡したところにリクエストを送る関数
    @Suppress("BlockingMethodInNonBlockingContext")
    override suspend fun getRequest(setValueViewModel: SetValueViewModel): Pair<Boolean, String> = withContext(Dispatchers.IO) {

        try{

            //Urlの取得
            val url = setValueViewModel.url

            //リクエストボディの作成
            val formBodyBuilder = FormBody.Builder()

            setValueViewModel.parameters.forEach {
                formBodyBuilder.add(it.key, it.value)
            }

            val request = Request.Builder()
                .url(url)
                .put(formBodyBuilder.build())
                .build()

            //リクエストの送信
            val response = client.newCall(request).execute()

            return@withContext Pair(true, response.body?.string().orEmpty())
        }
        catch (e: Exception){
            return@withContext errorHandling(e)
        }
    }
}