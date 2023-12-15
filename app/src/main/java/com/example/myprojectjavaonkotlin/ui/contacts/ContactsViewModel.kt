package com.example.myprojectjavaonkotlin.ui.contacts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.myprojectjavaonkotlin.data.contacts.ContactsRepoImpl
import com.example.myprojectjavaonkotlin.domain.repo.contacts.ContactsRepo

class ContactsViewModel(
    private val contactsRepo: ContactsRepo = ContactsRepoImpl()
) : ViewModel() {

    val contacts: MutableLiveData<AppState> = MutableLiveData()

    fun getContacts() {
        contacts.value = AppState.Loading
        val answer = contactsRepo.getListContact()
        contacts.value = AppState.Success(answer)
    }
}