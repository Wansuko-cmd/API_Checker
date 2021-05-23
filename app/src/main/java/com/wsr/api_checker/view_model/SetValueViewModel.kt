package com.wsr.api_checker.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SetValueViewModel: ViewModel() {

    private val _parameters: MutableLiveData<MutableMap<String, String>> = MutableLiveData()
    var parameters: MutableMap<String, String>
        get() = _parameters.value ?: mutableMapOf()
        set(value) = _parameters.postValue(value)
}