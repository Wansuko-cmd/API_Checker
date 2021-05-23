package com.wsr.api_checker.view_holder

import androidx.recyclerview.widget.RecyclerView
import com.wsr.api_checker.databinding.ItemsSetValueBinding

class SetValueViewHolder(binding: ItemsSetValueBinding) : RecyclerView.ViewHolder(binding.root) {
    val key = binding.itemKey
    val value = binding.itemValue
}