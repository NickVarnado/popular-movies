package com.nbvarnado.popularmovies.data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class Preferences {

    /**
     * Get's the sorting prefernce for the movies.
     * @param context
     * @param sortKey
     * @return
     */
    public static String getSortingPreference(Context context, String sortKey) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(sortKey, "popular");
    }

    /**
     * Set's the sorting prefernce for the movies.
     * @param context
     * @param sort
     */
    public static void setSortingPreference(Context context, String sort) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        preferences.edit().putString("sort_key", sort).apply();

    }
}
