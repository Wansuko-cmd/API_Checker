package com.wsr.api_checker.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsr.api_checker.entities.Parameter

class SetValueViewModel: ViewModel() {

    //ユーザーが入力したURL
    private val _url: MutableLiveData<String> = MutableLiveData()
    var url: String
        get() = _url.value ?: ""
        set(value) = _url.postValue(value)

    //ユーザーが入力したパラメータのリスト
    private val _parameters: MutableLiveData<MutableList<Parameter>> = MutableLiveData()
    var parameters: MutableList<Parameter>
        get() = _parameters.value ?: mutableListOf()
        set(value) = _parameters.postValue(value)

    //パラメータも書いたURLを返す関数
    fun getUrlWithParameters(): String{
        var resultUrl = url

        for((index, parameter) in parameters.withIndex()){
            resultUrl += if(index == 0) "?" else "&"
            resultUrl += "${parameter.key}=${parameter.value}"
        }

        return resultUrl
    }
}