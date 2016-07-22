package rest;

import java.util.List;

import model.Categories;
import model.Shayari;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ankit.purwar on 6/10/2016.
 */


/*
Categories:
http://www.shayarimafia.com/app/scategory.php

Shayari by category ID:
http://www.shayarimafia.com/app/shayari_by_category.php?cat_id=5

Single shayari by shayari id:
http://www.shayarimafia.com/app/shayari.php?sid=1272


All Shayari:
http://www.shayarimafia.com/app/all_shayari.php
*/

public interface ApiInterface {
    @GET("scategory")
    Call<List<Categories>> getAllCategories();

    @GET("all_shayari")
    Call<List<Shayari>> getAllShayaries();

    @GET("shayari.php")
    Call<Shayari> getShayariById(@Query("sid") int id);

/*     "/api/42/getDummieContent?test=test"
    @GET("/api/{id}/getDummieContent")
    public DummieContent getDummieContent(@Path("id") Integer id, @Query("test") String strTest);*/

    @GET("shayari_by_category.php")
    Call<List<Shayari>> getShayariByCategoy(@Query("cat_id") int categoryId);
}
