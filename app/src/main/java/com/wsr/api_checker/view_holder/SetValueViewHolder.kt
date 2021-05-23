package com.wsr.api_checker.view_holder

import androidx.recyclerview.widget.RecyclerView
import com.wsr.api_checker.databinding.ItemsSetValueBinding
import com.wsr.api_checker.entities.Parameter

class SetValueViewHolder(private val binding: ItemsSetValueBinding) : RecyclerView.ViewHolder(binding.root) {
    val key = binding.itemKey
    val value = binding.itemValue

    fun setBind(parameter: Parameter){
        binding.parameter = parameter
    }
}