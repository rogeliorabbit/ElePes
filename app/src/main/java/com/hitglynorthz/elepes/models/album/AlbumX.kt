package com.hitglynorthz.elepes.models.album


import com.google.gson.annotations.SerializedName

data class AlbumX(
    @SerializedName("artist")
    val artist: String,
    @SerializedName("image")
    val image: List<Image>,
    @SerializedName("mbid")
    val mbid: String?,
    @SerializedName("name")
    val name: String,
    @SerializedName("tracks")
    val tracks: Tracks?,
    @SerializedName("url")
    val url: String?,
    @SerializedName("wiki")
    val wiki: Wiki?
)