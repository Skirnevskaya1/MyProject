package com.example.myprojectjavaonkotlin.data.contacts

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract
import com.example.myprojectjavaonkotlin.ContextProvider
import com.example.myprojectjavaonkotlin.IContextProvider
import com.example.myprojectjavaonkotlin.domain.repo.contacts.ContactsRepo

class ContactsRepoImpl(
    contextProvider: IContextProvider = ContextProvider
) : ContactsRepo {

    private val contentResolver: ContentResolver = contextProvider.context.contentResolver

    @SuppressLint("Range")
    override fun getListContact(): List<String> {
        val cursorWithContacts: Cursor? =
            contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                null,
                null,
                null,
                ContactsContract.Contacts.DISPLAY_NAME
            )

        val answer = mutableListOf<String>()
        cursorWithContacts?.let { cursor ->
            cursor.moveToFirst()
            do {
                answer.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)))
                cursor.moveToNext()
            } while (!cursor.isAfterLast)
        }
        cursorWithContacts?.close()
        return answer
    }
}