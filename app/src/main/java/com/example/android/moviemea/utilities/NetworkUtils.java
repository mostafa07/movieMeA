package com.example.android.moviemea.utilities;

import android.net.Uri;

import com.example.android.moviemea.data.AppSharedPreferences;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;


public class NetworkUtils {

    private static final String LOG_TAG = NetworkUtils.class.getSimpleName();

    private static final String API_BASE_URL = "https://api.themoviedb.org";
    private static final String IMAGES_BASE_URL = "https://image.tmdb.org/t/p";
    private static final String API_VERSION = "3";

    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY_VALUE = "";     // TODO: Put your themoviesdb API_KEY here

    private static final String MOVIES_PATH = "movie";
    private static final String POPULAR_PATH = "popular";
    private static final String DISCOVER_PATH = "discover";
    private static final String TOP_RATED_PATH = "top_rated";

    private static final String LANGUAGE_PARAM_KEY = "language";
    private static final String PAGE_PARAM_KEY = "page";
    private static final String SORT_BY_PARAM_KEY = "sort_by";


    public static URL buildMoviesUrl(String... moviesOrderNotFromSharedPreferences) {
        final String languageParamValue = AppSharedPreferences.read(AppSharedPreferences.LANGUAGE_PARAM_VALUE_PREF_KEY,
                AppSharedPreferences.LANGUAGE_PARAM_VALUE_DEFAULT_VALUE);
        final String moviesOrderPathParam;

        if (moviesOrderNotFromSharedPreferences == null || moviesOrderNotFromSharedPreferences.length == 0) {
            moviesOrderPathParam = AppSharedPreferences.read(AppSharedPreferences.MOVIES_ORDER_PATH_PARAM_PREF_KEY,
                    AppSharedPreferences.MOVIES_ORDER_PATH_PARAM_DEFAULT_VALUE);
        } else {
            moviesOrderPathParam = moviesOrderNotFromSharedPreferences[0];
        }

        final int pageNum = 1;

        Uri uri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendPath(MOVIES_PATH)
                .appendPath(moviesOrderPathParam)
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .appendQueryParameter(LANGUAGE_PARAM_KEY, languageParamValue)
                .appendQueryParameter(PAGE_PARAM_KEY, Integer.toString(pageNum))
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        return url;
    }

    public static URL buildDiscoverMoviesUrl(int pageNum) {
        final String languageParamValue = AppSharedPreferences.read(AppSharedPreferences.LANGUAGE_PARAM_VALUE_PREF_KEY,
                AppSharedPreferences.LANGUAGE_PARAM_VALUE_DEFAULT_VALUE);
        final String sortByParamValue = AppSharedPreferences.read(AppSharedPreferences.SORT_BY_PARAM_VALUE_PREF_KEY,
                AppSharedPreferences.SORT_BY_PARAM_VALUE_DEFAULT_VALUE);

        Uri uri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendPath(DISCOVER_PATH)
                .appendPath(MOVIES_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .appendQueryParameter(LANGUAGE_PARAM_KEY, languageParamValue)
                .appendQueryParameter(SORT_BY_PARAM_KEY, sortByParamValue)
                .appendQueryParameter(PAGE_PARAM_KEY, Integer.toString(pageNum))
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        return url;
    }

    public static URL buildMovieDetailUrlById(int movieId) {
        final String languageParamValue = AppSharedPreferences.read(AppSharedPreferences.LANGUAGE_PARAM_VALUE_PREF_KEY,
                AppSharedPreferences.LANGUAGE_PARAM_VALUE_DEFAULT_VALUE);

        Uri uri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendPath(MOVIES_PATH)
                .appendPath(Integer.toString(movieId))
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .appendQueryParameter(LANGUAGE_PARAM_KEY, languageParamValue)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        return url;
    }

    public static URL buildImageUrl(String imageRelativePath) {
        final String imageSizePathParam = AppSharedPreferences.read(AppSharedPreferences.IMAGE_SIZE_PATH_PARAM_PREF_KEY,
                AppSharedPreferences.IMAGE_SIZE_PATH_PARAM_DEFAULT_VALUE);

        Uri uri = Uri.parse(IMAGES_BASE_URL).buildUpon()
                .appendPath(imageSizePathParam)
                .appendEncodedPath(imageRelativePath)
                .build();

        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException ex) {
            ex.printStackTrace();
        }

        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            if (scanner.hasNext()) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }
}
