package com.mason.myapplication.youbike

import androidx.recyclerview.widget.DiffUtil
import com.mason.myapplication.data.Youbike2RealtimeItem

class YoubikeItemDiffCallback(
    private var oldList: MutableList<Youbike2RealtimeItem>,
    private var newList: MutableList<Youbike2RealtimeItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].sno == newList[newItemPosition].sno
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
       return oldList[oldItemPosition].sna == newList[newItemPosition].sna
               && oldList[oldItemPosition].sno == newList[newItemPosition].sno
               && oldList[oldItemPosition].sbi == newList[newItemPosition].sbi
               && oldList[oldItemPosition].tot == newList[newItemPosition].tot
    }
}