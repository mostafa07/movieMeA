package com.example.android.moviemea.utilities;

import java.util.ArrayList;
import java.util.List;

import com.example.android.moviemea.models.remote.Movie;
import com.example.android.moviemea.models.remote.MovieDetail;
import com.example.android.moviemea.models.remote.Review;
import com.example.android.moviemea.models.remote.Video;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TheMoviesDbJsonUtils {

    private static final String RESULTS = "results";

    private static final String ID = "id";
    private static final String IMDB_ID = "imdb_id";
    private static final String TITLE = "title";
    private static final String ORIGINAL_TITLE = "original_title";
    private static final String ORIGINAL_LANGUAGE = "original_language";
    private static final String OVERVIEW = "overview";
    private static final String TAGLINE = "tagline";
    private static final String STATUS = "status";
    private static final String RELEASE_DATE = "release_date";
    private static final String RUNTIME = "runtime";
    private static final String BELONGS_TO_COLLECTION = "belongs_to_collection";
    private static final String HOMEPAGE = "homepage";
    private static final String POSTER_PATH = "poster_path";
    private static final String BACKDROP_PATH = "backdrop_path";
    private static final String BUDGET = "budget";
    private static final String REVENUE = "revenue";
    private static final String POPULARITY = "popularity";
    private static final String VOTE_AVERAGE = "vote_average";
    private static final String VOTE_COUNT = "vote_count";
    private static final String ADULT = "adult";
    private static final String VIDEO = "video";
    private static final String GENRES = "genres";
    private static final String PRODUCTION_COMPANIES = "production_companies";
    private static final String PRODUCTION_COUNTRIES = "production_countries";
    private static final String SPOKEN_LANGUAGES = "spoken_languages";
    private static final String GENRE_IDS = "genre_ids";
    private static final String NAME = "name";
    private static final String LOGO_PATH = "logo_path";
    private static final String ORIGIN_COUNTRY = "origin_country";
    private static final String ISO_3166_1 = "iso_3166_1";
    private static final String ISO_639_1 = "iso_639_1";

    private static final String AUTHOR = "author";
    private static final String CONTENT = "content";
    private static final String URL = "url";

    private static final String KEY = "key";
    private static final String SITE = "site";
    private static final String SIZE = "size";
    private static final String TYPE = "type";


    /* Constructor */
    private TheMoviesDbJsonUtils() {
    }

    /* Class Main Methods */

    public static List<Movie> extractMovieListFromJsonStr(final String jsonStr) throws JSONException {

        if (jsonStr.isEmpty())
            return null;

        final JSONObject jsonObj = new JSONObject(jsonStr);
        if (!jsonObj.has(RESULTS))
            return null;

        List<Movie> extractedMovieList = new ArrayList<>();
        final JSONArray resultsArray = jsonObj.getJSONArray(RESULTS);

        final int len = resultsArray.length();
        for (int i = 0; i < len; ++i) {
            final JSONObject resultItem = resultsArray.getJSONObject(i);

            final Integer id = resultItem.getInt(ID);
            final String title = resultItem.getString(TITLE);
            // final String originalTitle = resultItem.getString(ORIGINAL_TITLE);
            // final String originalLanguage = resultItem.getString(ORIGINAL_LANGUAGE);
            final String overview = resultItem.getString(OVERVIEW);
            final String releaseDate = resultItem.getString(RELEASE_DATE);
            final String posterPath = resultItem.getString(POSTER_PATH);
            final String backdropPath = resultItem.getString(BACKDROP_PATH);
            final Double popularity = resultItem.getDouble(POPULARITY);
            final Double voteAverage = resultItem.getDouble(VOTE_AVERAGE);
            // final Integer voteCount = resultItem.getInt(VOTE_COUNT);
            // final Boolean adult = resultItem.getBoolean(ADULT);
            // final Boolean video = resultItem.getBoolean(VIDEO);
            final JSONArray genreIdsJsonArray = resultItem.getJSONArray(GENRE_IDS);
            final int genreIdsLen = genreIdsJsonArray.length();

            List<Integer> genreIds = new ArrayList<>();
            for (int k = 0; k < genreIdsLen; ++k)
                genreIds.add(genreIdsJsonArray.getInt(k));

            extractedMovieList.add(new Movie(id, title, overview, releaseDate, posterPath, backdropPath,
                    popularity, voteAverage, genreIds));
        }

        return extractedMovieList;
    }

    public static MovieDetail extractMovieDetailFromJsonStr(final String jsonStr) throws JSONException {

        if (jsonStr.isEmpty())
            return null;

        final JSONObject jsonObj = new JSONObject(jsonStr);

        final Integer id = jsonObj.getInt(ID);
        final String imdbId = jsonObj.has(IMDB_ID) && !jsonObj.isNull(IMDB_ID) ? jsonObj.getString(IMDB_ID) : null;
        final String title = jsonObj.getString(TITLE);
        final String originalTitle = jsonObj.getString(ORIGINAL_TITLE);
        final String originalLanguage = jsonObj.getString(ORIGINAL_LANGUAGE);
        final String overview = jsonObj.getString(OVERVIEW);
        final String tagline = jsonObj.has(TAGLINE) && !jsonObj.isNull(TAGLINE) ? jsonObj.getString(TAGLINE) : null;
        final String status = jsonObj.getString(STATUS);
        final String releaseDate = jsonObj.getString(RELEASE_DATE);
        final Integer runtime = jsonObj.has(RUNTIME) && !jsonObj.isNull(RUNTIME) ? jsonObj.getInt(RUNTIME) : null;
        final String belongsToCollection = jsonObj.has(BELONGS_TO_COLLECTION) && !jsonObj.isNull(BELONGS_TO_COLLECTION) ?
                jsonObj.getString(BELONGS_TO_COLLECTION) : null;
        final String homepage = jsonObj.has(HOMEPAGE) && !jsonObj.isNull(HOMEPAGE) ? jsonObj.getString(HOMEPAGE) : null;
        final String posterPath = jsonObj.has(POSTER_PATH) && !jsonObj.isNull(POSTER_PATH) ? jsonObj.getString(POSTER_PATH) : null;
        final String backdropPath = jsonObj.has(BACKDROP_PATH) && !jsonObj.isNull(BACKDROP_PATH) ? jsonObj.getString(BACKDROP_PATH) : null;
        final Integer budget = jsonObj.getInt(BUDGET);
        final Integer revenue = jsonObj.getInt(REVENUE);
        final Double popularity = jsonObj.getDouble(POPULARITY);
        final Double voteAverage = jsonObj.getDouble(VOTE_AVERAGE);
        final Integer voteCount = jsonObj.getInt(VOTE_COUNT);
        final Boolean adult = jsonObj.getBoolean(ADULT);
        final Boolean video = jsonObj.getBoolean(VIDEO);

        final JSONArray genresJsonArray = jsonObj.getJSONArray(GENRES);
        final int genresJsonArrayLen = genresJsonArray.length();
        List<MovieDetail.Genre> genres = new ArrayList<>();
        for (int i = 0; i < genresJsonArrayLen; ++i) {
            final JSONObject genreItem = genresJsonArray.getJSONObject(i);
            final Integer genreId = genreItem.getInt(ID);
            final String genreName = genreItem.getString(NAME);
            genres.add(new MovieDetail.Genre(genreId, genreName));
        }

        final JSONArray productionCompaniesJsonArray = jsonObj.getJSONArray(PRODUCTION_COMPANIES);
        final int productionCompaniesJsonArrayLen = productionCompaniesJsonArray.length();
        List<MovieDetail.ProductionCompany> productionCompanies = new ArrayList<>();
        for (int i = 0; i < productionCompaniesJsonArrayLen; ++i) {
            final JSONObject productionCompanyItem = productionCompaniesJsonArray.getJSONObject(i);
            final Integer productionCompanyId = productionCompanyItem.getInt(ID);
            final String productionCompanyName = productionCompanyItem.getString(NAME);
            final String productionCompanyLogoPath = jsonObj.has(LOGO_PATH) && !jsonObj.isNull(LOGO_PATH) ?
                    jsonObj.getString(LOGO_PATH) : null;
            final String productionCompanyOriginCountry = productionCompanyItem.getString(ORIGIN_COUNTRY);
            productionCompanies.add(new MovieDetail.ProductionCompany(productionCompanyId, productionCompanyName,
                    productionCompanyLogoPath, productionCompanyOriginCountry));
        }

        final JSONArray productionCountriesJsonArray = jsonObj.getJSONArray(PRODUCTION_COUNTRIES);
        final int productionCountriesJsonArrayLen = productionCountriesJsonArray.length();
        List<MovieDetail.ProductionCountry> productionCountries = new ArrayList<>();
        for (int i = 0; i < productionCountriesJsonArrayLen; ++i) {
            final JSONObject productionCountryItem = productionCountriesJsonArray.getJSONObject(i);
            final String isoCode = productionCountryItem.getString(ISO_3166_1);
            final String productionCountryName = productionCountryItem.getString(NAME);
            productionCountries.add(new MovieDetail.ProductionCountry(isoCode, productionCountryName));
        }

        final JSONArray spokenLanguagesJsonArray = jsonObj.getJSONArray(SPOKEN_LANGUAGES);
        final int spokenLanguagesJsonArrayLen = spokenLanguagesJsonArray.length();
        List<MovieDetail.SpokenLanguage> spokenLanguages = new ArrayList<>();
        for (int i = 0; i < spokenLanguagesJsonArrayLen; ++i) {
            final JSONObject spokenLanguageItem = spokenLanguagesJsonArray.getJSONObject(i);
            final String isoCode = spokenLanguageItem.getString(ISO_639_1);
            final String spokenLanguageName = spokenLanguageItem.getString(NAME);
            spokenLanguages.add(new MovieDetail.SpokenLanguage(isoCode, spokenLanguageName));
        }

        return new MovieDetail(id, imdbId, title, originalTitle, originalLanguage, overview,
                tagline, status, releaseDate, runtime, belongsToCollection, homepage, posterPath,
                backdropPath, budget, revenue, popularity, voteAverage, voteCount, adult, video,
                genres, productionCompanies, productionCountries, spokenLanguages);
    }

    public static List<Review> extractReviewsListFromJsonStr(final String jsonStr) throws JSONException {

        if (jsonStr.isEmpty())
            return null;

        final JSONObject jsonObj = new JSONObject(jsonStr);
        if (!jsonObj.has(RESULTS))
            return null;

        List<Review> extractedReviewList = new ArrayList<>();
        final JSONArray resultsArray = jsonObj.getJSONArray(RESULTS);

        final int len = resultsArray.length();
        for (int i = 0; i < len; ++i) {
            final JSONObject resultItem = resultsArray.getJSONObject(i);

            final String id = resultItem.getString(ID);
            final String author = resultItem.getString(AUTHOR);
            final String content = resultItem.getString(CONTENT);
            final String url = resultItem.getString(URL);

            extractedReviewList.add(new Review(id, author, content, url));
        }

        return extractedReviewList;
    }

    public static List<Video> extractVideosListFromJsonStr(final String jsonStr) throws JSONException {

        if (jsonStr.isEmpty())
            return null;

        final JSONObject jsonObj = new JSONObject(jsonStr);
        if (!jsonObj.has(RESULTS))
            return null;

        List<Video> extractedVideoList = new ArrayList<>();
        final JSONArray resultsArray = jsonObj.getJSONArray(RESULTS);

        final int len = resultsArray.length();
        for (int i = 0; i < len; ++i) {
            final JSONObject resultItem = resultsArray.getJSONObject(i);

            final String id = resultItem.getString(ID);
            final String key = resultItem.getString(KEY);
            final String name = resultItem.getString(NAME);
            final String site = resultItem.getString(SITE);
            final Integer size = resultItem.getInt(SIZE);
            final String type = resultItem.getString(TYPE);

            extractedVideoList.add(new Video(id, key, name, site, size, type));
        }

        return extractedVideoList;
    }
}
