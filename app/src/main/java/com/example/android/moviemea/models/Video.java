package com.example.android.moviemea.models;

public class Video {

    private String id;
    // private String iso_639_1;
    // private String iso_3166_1;
    private String key;
    private String name;
    private String site;
    private Integer size;
    private String type;


    /* Constructors */

    public Video() {
    }

    public Video(String id, String key, String name, String site, Integer size, String type) {
        this.id = id;
        this.key = key;
        this.name = name;
        this.site = site;
        this.size = size;
        this.type = type;
    }


    /* Getters and Setters */

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSite() {
        return site;
    }

    public void setSite(String site) {
        this.site = site;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    /* Other Methods */

    @Override
    public String toString() {
        return "Video{" +
                "id='" + id + '\'' +
                ", key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", site='" + site + '\'' +
                ", size=" + size +
                ", type='" + type + '\'' +
                '}';
    }
}
