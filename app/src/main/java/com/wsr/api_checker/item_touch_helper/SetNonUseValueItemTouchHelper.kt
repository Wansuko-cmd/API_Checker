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

        val tempList = setValueViewModel.nonUseParameters
        tempList.removeAt(index).let{
            val param = setValueViewModel.parameters
            param.add(it)

            setValueViewModel.parameters = param

            setValueAdapter.notifyItemInserted(param.size)
        }
        setValueViewModel.nonUseParameters = tempList

        setNonUseValueAdapter.notifyItemRemoved(index)
    }
}