package com.mason.myapplication

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mason.myapplication.databinding.SmsItemBinding

class SmsViewHolder1(itemView: View) : RecyclerView.ViewHolder(itemView) {
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