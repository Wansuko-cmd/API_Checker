package com.wsr.api_checker.adapters.view_holder

import androidx.recyclerview.widget.RecyclerView
import com.wsr.api_checker.databinding.ItemsSetValueBinding
import com.wsr.api_checker.entities.Parameter

class SetValueViewHolder(private val binding: ItemsSetValueBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setBind(parameter: Parameter){
        binding.parameter = parameter
    }
}