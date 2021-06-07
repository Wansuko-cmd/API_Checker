package com.wsr.api_checker.item_touch_helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.wsr.api_checker.adapters.SetNonUseValueAdapter
import com.wsr.api_checker.adapters.SetValueAdapter
import com.wsr.api_checker.entities.Parameter
import com.wsr.api_checker.view_model.SetValueViewModel

class SetValueItemTouchHelper(
    private val setValueViewModel: SetValueViewModel,
    private val setValueAdapter: SetValueAdapter,
    private val setNonUseValueAdapter: SetNonUseValueAdapter
): ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN,
    ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
) {

    private var parameterList = mutableListOf<Parameter>()
    private var nonUserParameterList = mutableListOf<Parameter>()

    private var isDrag = false

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        super.onSelectedChanged(viewHolder, actionState)

        parameterList = setValueViewModel.parameters
        nonUserParameterList = setValueViewModel.nonUseParameters

        if(actionState == ItemTouchHelper.ACTION_STATE_DRAG) isDrag = true
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        val fromPosition = viewHolder.bindingAdapterPosition
        val toPosition = target.bindingAdapterPosition

        val fromValue = parameterList[fromPosition]
        parameterList[fromPosition] = parameterList[toPosition]
        parameterList[toPosition] = fromValue

        setValueAdapter.notifyItemMoved(fromPosition, toPosition)

        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val index = viewHolder.bindingAdapterPosition

        //左にスワイプしたとき（削除）
        if(direction == ItemTouchHelper.LEFT){
            parameterList.removeAt(index)
        }
        //右にスワイプしたとき（無効化）
        else{
            parameterList.removeAt(index).let{
                nonUserParameterList.add(it)

                setValueViewModel.nonUseParameters = nonUserParameterList
                setNonUseValueAdapter.notifyItemInserted(nonUserParameterList.size)
            }
        }

        setValueViewModel.parameters = parameterList
        setValueAdapter.notifyItemRemoved(index)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {

        if(isDrag) {
            setValueViewModel.parameters = parameterList
            isDrag = false
        }

        super.clearView(recyclerView, viewHolder)
    }
}