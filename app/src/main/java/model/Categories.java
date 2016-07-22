package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ankit.purwar on 6/8/2016.
 */

public class Categories implements Parcelable {

    @SerializedName("category_title")
    private String category_title;
    @SerializedName("category_id")
    private int category_id;
    @SerializedName("category_count")
    private int category_count;
    @SerializedName("img_url")
    private String img_url;

    //private List<Shayari> shayariResult ;

    public Categories() {
    }

    public Categories(String category_title, int category_id, int category_count, String img_url) {
        this.category_title = category_title;
        this.category_id = category_id;
        this.category_count = category_count;
        this.img_url = img_url;
    }

    protected Categories(Parcel in) {
        category_title = in.readString();
        category_id = in.readInt();
        category_count = in.readInt();
        img_url = in.readString();
    }

    public static final Creator<Categories> CREATOR = new Creator<Categories>() {
        @Override
        public Categories createFromParcel(Parcel in) {
            return new Categories(in);
        }

        @Override
        public Categories[] newArray(int size) {
            return new Categories[size];
        }
    };

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

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(category_title);
        dest.writeInt(category_count);
        dest.writeInt(category_id);
        dest.writeString(img_url);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Categories)) {
            return false;
        }
        Categories other = (Categories) o;
        return category_title.equals(other.category_title) && img_url.equals(other.img_url) && category_count == other.category_count && category_id == other.category_id;
    }

    public int hashCode() {
        return category_title.hashCode();
    }
}
