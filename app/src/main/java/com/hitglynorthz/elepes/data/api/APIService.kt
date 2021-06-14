package com.hitglynorthz.elepes.data.api

import com.hitglynorthz.elepes.models.album.Album
import com.hitglynorthz.elepes.models.search.Results
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface APIService {

    @GET("?method=album.getinfo&api_key=2a6cebf444687f70cf305a7fa917a309") //&artist=Cher&album=Believe&format=json
    suspend fun getAlbumFromArtistAlbum(@QueryMap params: HashMap<String, String>): Response<Album>

    @GET("?method=album.search") // &album=believe&api_key=2a6cebf444687f70cf305a7fa917a309&format=json
    suspend fun getAlbumFromAlbum(@QueryMap params: HashMap<String, String>): Response<Results>

}