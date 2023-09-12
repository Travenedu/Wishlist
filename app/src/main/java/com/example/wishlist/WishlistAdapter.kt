package com.example.wishlist

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WishlistAdapter(
    private val items: MutableList<WishlistItem>,
    private val onItemClicked: (String) -> Unit,
    private val onItemLongClicked: (Int) -> Unit
) : RecyclerView.Adapter<WishlistAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemName: TextView = itemView.findViewById(R.id.itemName)
        val itemPrice: TextView = itemView.findViewById(R.id.itemPrice)
        val itemUrl: TextView = itemView.findViewById(R.id.itemUrl)

        init {
            itemView.setOnLongClickListener {
                onItemLongClicked(adapterPosition)
                true // Consume the long-press event
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_wishlist, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = items[position]

        holder.itemName.text = currentItem.name
        holder.itemPrice.text = currentItem.price.toString()
        holder.itemUrl.text = currentItem.url

        // Handle item click to open URL
        holder.itemView.setOnClickListener {
            val url = currentItem.url
            if (url.isNotEmpty()) {
                try {
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    holder.itemView.context.startActivity(intent)
                } catch (e: ActivityNotFoundException) {
                    // Handle the case where no activity can handle the intent
                    showToast("No app found to open this URL.")
                }
            } else {
                // Handle the case where the URL is empty
                showToast("URL is empty.")
            }
        }
    }

    private fun showToast(message: String) {
        //Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
