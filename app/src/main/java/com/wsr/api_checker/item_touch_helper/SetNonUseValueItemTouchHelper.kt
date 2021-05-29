package com.wsr.api_checker.item_touch_helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.wsr.api_checker.adapters.SetNonUseValueAdapter
import com.wsr.api_checker.adapters.SetValueAdapter
import com.wsr.api_checker.view_model.SetValueViewModel

class SetNonUseValueItemTouchHelper(
    private val setValueViewModel: SetValueViewModel,
    private val setValueAdapter: SetValueAdapter,
    private val setNonUseValueAdapter: SetNonUseValueAdapter
): ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val index = viewHolder.bindingAdapterPosition

        val tempList = setValueViewModel.nonUseParameters


        if(direction == ItemTouchHelper.LEFT){
            tempList.removeAt(index)
            setValueViewModel.nonUseParameters = tempList
        }else{

            tempList.removeAt(index).let{
                val param = setValueViewModel.parameters
                param.add(it)

                setValueViewModel.parameters = param

                setValueAdapter.notifyItemInserted(param.size)
            }
            setValueViewModel.nonUseParameters = tempList
        }

        setNonUseValueAdapter.notifyItemRemoved(index)

    }
}