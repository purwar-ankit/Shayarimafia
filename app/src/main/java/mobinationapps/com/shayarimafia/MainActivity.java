package mobinationapps.com.shayarimafia;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import model.Categories;
import model.Shayari;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    private final static String API_KEY = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        //gets all the categories from web service
        getCategories(apiService);
        //gets all the shayaris from webservice
        getShayaries(apiService);

    }

    public void getCategories(ApiInterface apiService) {
        Call<List<Categories>> call = apiService.getAllCategories();
        call.enqueue(new Callback<List<Categories>>() {

            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
                Toast.makeText(MainActivity.this, "inside onResponse", Toast.LENGTH_SHORT).show();
                Categories categories = new Categories();
                List<Categories> categoriesList = response.body();

                for (int j = 0; j < categoriesList.size(); j++) {
                    Log.d("ankitTAG", " title:" + categoriesList.get(j).getCategory_title() + " id:" + categoriesList.get(j).getCategory_id()
                            + " count :" + categoriesList.get(j).getCategory_count());
                }

                Log.d("ankitTAG", "size : " + response.body().size());
                Log.d("ankitTAG", "values : " + response.body());
            }

            @Override
            public void onFailure(Call<List<Categories>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "inside onFailure", Toast.LENGTH_SHORT).show();
                Log.e("ankitTAG", t.toString());
            }
        });
    }

    public void getShayaries(ApiInterface apiService) {
        Call<List<Shayari>> call = apiService.getAllShayaries();
        call.enqueue(new Callback<List<Shayari>>() {
            @Override
            public void onResponse(Call<List<Shayari>> call, Response<List<Shayari>> response) {
                List<Shayari> shayariList = response.body();
                for (int i=0;i< shayariList.size(); i++){
                    Log.d("ankitTAG", " title:" + shayariList.get(i).getTitle()+ " id:" + shayariList.get(i).getId()
                            + " content :" + shayariList.get(i).getContent());
                }
            }

            @Override
            public void onFailure(Call<List<Shayari>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "inside onFailure", Toast.LENGTH_SHORT).show();
                Log.e("ankitTAG", t.toString());
            }
        });

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
