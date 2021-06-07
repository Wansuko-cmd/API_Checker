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

    var deleteValue: Pair<String, Parameter>?  = null

    //ユーザーが入力したパラメータのリスト
    private val _nonUseParameters: MutableLiveData<MutableList<Parameter>> = MutableLiveData()
    var nonUseParameters: MutableList<Parameter>
        get() = _nonUseParameters.value ?: mutableListOf()
        set(value) = _nonUseParameters.postValue(value)
}