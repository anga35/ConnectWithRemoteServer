package com.example.connectwithremoteserver

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.ColorInt
import androidx.annotation.RequiresPermission
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_message.view.*

class MessageAdapter(val context:Context,val messageList:ArrayList<Message>):RecyclerView.Adapter<MessageAdapter.ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
   return ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_message,parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.itemText.text=messageList[position].text

        if(messageList[position].sent==false){
        var params=(holder.itemText.layoutParams as RelativeLayout.LayoutParams)
            params.removeRule(RelativeLayout.ALIGN_PARENT_RIGHT)
            params.addRule(RelativeLayout.ALIGN_PARENT_LEFT)
            holder.itemText.layoutParams=params
        holder.itemText.background=ContextCompat.getDrawable(context,R.drawable.button_received)
        holder.itemText.setTextColor(Color.parseColor("#000000"))
        }

    }

    override fun getItemCount(): Int {
        return messageList.size
    }

    class ViewHolder(view: View):RecyclerView.ViewHolder(view){
        var itemText=view.tv_itemMessage
        var itemLayout=view.ll_messageBackground
    }


}