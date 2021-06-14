package com.hitglynorthz.elepes.models.album


import com.google.gson.annotations.SerializedName

data class Album(
    @SerializedName("album")
    val album: AlbumX
)