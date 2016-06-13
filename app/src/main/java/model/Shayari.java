package model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ankit.purwar on 6/8/2016.
 */

public class Shayari {

    @SerializedName("id")
    private int id;
    @SerializedName("content")
    private String content;
    @SerializedName("title")
    private String title;



    public Shayari() {
    }

    public Shayari(int id, String content, String title) {
        this.id = id;
        this.content = content;
        this.title = title;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
