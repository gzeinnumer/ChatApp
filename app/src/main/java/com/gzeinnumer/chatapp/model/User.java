package com.gzeinnumer.chatapp.model;

//todo 30
public class User {
    private String id;
    private String username;
    private String imageURL;
    //todo 79 part 12 start
    //tambah 1 variable, dan perbaiki constructor dan getter setternya
    private String status;
    //todo 101
    //tambah 1 variable, dan perbaiki constructor dan getter setternya
    private String search;

    public User(){

    }

    public User(String id, String username, String imageURL, String status, String search) {
        this.id = id;
        this.username = username;
        this.imageURL = imageURL;
        this.status = status;
        this.search = search;
    }

    public String getSearch() {
        return search;
    }

    public void setSearch(String search) {
        this.search = search;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
