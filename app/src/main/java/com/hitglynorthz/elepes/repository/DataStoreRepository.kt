package com.hitglynorthz.elepes.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.*
import androidx.datastore.preferences.core.*
import com.hitglynorthz.elepes.utils.Constants.Companion.FILTER_DEFAULT
import com.hitglynorthz.elepes.utils.Constants.Companion.PREFERENCES_FILTER_TYPE
import com.hitglynorthz.elepes.utils.Constants.Companion.PREFERENCES_FILTER_TYPE_ID
import com.hitglynorthz.elepes.utils.Constants.Companion.PREFERENCES_NAME
import com.hitglynorthz.elepes.utils.Constants.Companion.PREFERENCES_SORT_TYPE
import com.hitglynorthz.elepes.utils.Constants.Companion.PREFERENCES_SORT_TYPE_ID
import com.hitglynorthz.elepes.utils.Constants.Companion.SORT_DEFAULT
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.android.scopes.ViewModelScoped
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private val Context.dataStore by preferencesDataStore(PREFERENCES_NAME)

@ViewModelScoped
class DataStoreRepository @Inject constructor(@ApplicationContext private val context: Context) {

    private object PreferenceKeys {
        val selectedSortType = stringPreferencesKey(PREFERENCES_SORT_TYPE)
        val selectedSortTypeId = intPreferencesKey(PREFERENCES_SORT_TYPE_ID)
        val selectedFilterType = stringPreferencesKey(PREFERENCES_FILTER_TYPE)
        val selectedFilterTypeId = intPreferencesKey(PREFERENCES_FILTER_TYPE_ID)
    }

    private val dataStore: DataStore<Preferences> = context.dataStore

    suspend fun saveSortAndFilterType(sortType: String, sortTypeId: Int, filterType: String, filterTypeId: Int) {
        dataStore.edit { preferences ->
            preferences[PreferenceKeys.selectedSortType] = sortType
            preferences[PreferenceKeys.selectedSortTypeId] = sortTypeId
            preferences[PreferenceKeys.selectedFilterType] = filterType
            preferences[PreferenceKeys.selectedFilterTypeId] = filterTypeId
        }
    }

    val readSortAndFilterType: Flow<SortAndFilterType> = dataStore.data
        .catch { exception ->
            if(exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val selectedSortType = preferences[PreferenceKeys.selectedSortType] ?: SORT_DEFAULT
            val selectedSortTypeId = preferences[PreferenceKeys.selectedSortTypeId] ?: 0
            val selectedFilterType = preferences[PreferenceKeys.selectedFilterType] ?: FILTER_DEFAULT
            val selectedFilterTypeId = preferences[PreferenceKeys.selectedFilterTypeId] ?: 0
            SortAndFilterType(
                selectedSortType,
                selectedSortTypeId,
                selectedFilterType,
                selectedFilterTypeId
            )

        }

}

data class SortAndFilterType(
    val selectedSortType: String,
    val selectedSortTypeId: Int,
    val selectedFilterType: String,
    val selectedFilterTypeId: Int
)