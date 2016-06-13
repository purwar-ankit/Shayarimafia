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

//MovieResopnse is CAtegories and movi is shayari
public interface ApiInterface {

    @GET("scategory")
    Call<List<Categories>> getAllCategories();

    @GET("all_shayari")
    Call<List<Shayari>> getAllShayaries();
    //result - www.shayarimafia.com/app/all_shayari

    @GET("Shayari/{id}")
    Call<Categories> getShayariById(@Path("id") int id/*, @Query("api_key") String apiKey*/);

    @GET("Shayari/{category_id}")
    Call<Categories> getShayariByCategoy(@Path("category_id") String categoryId/*, @Query("category_id") int categoty_id*/);
}
