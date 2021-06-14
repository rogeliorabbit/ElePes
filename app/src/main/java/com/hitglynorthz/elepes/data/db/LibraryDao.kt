package com.hitglynorthz.elepes.data.db

import androidx.lifecycle.LiveData
import androidx.room.*
import com.hitglynorthz.elepes.models.Artist
import com.hitglynorthz.elepes.models.Library
import kotlinx.coroutines.flow.Flow

@Dao
interface LibraryDao {

    @Query("SELECT * FROM library_table ORDER BY id DESC")
    fun getAllData(): LiveData<List<Library>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertData(library: Library)

    @Update
    suspend fun updateItem(library: Library)

    @Delete
    suspend fun deleteItem(library: Library)

    // Artists
    @Query("SELECT artist, count(*) as nAlbums FROM library_table GROUP BY artist ORDER BY artist ASC")
    fun showArtists(): Flow<List<Artist>>

    @Query("SELECT * FROM library_table WHERE artist LIKE :artistQuery ORDER BY id")
    fun showArtistAlbums(artistQuery: String): Flow<List<Library>>

}