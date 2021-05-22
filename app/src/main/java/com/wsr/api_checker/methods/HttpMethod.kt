package com.wsr.api_checker.methods

import okhttp3.OkHttpClient

interface HttpMethod {

    //OkHttpClientを持つメンバ変数
    val client: OkHttpClient

    //Urlを渡したところにリクエストを送る関数
    suspend fun getRequest(url: String): Pair<Boolean, String>

}