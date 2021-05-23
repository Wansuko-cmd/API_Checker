package com.wsr.api_checker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.api_checker.databinding.ItemsSetValueBinding
import com.wsr.api_checker.view_holder.SetValueViewHolder
import com.wsr.api_checker.view_model.SetValueViewModel

class SetValueAdapter(
    private val setValueViewModel: SetValueViewModel
    ) : RecyclerView.Adapter<SetValueViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetValueViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SetValueViewHolder(ItemsSetValueBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return setValueViewModel.parameters.size
    }

    override fun onBindViewHolder(holder: SetValueViewHolder, position: Int) {
        holder.setBind(setValueViewModel.parameters[position])
    }
}