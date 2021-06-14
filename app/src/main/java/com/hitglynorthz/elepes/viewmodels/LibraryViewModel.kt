package com.hitglynorthz.elepes.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.hitglynorthz.elepes.data.db.DBHelper
import com.hitglynorthz.elepes.models.Artist
import com.hitglynorthz.elepes.models.Library
import com.hitglynorthz.elepes.repository.LibraryRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LibraryViewModel(application: Application): AndroidViewModel(application) {

    private val libraryDao = DBHelper.getDatabase(application).libraryDao()
    private val repository: LibraryRepository

    val getAllData: LiveData<List<Library>>

    init {
        repository = LibraryRepository(libraryDao)
        getAllData = repository.getAllData
    }

    fun insertData(library: Library) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertData(library)
        }
    }

    fun updateItem(library: Library) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateItem(library)
        }
    }

    fun deleteItem(library: Library) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteItem(library)
        }
    }

    // Artists
    fun showArtists(): LiveData<List<Artist>> {
        return repository.showArtists().asLiveData()
    }

    fun showArtistAlbums(artistQuery: String): LiveData<List<Library>> {
        return repository.showArtistAlbums(artistQuery).asLiveData()
    }

}