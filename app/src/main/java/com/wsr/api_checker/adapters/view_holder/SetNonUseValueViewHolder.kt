package com.wsr.api_checker.adapters.view_holder

import androidx.recyclerview.widget.RecyclerView
import com.wsr.api_checker.databinding.ItemsNonSetValueBinding
import com.wsr.api_checker.entities.Parameter

class SetNonUseValueViewHolder(private val binding: ItemsNonSetValueBinding) : RecyclerView.ViewHolder(binding.root) {

    fun setBind(parameter: Parameter){
        binding.parameter = parameter
    }
}