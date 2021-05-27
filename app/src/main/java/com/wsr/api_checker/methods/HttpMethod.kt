package com.wsr.api_checker.methods

import okhttp3.OkHttpClient
import java.lang.IllegalArgumentException
import java.lang.reflect.InvocationTargetException
import java.net.ConnectException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

abstract class HttpMethod {

    //OkHttpClientを持つメンバ変数
    abstract val client: OkHttpClient

    //Urlを渡したところにリクエストを送る関数
    abstract suspend fun getRequest(url: String): Pair<Boolean, String>

    //通信の際起こったエラーのハンドリング
    fun errorHandling(e: Exception): Pair<Boolean, String>{
        return when(e){
            is InvocationTargetException -> Pair(false, e.toString())
            is UnknownHostException -> Pair(false, "UnknownHostException")
            is IllegalArgumentException -> Pair(false, "IllegalArgumentException")
            is ConnectException -> Pair(false, "ConnectException")
            is SSLHandshakeException -> Pair(false, "SSLHandshakeException")
            else -> return Pair(true, e.toString())
        }
    }
}