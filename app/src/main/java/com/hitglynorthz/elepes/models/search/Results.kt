package com.hitglynorthz.elepes.models.search


import com.google.gson.annotations.SerializedName

data class Results(
    //var results: List<ResultsX>?

    @SerializedName("results")
    val results: ResultsX
)