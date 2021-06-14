package com.hitglynorthz.elepes.models.search


import com.google.gson.annotations.SerializedName

data class ResultsX(
    @SerializedName("albummatches")
    val albummatches: Albummatches,
    @SerializedName("opensearch:itemsPerPage")
    val opensearchItemsPerPage: String,
    @SerializedName("opensearch:startIndex")
    val opensearchStartIndex: String,
    @SerializedName("opensearch:totalResults")
    val opensearchTotalResults: String
)