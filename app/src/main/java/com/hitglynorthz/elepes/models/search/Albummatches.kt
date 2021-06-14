package com.hitglynorthz.elepes.models.search


import com.google.gson.annotations.SerializedName

data class Albummatches(
    @SerializedName("album")
    val album: List<Album>
)