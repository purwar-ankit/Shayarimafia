package mobinationapps.com.shayarimafia;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapter.CategoryAdapter;
import adapter.ShayariAdapter;
import model.Categories;
import model.Shayari;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static mobinationapps.com.shayarimafia.R.id.ivCatIcon;
import static mobinationapps.com.shayarimafia.R.id.textView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    RecyclerView rvCategoryView;
    // List<Categories> categoriesList;
    ArrayList<Shayari> shayariList;
    ArrayList<Categories> categoriesList;
    List<Shayari> shayariByCatList;
    CategoryAdapter categoryAdapter;
    ShayariAdapter shayariAdapter;
    ProgressDialog pDialog;
    ShayariFragment shayariFragment;
    FragmentManager fm;
    Fragment fragment = null;
    String catTitle;
    String catImgUrl;
    ImageView ivTransition;
    TextView tvTransition;


    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        categoriesList = new ArrayList<Categories>();
        shayariList = new ArrayList<Shayari>();

        rvCategoryView = (RecyclerView) findViewById(R.id.recycler_view);

        rvCategoryView.setHasFixedSize(true);
        rvCategoryView.setLayoutManager(new LinearLayoutManager(this));

      /*  rvCategoryView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        DividerItemDecoration decoration = new DividerItemDecoration(5);
        rvCategoryView.addItemDecoration(decoration);*/

        rvCategoryView.setHasFixedSize(true);
        rvCategoryView.setItemViewCacheSize(20);
        rvCategoryView.setDrawingCacheEnabled(true);
        rvCategoryView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        View view = rvCategoryView.getChildAt(0);
        if(view != null && rvCategoryView.getChildAdapterPosition(view) == 0)  {
            view.setTranslationY(-view.getTop() / 2);// or use view.animate().translateY();
        }


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

        final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);

        fm = getSupportFragmentManager();
//        shayariFragment = (ShayariFragment) fm.findFragmentById(R.id.container_body);

     /*   fragment = new CategoryFragment();

        replaceFrag(fragment);*/

        getCategories(apiService);


    }

    public static interface CategoryClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class CategoryRecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private CategoryClickListener categoryClickListener;

        public CategoryRecyclerTouchListener(final Context context, final RecyclerView recyclerView, final CategoryClickListener categoryClickListener) {
            this.categoryClickListener = categoryClickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && categoryClickListener != null) {
                        categoryClickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                    super.onLongPress(e);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && categoryClickListener != null && gestureDetector.onTouchEvent(e)) {
                categoryClickListener.onClick(child, rv.getChildLayoutPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }


    public void getCategories(final ApiInterface apiService) {
        pDialog = new ProgressDialog(this,R.style.MyTheme);
        pDialog.setCancelable(false);
       // pDialog.setMessage("Loading...");
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.show();
        Call<List<Categories>> call = apiService.getAllCategories();
        call.enqueue(new Callback<List<Categories>>() {
            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
                Toast.makeText(MainActivity.this, "inside onResponse", Toast.LENGTH_SHORT).show();
                for (int j = 0; j < response.body().size(); j++) {
                    Categories categories = new Categories();
                    categories.setCategory_id(response.body().get(j).getCategory_id());
                    categories.setCategory_count(response.body().get(j).getCategory_count());
                    categories.setCategory_title(response.body().get(j).getCategory_title());
                    categories.setImg_url(response.body().get(j).getImg_url());
                    categoriesList.add(categories);
                }
                for (int i = 0; i < categoriesList.size(); i++) {
                    Log.d("ankitTAGreversed", "title : " + categoriesList.get(i).getCategory_title());
                }

                fragment = new CategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("categoryArrayList", categoriesList);
                fragment.setArguments(bundle);
                //  pDialog.hide();
                //replaceFrag(fragment);
                categoryAdapter = new CategoryAdapter(categoriesList, MainActivity.this);
                rvCategoryView.setAdapter(categoryAdapter);
                pDialog.hide();
                rvCategoryView.addOnItemTouchListener(new CategoryRecyclerTouchListener(MainActivity.this, rvCategoryView,
                        new CategoryClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Toast.makeText(MainActivity.this, "calling getShayaris()", Toast.LENGTH_SHORT).show();
                                catTitle = categoriesList.get(position).getCategory_title();
                                catImgUrl = categoriesList.get(position).getImg_url();
                                getShayariByCatId(apiService, categoriesList.get(position).getCategory_id(), position);

                                ivTransition = (ImageView)view.findViewById(R.id.ivCatIcon);
                                tvTransition = (TextView)view.findViewById(R.id.tvCatName);
                            }

                            @Override
                            public void onLongClick(View view, int position) {
                            }
                        }));
            }

            @Override
            public void onFailure(Call<List<Categories>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "inside onFailure", Toast.LENGTH_SHORT).show();
                Log.e("ankitTAG", t.toString());
                pDialog.hide();
            }
        });
    }

    public void getShayaries(ApiInterface apiService) {
        Toast.makeText(MainActivity.this, "CALLED getShayaris()", Toast.LENGTH_SHORT).show();
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(true);
        pDialog.setMessage("Loading...");
        pDialog.show();
        Call<List<Shayari>> call = apiService.getAllShayaries();
        call.enqueue(new Callback<List<Shayari>>() {
            @Override
            public void onResponse(Call<List<Shayari>> call, Response<List<Shayari>> response) {
                //List<Shayari> shayariList = response.body();
                for (int j = 0; j < response.body().size(); j++) {
                    Shayari shayari = new Shayari();
                    shayari.setContent(response.body().get(j).getContent());
                    shayari.setId(response.body().get(j).getId());
                    shayari.setTitle(response.body().get(j).getTitle());
                    //  Log.d("ankitTAG", "size : " + categories.getCategory_title());
                    shayariList.add(shayari);
                }
                for (int i = 0; i < shayariList.size(); i++) {
                    Log.d("ankitTAGreversed", "title : " + shayariList.get(i).getTitle());
                }
                shayariAdapter = new ShayariAdapter(shayariList, MainActivity.this);
                rvCategoryView.setAdapter(shayariAdapter);
                pDialog.hide();
            }

            @Override
            public void onFailure(Call<List<Shayari>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "inside onFailure", Toast.LENGTH_SHORT).show();
                Log.e("ankitTAG", t.toString());
                pDialog.hide();
            }
        });

    }

    public void getShayariById(ApiInterface apiService, int shayariID) {

        Log.d("ankitTAG", "getShayariById");

        Call<Shayari> call = apiService.getShayariById(shayariID);
        call.enqueue(new Callback<Shayari>() {
            @Override
            public void onResponse(Call<Shayari> call, Response<Shayari> response) {
                Log.d("ankitTAG", " title:" + response.body().getTitle() + " id:" + response.body().getId()
                        + " content :" + response.body().getContent());
            }

            @Override
            public void onFailure(Call<Shayari> call, Throwable t) {
                Toast.makeText(MainActivity.this, "inside onFailure", Toast.LENGTH_SHORT).show();
                Log.e("ankitTAG", t.toString());
            }
        });
    }

    public void getShayariByCatId(ApiInterface apiService, int catID, final int position) {

        Log.d("ankitTAG", "getShayariById");
        //pDialog = new ProgressDialog(this);
       // pDialog.setCancelable(true);
        //pDialog.setMessage("Loading...");

        pDialog = new ProgressDialog(this,R.style.MyTheme);
        pDialog.setCancelable(false);
        // pDialog.setMessage("Loading...");
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

        pDialog.show();
        Call<List<Shayari>> call = apiService.getShayariByCategoy(catID);
        call.enqueue(new Callback<List<Shayari>>() {

            @Override
            public void onResponse(Call<List<Shayari>> call, Response<List<Shayari>> response) {
                for (int j = 0; j < response.body().size(); j++) {
                    Shayari shayari = new Shayari();
                    shayari.setContent(response.body().get(j).getContent());
                    shayari.setId(response.body().get(j).getId());
                    shayari.setTitle(response.body().get(j).getTitle());
                    shayariList.add(shayari);
                }
                for (int i = 0; i < shayariList.size(); i++) {
                    Log.d("ankitTAGreversed", "title : " + shayariList.get(i).getTitle());
                }

                Bitmap bitmap = ((BitmapDrawable)ivTransition.getDrawable()).getBitmap();
                shayariFragment = new ShayariFragment();
                Bundle bundle = new Bundle();
                bundle.putString("catTitle", catTitle);
                bundle.putString("catImgUrl", catImgUrl);
                bundle.putInt("catTransitionNamePos", position);
                bundle.putParcelable("bitmap", bitmap);
                bundle.putParcelableArrayList("shayariArrayList", shayariList);

                //bundle.putString("categoryName", categoriesList.get();
                shayariFragment.setArguments(bundle);
                pDialog.hide();
                replaceFrag(shayariFragment, position);
            }

            @Override
            public void onFailure(Call<List<Shayari>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "inside onFailure", Toast.LENGTH_SHORT).show();
                Log.e("ankitTAG", t.toString());
                pDialog.hide();
            }
        });
    }

    private void replaceFrag(Fragment fragment, int position) {
        if (fragment != null) {
            // FragmentManager fragmentManager = getSupportFragmentManager();
           /* FragmentTransaction fragmentTransaction = fm.beginTransaction();
            fragmentTransaction.addSharedElement(tvTransition , getString(R.string.category_title));
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();*/

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Inflate transitions to apply

                Toast.makeText(this, "inside if", Toast.LENGTH_SHORT).show();

                Transition changeTransform = TransitionInflater.from(this).
                        inflateTransition(R.transition.change_image_transform);
                Transition explodeTransform = TransitionInflater.from(this).
                        inflateTransition(android.R.transition.explode);

                // Setup exit transition on first fragment
                fragment.setSharedElementReturnTransition(changeTransform);
                fragment.setExitTransition(explodeTransform);

                // Add second fragment by replacing first
                tvTransition.setTransitionName(getString(R.string.category_title)+position);
                ivTransition.setTransitionName(getString(R.string.category_img)+position);



                Pair<View, String> pair1 = Pair.create((View)tvTransition, getString(R.string.category_title)+position);
                Pair<View, String> pair2 = Pair.create((View)ivTransition, getString(R.string.category_img)+position);

                Log.d("transitionNameSent", getString(R.string.category_title)+position + "tvTransition.getTransitionName(): " +tvTransition.getTransitionName());
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addSharedElement(tvTransition , getString(R.string.category_title)+position);
                fragmentTransaction.addSharedElement(ivTransition,getString(R.string.category_img)+position);
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack("transaction");
                fragmentTransaction.commit();
            }
            else {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
               // fragmentTransaction.addSharedElement(tvTransition , getString(R.string.category_title));
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }






            if(!shayariList.isEmpty() || !shayariList.equals(null))
            {
                shayariList = new ArrayList<Shayari>();
            }

        }
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
