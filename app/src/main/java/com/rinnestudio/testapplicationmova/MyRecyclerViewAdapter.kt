package com.rinnestudio.testapplicationmova

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso
import java.lang.Exception

class MyRecyclerViewAdapter(private val itemList: ArrayList<Item>) :
    RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false)
    )


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(itemList[position])
    }

    fun addItem(item: Item) {
        itemList.removeIf {
            it.searchStr == item.searchStr
        }
        itemList.add(0, item)
        notifyDataSetChanged()
    }

    fun setItemList(itemList: List<Item>) {
        this.itemList.clear()
        this.itemList.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun getItemCount() = itemList.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textView)
        private val imageView: ImageView = itemView.findViewById(R.id.imageView)

        fun bind(item: Item) {
            textView.text = item.searchStr
            bindImage(item)
        }

        private fun bindImage(item: Item, position: Int = 0) {
            if (item.url.size > position) {
                Picasso.get().load(item.url[position]).into(imageView, object : Callback {
                    override fun onSuccess() {
                        if (item.url.size > 1) {
                            RealmManager().updateUrlListForItem(item, position)
                        }
                    }

                    override fun onError(e: Exception?) {
                       Log.i("Log_tag", "Error")
                        bindImage(item, position + 1)
                    }
                })
            }
        }

    }
}
