package com.hitglynorthz.elepes.viewmodels

import android.app.Application
import android.content.ClipData.Item
import android.content.Context
import android.text.TextUtils
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hitglynorthz.elepes.R
import com.hitglynorthz.elepes.models.Library
import com.hitglynorthz.elepes.models.Type


class SharedViewModel(application: Application): AndroidViewModel(application) {

    val emptyDatabase: MutableLiveData<Boolean> = MutableLiveData(true)

    fun checkIfDatabaseEmpty(library: List<Library>) {
        emptyDatabase.value = library.isEmpty()
    }

    //
    fun checkEmpty(string: String): Boolean {
        return TextUtils.isEmpty(string)
    }

    //
    fun hideShowFab(fab: FloatingActionButton, show: Boolean) {
        if(show) {
            if(!fab.isShown) {
                fab.show()
            }
        } else {
            if(fab.isShown) {
                fab.hide()
            }
        }
    }

    //
    fun parseType(context: Context, type: String): Type {
        return when(type) {
            context.resources.getStringArray(R.array.types)[0] -> {
                Type.CD}
            context.resources.getStringArray(R.array.types)[1] -> {
                Type.VINYL}
            context.resources.getStringArray(R.array.types)[2] -> {
                Type.CLOUD}
            else -> {
                Type.CD}
        }
    }

    //
    fun parseTypeToInt(type: Type): Int {
        return when(type) {
            Type.CD -> 0
            Type.VINYL -> 1
            Type.CLOUD -> 2
        }
    }

    //
    val allFilter: MutableLiveData<String> = MutableLiveData("test")
    fun setAllFilter(query: String) {
        allFilter.value = query
    }
    fun getAllFilter(): LiveData<String> {
        return allFilter
    }

}