package com.example.wishlist

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var wishlistItems: MutableList<WishlistItem>
    private lateinit var wishlistAdapter: WishlistAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val onOpenUrl: (String) -> Unit = { url ->
            // Handle opening URL here
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            startActivity(intent)
        }

        recyclerView = findViewById(R.id.recyclerView)
        wishlistItems = mutableListOf()
        wishlistAdapter = WishlistAdapter(wishlistItems, onOpenUrl) { position ->
            // Handle item deletion
            removeItem(position)
        }

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = wishlistAdapter

        val editTextName = findViewById<EditText>(R.id.editTextName)
        val editTextPrice = findViewById<EditText>(R.id.editTextPrice)
        val editTextURL = findViewById<EditText>(R.id.editTextURL)
        val btnAddItem = findViewById<Button>(R.id.btnAddItem)

        btnAddItem.setOnClickListener {
            val name = editTextName.text.toString()
            val priceText = editTextPrice.text.toString()
            val url = editTextURL.text.toString()

            if (name.isNotEmpty() && priceText.isNotEmpty()) {
                val price = priceText.toDoubleOrNull()
                if (price != null && price > 0) {
                    val newItem = WishlistItem(name, price, url)
                    wishlistItems.add(newItem)
                    Log.d("WishlistApp", "Item added: $newItem")
                    wishlistAdapter.notifyDataSetChanged()
                    clearEditTextFields()
                } else {
                    // Handle invalid price input (e.g., show a Toast or set an error message)
                    showToast("Please enter a valid positive price.")
                }
            } else {
                // Handle empty name or price input (e.g., show a Toast or set an error message)
                showToast("Name and price are required fields.")
            }
        }

    }

    private fun clearEditTextFields() {
        findViewById<EditText>(R.id.editTextName).text.clear()
        findViewById<EditText>(R.id.editTextPrice).text.clear()
        findViewById<EditText>(R.id.editTextURL).text.clear()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun removeItem(position: Int) {
        if (position >= 0 && position < wishlistItems.size) {
            wishlistItems.removeAt(position)
            wishlistAdapter.notifyItemRemoved(position)
        }
    }
}