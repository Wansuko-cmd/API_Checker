package com.wsr.api_checker.view_model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.wsr.api_checker.entities.Parameter

class SetValueViewModel: ViewModel() {

    private val _parameters: MutableLiveData<MutableList<Parameter>> = MutableLiveData()
    var parameters: MutableList<Parameter>
        get() = _parameters.value ?: mutableListOf()
        set(value) = _parameters.postValue(value)
}