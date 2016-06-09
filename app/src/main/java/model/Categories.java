package model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ankit.purwar on 6/8/2016.
 */

public class Categories {

    @SerializedName("category_title")
    private String category_title;
    @SerializedName("category_id")
    private int category_id;
    @SerializedName("category_count")
    private int category_count;

    public Categories() {
    }

    public Categories(String category_title, int category_id, int category_count) {
        this.category_title = category_title;
        this.category_id = category_id;
        this.category_count = category_count;
    }

    public String getCategory_title() {
        return category_title;
    }

    public void setCategory_title(String category_title) {
        this.category_title = category_title;
    }

    public int getCategory_id() {
        return category_id;
    }

    public void setCategory_id(int category_id) {
        this.category_id = category_id;
    }

    public int getCategory_count() {
        return category_count;
    }

    public void setCategory_count(int category_count) {
        this.category_count = category_count;
    }
}
