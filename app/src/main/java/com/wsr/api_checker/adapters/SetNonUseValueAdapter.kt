package com.wsr.api_checker.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wsr.api_checker.adapters.view_holder.SetNonUseValueViewHolder
import com.wsr.api_checker.databinding.ItemsNonSetValueBinding
import com.wsr.api_checker.view_model.SetValueViewModel
import java.lang.IndexOutOfBoundsException

class SetNonUseValueAdapter(
    private val setValueViewModel: SetValueViewModel
) : RecyclerView.Adapter<SetNonUseValueViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SetNonUseValueViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return SetNonUseValueViewHolder(ItemsNonSetValueBinding.inflate(inflater, parent, false))
    }

    override fun getItemCount(): Int {
        return setValueViewModel.nonUseParameters.size
    }

    override fun onBindViewHolder(holder: SetNonUseValueViewHolder, position: Int) {
        try{
            holder.setBind(setValueViewModel.nonUseParameters[position])
        }catch (e: IndexOutOfBoundsException){}
    }
}