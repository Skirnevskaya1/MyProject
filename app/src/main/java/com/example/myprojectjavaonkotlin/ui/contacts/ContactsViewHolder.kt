package com.example.myprojectjavaonkotlin.ui.contacts

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.myprojectjavaonkotlin.R
import com.example.myprojectjavaonkotlin.databinding.ItemContactsListBinding

class ContactsViewHolder(
    parent: ViewGroup
) : RecyclerView.ViewHolder(
    LayoutInflater.from(parent.context)
        .inflate(R.layout.item_contacts_list, parent, false)
) {

    private val binding: ItemContactsListBinding = ItemContactsListBinding.bind(itemView)

    fun bind(name: String) {
        binding.contactTextView.text = name
    }
}