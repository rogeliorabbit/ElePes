package com.hitglynorthz.elepes.repository

import androidx.lifecycle.LiveData
import com.hitglynorthz.elepes.data.db.LibraryDao
import com.hitglynorthz.elepes.models.Artist
import com.hitglynorthz.elepes.models.Library
import kotlinx.coroutines.flow.Flow

class LibraryRepository(private val libraryDao: LibraryDao) {

    val getAllData: LiveData<List<Library>> = libraryDao.getAllData()

    suspend fun insertData(library: Library) {
        libraryDao.insertData(library)
    }

    suspend fun updateItem(library: Library) {
        libraryDao.updateItem(library)
    }

    suspend fun deleteItem(library: Library) {
        libraryDao.deleteItem(library)
    }

    // Artists
    fun showArtists(): Flow<List<Artist>> {
        return libraryDao.showArtists()
    }

    fun showArtistAlbums(artistQuery: String): Flow<List<Library>> {
        return libraryDao.showArtistAlbums(artistQuery)
    }

}