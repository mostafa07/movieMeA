package com.example.android.moviemea.utilities;

import java.util.ArrayList;
import java.util.List;

import com.example.android.moviemea.models.Movie;
import com.example.android.moviemea.models.MovieDetail;

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


    /* Class Main Methods */

    public static List<Movie> extractMovieListFromJsonStr(String jsonStr) throws JSONException {

        if (jsonStr.isEmpty())
            return null;

        JSONObject jsonObj = new JSONObject(jsonStr);
        if (!jsonObj.has(RESULTS))
            return null;

        ArrayList<Movie> extractedMovieList = new ArrayList<>();
        JSONArray resultsArray = jsonObj.getJSONArray(RESULTS);

        int len = resultsArray.length();
        for (int i = 0; i < len; ++i) {
            JSONObject resultItem = resultsArray.getJSONObject(i);

            Integer id = resultItem.getInt(ID);
            String title = resultItem.getString(TITLE);
            // String originalTitle = resultItem.getString(ORIGINAL_TITLE);
            // String originalLanguage = resultItem.getString(ORIGINAL_LANGUAGE);
            String overview = resultItem.getString(OVERVIEW);
            String releaseDate = resultItem.getString(RELEASE_DATE);
            String posterPath = resultItem.getString(POSTER_PATH);
            String backdropPath = resultItem.getString(BACKDROP_PATH);
            Double popularity = resultItem.getDouble(POPULARITY);
            Double voteAverage = resultItem.getDouble(VOTE_AVERAGE);
            // Integer voteCount = resultItem.getInt(VOTE_COUNT);
            // Boolean adult = resultItem.getBoolean(ADULT);
            // Boolean video = resultItem.getBoolean(VIDEO);
            JSONArray genreIdsJsonArray = resultItem.getJSONArray(GENRE_IDS);

            ArrayList<Integer> genreIds = new ArrayList<>();
            for (int k = 0; k < genreIdsJsonArray.length(); ++k)
                genreIds.add(genreIdsJsonArray.getInt(k));

            extractedMovieList.add(constructMovie(id, title, overview, releaseDate, posterPath,
                    backdropPath, popularity, voteAverage, genreIds));
        }

        return extractedMovieList;
    }

    public static MovieDetail extractMovieDetailFromJsonStr(String jsonStr) throws JSONException {

        if (jsonStr.isEmpty())
            return null;

        JSONObject jsonObj = new JSONObject(jsonStr);

        Integer id = jsonObj.getInt(ID);
        String imdbId = jsonObj.getString(IMDB_ID);
        String title = jsonObj.getString(TITLE);
        String originalTitle = jsonObj.getString(ORIGINAL_TITLE);
        String originalLanguage = jsonObj.getString(ORIGINAL_LANGUAGE);
        String overview = jsonObj.getString(OVERVIEW);
        String tagline = jsonObj.getString(TAGLINE);
        String status = jsonObj.getString(STATUS);
        String releaseDate = jsonObj.getString(RELEASE_DATE);
        Integer runtime = jsonObj.getInt(RUNTIME);
        String belongsToCollection = jsonObj.getString(BELONGS_TO_COLLECTION);
        String homepage = jsonObj.getString(HOMEPAGE);
        String posterPath = jsonObj.getString(POSTER_PATH);
        String backdropPath = jsonObj.getString(BACKDROP_PATH);
        Integer budget = jsonObj.getInt(BUDGET);
        Integer revenue = jsonObj.getInt(REVENUE);
        Double popularity = jsonObj.getDouble(POPULARITY);
        Double voteAverage = jsonObj.getDouble(VOTE_AVERAGE);
        Integer voteCount = jsonObj.getInt(VOTE_COUNT);
        Boolean adult = jsonObj.getBoolean(ADULT);
        Boolean video = jsonObj.getBoolean(VIDEO);

        JSONArray genresJsonArray = jsonObj.getJSONArray(GENRES);
        ArrayList<MovieDetail.Genre> genres = new ArrayList<>();
        for (int i = 0; i < genresJsonArray.length(); ++i) {
            JSONObject genreItem = genresJsonArray.getJSONObject(i);
            Integer genreId = genreItem.getInt(ID);
            String genreName = genreItem.getString(NAME);
            genres.add(new MovieDetail.Genre(genreId, genreName));
        }

        JSONArray productionCompaniesJsonArray = jsonObj.getJSONArray(PRODUCTION_COMPANIES);
        ArrayList<MovieDetail.ProductionCompany> productionCompanies = new ArrayList<>();
        for (int i = 0; i < productionCompaniesJsonArray.length(); ++i) {
            JSONObject productionCompanyItem = productionCompaniesJsonArray.getJSONObject(i);
            Integer productionCompanyId = productionCompanyItem.getInt(ID);
            String productionCompanyName = productionCompanyItem.getString(NAME);
            String productionCompanyLogoPath = productionCompanyItem.getString(LOGO_PATH);
            String productionCompanyOriginCountry = productionCompanyItem.getString(ORIGIN_COUNTRY);
            productionCompanies.add(new MovieDetail.ProductionCompany(productionCompanyId, productionCompanyName,
                    productionCompanyLogoPath, productionCompanyOriginCountry));
        }

        JSONArray productionCountriesJsonArray = jsonObj.getJSONArray(PRODUCTION_COUNTRIES);
        ArrayList<MovieDetail.ProductionCountry> productionCountries = new ArrayList<>();
        for (int i = 0; i < productionCountriesJsonArray.length(); ++i) {
            JSONObject productionCountryItem = productionCountriesJsonArray.getJSONObject(i);
            String isoCode = productionCountryItem.getString(ISO_3166_1);
            String productionCountryName = productionCountryItem.getString(NAME);
            productionCountries.add(new MovieDetail.ProductionCountry(isoCode, productionCountryName));
        }

        JSONArray spokenLanguagesJsonArray = jsonObj.getJSONArray(SPOKEN_LANGUAGES);
        ArrayList<MovieDetail.SpokenLanguage> spokenLanguages = new ArrayList<>();
        for (int i = 0; i < spokenLanguagesJsonArray.length(); ++i) {
            JSONObject spokenLanguageItem = spokenLanguagesJsonArray.getJSONObject(i);
            String isoCode = spokenLanguageItem.getString(ISO_639_1);
            String spokenLanguageName = spokenLanguageItem.getString(NAME);
            spokenLanguages.add(new MovieDetail.SpokenLanguage(isoCode, spokenLanguageName));
        }

        MovieDetail movieDetail = constructMovieDetail(id, imdbId, title, originalTitle, originalLanguage,
                overview, tagline, status, releaseDate, runtime, belongsToCollection, homepage, posterPath,
                backdropPath, budget, revenue, popularity, voteAverage, voteCount, adult, video,
                genres, productionCompanies, productionCountries, spokenLanguages);

        return movieDetail;
    }


    /* Helper Methods */

    private static Movie constructMovie(Integer id, String title, String overview, String releaseDate, String posterPath,
                                        String backdropPath, Double popularity, Double voteAverage, ArrayList<Integer> genreIds) {

        Movie movie = new Movie();
        movie.setId(id);
        movie.setTitle(title);
        movie.setOverview(overview);
        movie.setReleaseDate(releaseDate);
        movie.setPosterPath(posterPath);
        movie.setBackdropPath(backdropPath);
        movie.setPopularity(popularity);
        movie.setVoteAverage(voteAverage);
        movie.setGenreIds(genreIds);

        return movie;
    }

    private static MovieDetail constructMovieDetail(Integer id, String imdbId, String title, String originalTitle,
                                                    String originalLanguage, String overview, String tagline,
                                                    String status, String releaseDate, Integer runtime,
                                                    String belongsToCollection, String homepage, String posterPath,
                                                    String backdropPath, Integer budget, Integer revenue,
                                                    Double popularity, Double voteAverage, Integer voteCount,
                                                    Boolean adult, Boolean video, ArrayList<MovieDetail.Genre> genres,
                                                    ArrayList<MovieDetail.ProductionCompany> productionCompanies,
                                                    ArrayList<MovieDetail.ProductionCountry> productionCountries,
                                                    ArrayList<MovieDetail.SpokenLanguage> spokenLanguages) {

        MovieDetail movieDetail = new MovieDetail();

        movieDetail.setId(id);
        movieDetail.setImdbId(imdbId);
        movieDetail.setTitle(title);
        movieDetail.setOriginalTitle(originalTitle);
        movieDetail.setOriginalLanguage(originalLanguage);
        movieDetail.setOverview(overview);
        movieDetail.setTagline(tagline);
        movieDetail.setStatus(status);
        movieDetail.setReleaseDate(releaseDate);
        movieDetail.setRuntime(runtime);
        movieDetail.setBelongsToCollection(belongsToCollection);
        movieDetail.setHomepage(homepage);
        movieDetail.setPosterPath(posterPath);
        movieDetail.setBackdropPath(backdropPath);
        movieDetail.setBudget(budget);
        movieDetail.setRevenue(revenue);
        movieDetail.setPopularity(popularity);
        movieDetail.setVoteAverage(voteAverage);
        movieDetail.setVoteCount(voteCount);
        movieDetail.setAdult(adult);
        movieDetail.setVideo(video);
        movieDetail.setGenres(genres);
        movieDetail.setProductionCompanies(productionCompanies);
        movieDetail.setProductionCountries(productionCountries);
        movieDetail.setSpokenLanguages(spokenLanguages);

        return movieDetail;
    }
}