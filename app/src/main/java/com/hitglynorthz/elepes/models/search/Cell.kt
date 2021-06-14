package com.hitglynorthz.elepes.models.search

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cell(
    var album: String,
    var artist: String,
    var img: String
): Parcelable
