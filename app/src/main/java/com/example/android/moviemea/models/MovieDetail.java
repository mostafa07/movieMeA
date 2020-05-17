package com.example.android.moviemea.models;

import java.util.List;


public class MovieDetail {

    private Integer id;
    private String imdbId;
    private String title;
    private String originalTitle;
    private String originalLanguage;
    private String overview;
    private String tagline;
    private String status;
    private String releaseDate;
    private Integer runtime;
    private String belongsToCollection;
    private String homepage;
    private String posterPath;
    private String backdropPath;
    private Integer budget;
    private Integer revenue;
    private Double popularity;
    private Double voteAverage;
    private Integer voteCount;
    private Boolean adult;
    private Boolean video;
    private List<Genre> genres;
    private List<ProductionCompany> productionCompanies;
    private List<ProductionCountry> productionCountries;
    private List<SpokenLanguage> spokenLanguages;


    /* Constructors */

    public MovieDetail() {
    }

    public MovieDetail(Integer id, String imdbId, String title, String originalTitle, String originalLanguage,
                       String overview, String tagline, String status, String releaseDate, Integer runtime,
                       String belongsToCollection, String homepage, String posterPath, String backdropPath,
                       Integer budget, Integer revenue, Double popularity, Double voteAverage,
                       Integer voteCount, Boolean adult, Boolean video, List<Genre> genres,
                       List<ProductionCompany> productionCompanies, List<ProductionCountry> productionCountries,
                       List<SpokenLanguage> spokenLanguages) {
        this.id = id;
        this.imdbId = imdbId;
        this.title = title;
        this.originalTitle = originalTitle;
        this.originalLanguage = originalLanguage;
        this.overview = overview;
        this.tagline = tagline;
        this.status = status;
        this.releaseDate = releaseDate;
        this.runtime = runtime;
        this.belongsToCollection = belongsToCollection;
        this.homepage = homepage;
        this.posterPath = posterPath;
        this.backdropPath = backdropPath;
        this.budget = budget;
        this.revenue = revenue;
        this.popularity = popularity;
        this.voteAverage = voteAverage;
        this.voteCount = voteCount;
        this.adult = adult;
        this.video = video;
        this.genres = genres;
        this.productionCompanies = productionCompanies;
        this.productionCountries = productionCountries;
        this.spokenLanguages = spokenLanguages;
    }


    /* Getters and Setters */

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getImdbId() {
        return imdbId;
    }

    public void setImdbId(String imdbId) {
        this.imdbId = imdbId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getTagline() {
        return tagline;
    }

    public void setTagline(String tagline) {
        this.tagline = tagline;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    public String getBelongsToCollection() {
        return belongsToCollection;
    }

    public void setBelongsToCollection(String belongsToCollection) {
        this.belongsToCollection = belongsToCollection;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public Integer getBudget() {
        return budget;
    }

    public void setBudget(Integer budget) {
        this.budget = budget;
    }

    public Integer getRevenue() {
        return revenue;
    }

    public void setRevenue(Integer revenue) {
        this.revenue = revenue;
    }

    public Double getPopularity() {
        return popularity;
    }

    public void setPopularity(Double popularity) {
        this.popularity = popularity;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public void setVoteAverage(Double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public Integer getVoteCount() {
        return voteCount;
    }

    public void setVoteCount(Integer voteCount) {
        this.voteCount = voteCount;
    }

    public Boolean getAdult() {
        return adult;
    }

    public void setAdult(Boolean adult) {
        this.adult = adult;
    }

    public Boolean getVideo() {
        return video;
    }

    public void setVideo(Boolean video) {
        this.video = video;
    }

    public List<Genre> getGenres() {
        return genres;
    }

    public void setGenres(List<Genre> genres) {
        this.genres = genres;
    }

    public List<ProductionCompany> getProductionCompanies() {
        return productionCompanies;
    }

    public void setProductionCompanies(List<ProductionCompany> productionCompanies) {
        this.productionCompanies = productionCompanies;
    }

    public List<ProductionCountry> getProductionCountries() {
        return productionCountries;
    }

    public void setProductionCountries(List<ProductionCountry> productionCountries) {
        this.productionCountries = productionCountries;
    }

    public List<SpokenLanguage> getSpokenLanguages() {
        return spokenLanguages;
    }

    public void setSpokenLanguages(List<SpokenLanguage> spokenLanguages) {
        this.spokenLanguages = spokenLanguages;
    }


    /* Other Methods */

    @Override
    public String toString() {
        return "MovieDetail{" +
                "id=" + id +
                ", imdbId='" + imdbId + '\'' +
                ", title='" + title + '\'' +
                ", originalTitle='" + originalTitle + '\'' +
                ", originalLanguage='" + originalLanguage + '\'' +
                ", overview='" + overview + '\'' +
                ", tagline='" + tagline + '\'' +
                ", status='" + status + '\'' +
                ", releaseDate='" + releaseDate + '\'' +
                ", runtime=" + runtime +
                ", belongsToCollection='" + belongsToCollection + '\'' +
                ", homepage='" + homepage + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", backdropPath='" + backdropPath + '\'' +
                ", budget=" + budget +
                ", revenue=" + revenue +
                ", popularity=" + popularity +
                ", voteAverage=" + voteAverage +
                ", voteCount=" + voteCount +
                ", adult=" + adult +
                ", video=" + video +
                ", genres=" + genres.toString() +
                ", productionCompanies=" + productionCompanies.toString() +
                ", productionCountries=" + productionCountries.toString() +
                ", spokenLanguages=" + spokenLanguages.toString() +
                '}';
    }


    /* Inner Classes */

    public static class Genre {

        private Integer id;
        private String name;

        public Genre() {
        }

        public Genre(Integer id, String name) {
            this.id = id;
            this.name = name;
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return "Genre{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class ProductionCompany {

        private Integer id;
        private String name;
        private String logoPath;
        private String originCountry;

        public ProductionCompany() {
        }

        public ProductionCompany(Integer id, String name, String logoPath, String originCountry) {
            this.id = id;
            this.name = name;
            this.logoPath = logoPath;
            this.originCountry = originCountry;
        }


        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogoPath() {
            return logoPath;
        }

        public void setLogoPath(String logoPath) {
            this.logoPath = logoPath;
        }

        public String getOriginCountry() {
            return originCountry;
        }

        public void setOriginCountry(String originCountry) {
            this.originCountry = originCountry;
        }


        @Override
        public String toString() {
            return "ProductionCompany{" +
                    "id=" + id +
                    ", name='" + name + '\'' +
                    ", logoPath='" + logoPath + '\'' +
                    ", originCountry='" + originCountry + '\'' +
                    '}';
        }
    }

    public static class ProductionCountry {

        private String isoCode;     // iso_3166_1
        private String name;

        public ProductionCountry() {
        }

        public ProductionCountry(String isoCode, String name) {
            this.isoCode = isoCode;
            this.name = name;
        }


        public String getIsoCode() {
            return isoCode;
        }

        public void setIsoCode(String isoCode) {
            this.isoCode = isoCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        @Override
        public String toString() {
            return "ProductionCountry{" +
                    "isoCode='" + isoCode + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }

    public static class SpokenLanguage {

        private String isoCode;     // iso_639_1
        private String name;

        public SpokenLanguage() {
        }

        public SpokenLanguage(String isoCode, String name) {
            this.isoCode = isoCode;
            this.name = name;
        }


        public String getIsoCode() {
            return isoCode;
        }

        public void setIsoCode(String isoCode) {
            this.isoCode = isoCode;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }


        @Override
        public String toString() {
            return "SpokenLanguage{" +
                    "isoCode='" + isoCode + '\'' +
                    ", name='" + name + '\'' +
                    '}';
        }
    }
}
