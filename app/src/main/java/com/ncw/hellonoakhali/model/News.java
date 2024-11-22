package com.ncw.hellonoakhali.model;


public class News {

    private int id;
    private String title;
    private String content;
    private String author;
    private String category;
    private String imageUrl;
    private String publishedAt;
    private String updatedAt;
    private String status;
    private String tags;
    private int views;
    private int isFeatured;

    // Constructor
    public News(int id, String title, String content, String author, String category,
                String imageUrl, String publishedAt, String updatedAt, String status,
                String tags, int views, int isFeatured) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.author = author;
        this.category = category;
        this.imageUrl = imageUrl;
        this.publishedAt = publishedAt;
        this.updatedAt = updatedAt;
        this.status = status;
        this.tags = tags;
        this.views = views;
        this.isFeatured = isFeatured;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public int getViews() {
        return views;
    }

    public void setViews(int views) {
        this.views = views;
    }

    public int isFeatured() {
        return isFeatured;
    }

    public void setFeatured(int featured) {
        isFeatured = featured;
    }
}
