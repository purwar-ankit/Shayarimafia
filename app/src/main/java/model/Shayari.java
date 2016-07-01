package model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by ankit.purwar on 6/8/2016.
 */

public class Shayari implements Parcelable {

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

    protected Shayari(Parcel in) {
        id = in.readInt();
        content = in.readString();
        title = in.readString();
    }

    public static final Creator<Shayari> CREATOR = new Creator<Shayari>() {
        @Override
        public Shayari createFromParcel(Parcel in) {
            return new Shayari(in);
        }

        @Override
        public Shayari[] newArray(int size) {
            return new Shayari[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(content);
        dest.writeInt(id);
    }
}
