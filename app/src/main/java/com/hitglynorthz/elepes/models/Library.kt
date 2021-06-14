package com.hitglynorthz.elepes.models

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "library_table")
@Parcelize
data class Library(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var title: String,
    var artist: String,
    var record: String,
    var img: String,
    var type: Type,
    var fav: Int,
    var url: String,
    var nTracks: Int,
    var tracks: ArrayList<String>,
    var info: String
): Parcelable