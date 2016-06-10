package rest;

import model.Categories;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by ankit.purwar on 6/10/2016.
 */


//MovieResopnse is CAtegories and movi is shayari
public interface ApiInterface {

    @GET("Shayari/top_rated")
    Call<Categories> getTopRatedShayaries(@Query("api_key") String apiKey);
    //result - http://api.themoviedb.org/3/shayari/top_rated?api_key=12345678910111213

    @GET("Shayari/{id}")
    Call<Categories> getShayariById(@Path("id") int id, @Query("api_key") String apiKey);

    @GET("Shayari/{category}")
    Call<Categories> getShayariByCategoy(@Path("category_title") String categoryTitle, @Query("category_id") int categoty_id);
}
