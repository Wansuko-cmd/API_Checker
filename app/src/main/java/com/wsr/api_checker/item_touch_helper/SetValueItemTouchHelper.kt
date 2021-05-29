package com.wsr.api_checker.item_touch_helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.wsr.api_checker.adapters.SetNonUseValueAdapter
import com.wsr.api_checker.adapters.SetValueAdapter
import com.wsr.api_checker.view_model.SetValueViewModel

class SetValueItemTouchHelper(
    private val setValueViewModel: SetValueViewModel,
    private val setValueAdapter: SetValueAdapter,
    private val setNonUseValueAdapter: SetNonUseValueAdapter
): ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {
    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        TODO("Not yet implemented")
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val index = viewHolder.bindingAdapterPosition

        val tempList = setValueViewModel.parameters

        if(direction == ItemTouchHelper.LEFT){
            tempList.removeAt(index)
            setValueViewModel.parameters = tempList
        }else{
            tempList.removeAt(index).let{
                val param = setValueViewModel.nonUseParameters
                param.add(it)

                setValueViewModel.nonUseParameters = param
                setNonUseValueAdapter.notifyItemInserted(param.size)
            }
            setValueViewModel.parameters = tempList
        }

        setValueAdapter.notifyItemRemoved(index)
    }
}