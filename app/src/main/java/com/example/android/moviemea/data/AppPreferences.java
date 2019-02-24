package com.example.android.moviemea.data;

public class AppPreferences {

    private static final String LOG_TAG = AppPreferences.class.getSimpleName();

    private static String LANGUAGE_PARAM_VALUE;
    private static String SORT_BY_PARAM_VALUE;

    private static String MOVIES_ORDER_PATH_PARAM;
    private static final String NOW_PLAYING = "now_playing";
    private static final String POPULAR = "popular";
    private static final String TOP_RATED = "top_rated";
    private static final String UPCOMING = "upcoming";

    private static String IMAGE_SIZE_PATH_PARAM;


    static {
        LANGUAGE_PARAM_VALUE = "en-US";
        SORT_BY_PARAM_VALUE = "popularity.desc";

        MOVIES_ORDER_PATH_PARAM = UPCOMING;
        IMAGE_SIZE_PATH_PARAM = "w185";
    }


    public static String getLanguageParamValue() {
        return LANGUAGE_PARAM_VALUE;
    }

    public static void setLanguageParamValue(String languageParamValue) {
        LANGUAGE_PARAM_VALUE = languageParamValue;
    }

    public static String getSortByParamValue() {
        return SORT_BY_PARAM_VALUE;
    }

    public static void setSortByParamValue(String sortByParamValue) {
        SORT_BY_PARAM_VALUE = sortByParamValue;
    }

    public static String getMoviesOrderPathParam() {
        return MOVIES_ORDER_PATH_PARAM;
    }

    public static void setMoviesOrderPathParam(String moviesOrderPathParam) {
        MOVIES_ORDER_PATH_PARAM = moviesOrderPathParam;
    }

    public static String getImageSizePathParam() {
        return IMAGE_SIZE_PATH_PARAM;
    }

    public static void setImageSizePathParam(String imageSizePathParam) {
        IMAGE_SIZE_PATH_PARAM = imageSizePathParam;
    }
}
