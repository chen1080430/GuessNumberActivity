package com.mason.myapplication.youbike

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filter.FilterListener
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.mason.myapplication.R
import com.mason.myapplication.data.Youbike2RealtimeItem
import com.mason.myapplication.databinding.YoubikeItemBinding

class YoubikeRecyclerViewAdapter : RecyclerView.Adapter<YoubikeRecyclerViewAdapter.YoubikeViewHolder>() , Filterable {

    private lateinit var mFilterListener: FilterListener
    private var filterString: String = ""
    private var allbikeInfo = ArrayList<Youbike2RealtimeItem>()
    private var newFilteredBikeInfo = ArrayList<Youbike2RealtimeItem>()
    private var oldFilteredBikeInfo = ArrayList<Youbike2RealtimeItem>()
    class YoubikeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         var stopName: TextView
         var stopSbi: TextView
         var stopRbi: TextView
         var stopArea: TextView
        private var binding: YoubikeItemBinding

        init {
            binding = YoubikeItemBinding.bind(itemView)
            stopName = binding.textViewSna
            stopRbi = binding.textViewRbi
            stopSbi = binding.textViewSbi
            stopArea = binding.textViewSarea
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): YoubikeViewHolder {
        var inflate =
            LayoutInflater.from(parent.context).inflate(R.layout.youbike_item, parent, false)
        return YoubikeViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return oldFilteredBikeInfo.size
    }

    override fun onBindViewHolder(holder: YoubikeViewHolder, position: Int) {
        holder.stopName.text = newFilteredBikeInfo[position].sna
        var sbi = newFilteredBikeInfo[position].sbi
        var tot = newFilteredBikeInfo[position].tot
        holder.stopSbi.text = sbi.toString()
        holder.stopRbi.text = (tot-sbi).toString()
        holder.stopArea.text = newFilteredBikeInfo[position].sarea
    }

    fun updateData(bikeList: java.util.ArrayList<Youbike2RealtimeItem>) {
        Log.d(TAG, "XXXXX> updateData: bikeList: ${bikeList.size}")
        allbikeInfo = bikeList
//        filter.filter(filterString)
        updateFilter()
//        notifyDataSetChanged()
    }

    fun updateFilter(word: String = "") {
        filterString = word
        filter.filter(filterString)
    }

    fun setFilterListener(filterListener: FilterListener) {
        mFilterListener = filterListener
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charSearch = constraint.toString()
                Log.d(TAG, "XXXXX> performFiltering: charSearch: $charSearch")
                newFilteredBikeInfo = ArrayList()
                if (charSearch.isEmpty()) {
                    newFilteredBikeInfo = allbikeInfo
                } else {
                    val resultList = ArrayList<Youbike2RealtimeItem>()
                    for (row in allbikeInfo) {
                        if (row.sna.toLowerCase().contains(charSearch.toLowerCase())) {
                            resultList.add(row)
                        } else if (row.sarea.toLowerCase().contains(charSearch.toLowerCase())) {
                            resultList.add(row)
                        }
                    }
                    newFilteredBikeInfo = resultList
                }
                return FilterResults().apply {
                    values = newFilteredBikeInfo
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                newFilteredBikeInfo = results?.values as ArrayList<Youbike2RealtimeItem>
                mFilterListener.onFilterComplete(newFilteredBikeInfo.size)
                // use diffutil to compare mfilteredBikeInfo and allbikeInfo
                val diffResult = DiffUtil.calculateDiff(YoubikeItemDiffCallback(oldFilteredBikeInfo, newFilteredBikeInfo))
                diffResult.dispatchUpdatesTo(this@YoubikeRecyclerViewAdapter)
                oldFilteredBikeInfo = newFilteredBikeInfo
//                notifyDataSetChanged()
            }

        }

    }

    companion object {
        private const val TAG = "YoubikeRecyclerViewAdap"
    }
}
