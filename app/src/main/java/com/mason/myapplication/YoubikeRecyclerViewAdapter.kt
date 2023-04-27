package com.mason.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mason.myapplication.data.Youbike2RealtimeItem
import com.mason.myapplication.databinding.SmsItemBinding
import com.mason.myapplication.databinding.YoubikeItemBinding

class YoubikeRecyclerViewAdapter : RecyclerView.Adapter<YoubikeRecyclerViewAdapter.YoubikeViewHolder>() {

    var bikeInfo = ArrayList<Youbike2RealtimeItem>()
    class YoubikeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         var stopName: TextView
         var stopSbi: TextView
         var stopRbi: TextView
        private var binding: YoubikeItemBinding

        init {
            binding = YoubikeItemBinding.bind(itemView)
            stopName = binding.textViewSna
            stopRbi = binding.textViewRbi
            stopSbi = binding.textViewSbi
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoubikeViewHolder {
        var inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.youbike_item, parent, false)
        return YoubikeViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return bikeInfo.size
    }

    override fun onBindViewHolder(holder: YoubikeViewHolder, position: Int) {
        holder.stopName.text = bikeInfo[position].sna
        var sbi = bikeInfo[position].sbi
        var tot = bikeInfo[position].tot
        holder.stopSbi.text = sbi.toString()
        holder.stopRbi.text = (tot-sbi).toString()
    }

    fun updateData(bikeList: java.util.ArrayList<Youbike2RealtimeItem>) {
        bikeInfo = bikeList
        notifyDataSetChanged()
    }

}
