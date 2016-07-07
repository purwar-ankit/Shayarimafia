package utils;

/**
 * Created by ankit.purwar on 7/4/2016.
 */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

import com.google.gson.Gson;

import model.Shayari;

public class SharedPreference {

    public static final String PREFS_NAME = "SHAYARI_APP";
    public static final String FAVORITES = "Shayari_Favorite";

    public SharedPreference() {
        super();
    }

    // This four methods are used for maintaining favorites.
    public void saveFavorites(Context context, List<Shayari> favorites) {
        SharedPreferences settings;
        Editor editor;
        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);
        editor = settings.edit();
        Gson gson = new Gson();
        String jsonFavorites = gson.toJson(favorites);
        editor.putString(FAVORITES, jsonFavorites);
        editor.commit();

        Log.d("sharedPrefsFAV" ," fav size is : "+ favorites.size());
    }

    public void addFavorite(Context context, Shayari shayari) {
        List<Shayari> favorites = getFavorites(context);
        if (favorites == null)
            favorites = new ArrayList<Shayari>();
        favorites.add(shayari);
        saveFavorites(context, favorites);
    }

    public void removeFavorite(Context context, Shayari shayari) {
        Log.d("sharedPrefs", " called remove fav " +" shayari: " + shayari.toString());
        List<Shayari> favorites = getFavorites(context);
        if (favorites != null) {
            favorites.remove(shayari);
            saveFavorites(context, favorites);
        }
    }

    public ArrayList<Shayari> getFavorites(Context context) {
        SharedPreferences settings;
        List<Shayari> favorites;

        settings = context.getSharedPreferences(PREFS_NAME,
                Context.MODE_PRIVATE);

        if (settings.contains(FAVORITES)) {
            String jsonFavorites = settings.getString(FAVORITES, null);
            Gson gson = new Gson();
            Shayari[] favoriteItems = gson.fromJson(jsonFavorites,
                    Shayari[].class);

            favorites = Arrays.asList(favoriteItems);
            favorites = new ArrayList<Shayari>(favorites);
        } else
            return null;

        return (ArrayList<Shayari>) favorites;
    }
}