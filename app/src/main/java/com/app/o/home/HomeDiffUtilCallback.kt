package com.app.o.home

import android.support.annotation.Nullable
import android.support.v7.util.DiffUtil
import com.app.o.api.home.HomePostItem

class HomeDiffUtilCallback(private val oldList: List<HomePostItem>, private val newList: List<HomePostItem>) : DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldList.size
    }

    override fun getNewListSize(): Int {
        return newList.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].post_id == newList[newItemPosition].post_id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldData = oldList[oldItemPosition]
        val newData = newList[newItemPosition]

        return oldData.post_id == newData.post_id
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}