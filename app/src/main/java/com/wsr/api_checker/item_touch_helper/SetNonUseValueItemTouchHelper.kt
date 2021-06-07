package com.wsr.api_checker.item_touch_helper

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.wsr.api_checker.adapters.SetNonUseValueAdapter
import com.wsr.api_checker.adapters.SetValueAdapter
import com.wsr.api_checker.entities.Parameter
import com.wsr.api_checker.view_model.SetValueViewModel

class SetNonUseValueItemTouchHelper(
    private val setValueViewModel: SetValueViewModel,
    private val setValueAdapter: SetValueAdapter,
    private val setNonUseValueAdapter: SetNonUseValueAdapter,
    private val snackBar: Snackbar
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

        val fromValue = nonUserParameterList[fromPosition]
        nonUserParameterList[fromPosition] = nonUserParameterList[toPosition]
        nonUserParameterList[toPosition] = fromValue

        setNonUseValueAdapter.notifyItemMoved(fromPosition, toPosition)

        return false
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val index = viewHolder.bindingAdapterPosition

        //左にスワイプしたとき（削除）
        if(direction == ItemTouchHelper.LEFT){
            nonUserParameterList.removeAt(index).let{
                setValueViewModel.deleteValue = Pair("nonUseParameters", it)
            }

            snackBar.show()
        }
        //右にスワイプしたとき（有効化）
         else{

            nonUserParameterList.removeAt(index).let{
                parameterList.add(it)

                setValueViewModel.parameters = parameterList
                setValueAdapter.notifyItemInserted(parameterList.size)
            }
        }

        setValueViewModel.nonUseParameters = nonUserParameterList
        setNonUseValueAdapter.notifyItemRemoved(index)
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {

        if(isDrag) {
            setValueViewModel.nonUseParameters = nonUserParameterList
            isDrag = false
        }

        super.clearView(recyclerView, viewHolder)
    }
}