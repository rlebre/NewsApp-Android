package com.ua.cm.project.unews.model;

import java.io.Serializable;

/**
 * Created by rui on 10/27/16.
 */

public class News implements Serializable {
    private String title;
    private String description;
    private String author;
    private String service;
    private String pub_date;
    private String category;
    private String link;
    private String shortDescription;
    private String imageLink;

    public News(String title, String shortDescription, String description, String category, String link) {
        this.title = title;
        this.description = description;
        this.category = category;
        this.link = link;
        this.shortDescription = shortDescription;
    }

    public News(String title, String shortDescription, String description, String author, String service, String pub_date, String category, String link) {
        this.title = title;
        this.description = description;
        this.author = author;
        this.service = service;
        this.pub_date = pub_date;
        this.category = category;
        this.link = link;
        this.shortDescription = shortDescription;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }

    public String getPub_date() {
        return pub_date;
    }

    public void setPub_date(String pub_date) {
        this.pub_date = pub_date;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public void setShortDescription(String short_description) {
        this.shortDescription = short_description;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    @Override
    public String toString() {
        return "News{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", author='" + author + '\'' +
                ", service='" + service + '\'' +
                ", pub_date='" + pub_date + '\'' +
                ", category='" + category + '\'' +
                ", link='" + link + '\'' +
                ", shortDescription='" + shortDescription + '\'' +
                ", imageLink='" + imageLink + '\'' +
                '}';
    }
}
