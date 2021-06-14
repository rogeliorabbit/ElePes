package com.hitglynorthz.elepes.utils

class Constants {

    companion object {
        //
        const val BASE_URL = "http://ws.audioscrobbler.com/2.0/"
        const val API_KEY = "2a6cebf444687f70cf305a7fa917a309"
        const val QUERY_ARTIST = "&artist="
        const val QUERY_ALBUM = "&album="
        const val QUERY_FORMAT ="&format=json"

        // Bottom Sheet Preferences
        const val PREFERENCES_NAME = "sortfilter_preferences"
        const val SORT_DEFAULT = "Default"
        const val FILTER_DEFAULT = "Default"
        const val PREFERENCES_SORT_TYPE = "sortType"
        const val PREFERENCES_SORT_TYPE_ID = "sortTypeId"
        const val PREFERENCES_FILTER_TYPE = "filterType"
        const val PREFERENCES_FILTER_TYPE_ID = "filterTypeId"
    }

}