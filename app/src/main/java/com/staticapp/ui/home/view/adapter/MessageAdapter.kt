package com.staticapp.ui.home.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.staticapp.R
import com.staticapp.databinding.ItemMessageBinding
import com.staticapp.roomDb.MessageData

class MessageAdapter(
    var msgList: ArrayList<MessageData>,
    val onItemClick: (Int) -> Unit,
    val isRecentMsg: Boolean
) :
    RecyclerView.Adapter<MessageAdapter.MsgViewHolder>() {
    class MsgViewHolder(val itemMessageBinding: ItemMessageBinding, val onItemClick: (Int) -> Unit) :
        RecyclerView.ViewHolder(itemMessageBinding.root) {

            init {
                itemView.setOnClickListener {
                    onItemClick(adapterPosition)
                }
            }
    }
     override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MsgViewHolder {

      val binding:ItemMessageBinding=ItemMessageBinding.inflate(LayoutInflater.from(parent.context),parent,false)
      return MsgViewHolder(binding,{onItemClick(it)})
    }

    override fun getItemCount(): Int {
       return msgList.size
    }


    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }

    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }

    override fun onBindViewHolder(holder: MsgViewHolder, position: Int) {

            msgList.get(position).message?.let {
                holder.itemMessageBinding.tvAddNewMsg.setText(""+it)
            }

        if(isRecentMsg)
        {
            holder.itemMessageBinding.imgPhone.setImageResource(R.drawable.img_phone_msg)
        }else{
            holder.itemMessageBinding.imgPhone.setImageResource(R.drawable.img_dot)

        }




    }
}