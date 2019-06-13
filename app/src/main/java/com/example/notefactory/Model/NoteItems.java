package com.example.notefactory.Model;

public class NoteItems {
    public String title,body,image_path,timestamp;
    public int id;

    public NoteItems(int id,String title, String body, String image_path,String timestamp) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.image_path = image_path;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getImage_path() {
        return image_path;
    }

    public void setImage_path(String image_path) {
        this.image_path = image_path;
    }
}
