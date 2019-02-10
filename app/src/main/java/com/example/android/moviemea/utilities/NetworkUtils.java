package com.example.android.moviemea.utilities;

import android.net.Uri;

import com.example.android.moviemea.data.AppPreferences;

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
    private static final String MOVIES_ENDPOINT = "movie";
    private static final String DISCOVER_ENDPOINT = "discover";
    private static final String DISCOVER_MOVIES_PATH = "movie";

    private static final String API_KEY_PARAM = "api_key";
    private static final String API_KEY_VALUE = "";     // TODO: Put your themoviesdb API_KEY here

    private static final String LANGUAGE_PARAM_KEY = "language";
    private static final String SORT_BY_PARAM_KEY = "sort_by";

    private static final String IMAGE_SIZE_PATH_PARAM;

    private static String LANGUAGE_PARAM_VALUE;
    private static String SORT_BY_PARAM_VALUE;


    static {
        LANGUAGE_PARAM_VALUE = AppPreferences.getLanguageParamValue();
        SORT_BY_PARAM_VALUE = AppPreferences.getSortByParamValue();
        IMAGE_SIZE_PATH_PARAM = AppPreferences.getImageSizePathParam();
    }

    public static URL buildDiscoverMoviesUrl() {

        Uri uri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendPath(DISCOVER_ENDPOINT)
                .appendPath(DISCOVER_MOVIES_PATH)
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .appendQueryParameter(LANGUAGE_PARAM_KEY, LANGUAGE_PARAM_VALUE)
                .appendQueryParameter(SORT_BY_PARAM_KEY, SORT_BY_PARAM_VALUE)
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

        Uri uri = Uri.parse(API_BASE_URL).buildUpon()
                .appendPath(API_VERSION)
                .appendPath(MOVIES_ENDPOINT)
                .appendPath(Integer.toString(movieId))
                .appendQueryParameter(API_KEY_PARAM, API_KEY_VALUE)
                .appendQueryParameter(LANGUAGE_PARAM_KEY, LANGUAGE_PARAM_VALUE)
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

        Uri uri = Uri.parse(IMAGES_BASE_URL).buildUpon()
                .appendPath(IMAGE_SIZE_PATH_PARAM)
                .appendPath(imageRelativePath)
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
