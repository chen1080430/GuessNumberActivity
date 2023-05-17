package com.mason.myapplication.util

import android.util.Log
import android.view.View
import android.view.View.OnLongClickListener
import androidx.databinding.BindingAdapter

@BindingAdapter("app:longClick")
fun onLongClick(view : View, longClick : () -> Unit) {
   view.setOnLongClickListener {
       longClick()
       Log.d("BindingAdapter", "XXXXX> onLongClick: view ${view}")
       true
   }
}