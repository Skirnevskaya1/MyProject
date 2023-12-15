package com.example.myprojectjavaonkotlin.ui

import android.app.Activity
import android.view.MenuItem

object TitleUtils {

    fun setupTitle(activity: Activity, menuItem: MenuItem) {
        activity.title = menuItem.title
    }
}