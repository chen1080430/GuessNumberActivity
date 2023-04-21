package com.mason.myapplication

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mason.myapplication.databinding.SmsItemBinding

class SmsAdapter : RecyclerView.Adapter<SmsAdapter.SmsViewHolder>() {

    class SmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding: SmsItemBinding

        var messageBody: TextView
        var dateTime: TextView
        var phoneNumber: TextView

        init {
            binding = SmsItemBinding.bind(itemView)
            phoneNumber = binding.textViewPhoneNumber
            messageBody = binding.textViewMessageBody
            dateTime = binding.textViewDateTime
        }
    }

//    class SmsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        // enable view binding
//        private val binding = SmsItemBinding.bind(itemView)
//
////        binding.textViewPhoneNumber.text = "123456"
//
//
//    }

    private var smsList: MutableList<Sms> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SmsViewHolder {
        var from = LayoutInflater.from(parent.context)
        var inflate = from.inflate(R.layout.sms_item, parent, false)
        return SmsViewHolder(inflate)
    }

    override fun getItemCount(): Int {
        return smsList.size
    }

    override fun onBindViewHolder(holder: SmsViewHolder, position: Int) {
        holder.phoneNumber.text = smsList[position].address
        smsList[position].body
        // convert time from timestamp
//        轉換timestamp 到 台灣用的時間格式
        val time = smsList[position].date.toLong()
        val date = android.text.format.DateFormat.format("yyyy-MM-dd hh:mm:ss", time)

        holder.messageBody.text = smsList[position].body
        holder.dateTime.text = date
    }

    fun setSmsList(sms: MutableList<Sms>) {
        smsList = sms
    }


}
