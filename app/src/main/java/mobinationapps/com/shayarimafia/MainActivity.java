package mobinationapps.com.shayarimafia;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.util.Pair;
import android.support.v7.app.AlertDialog;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

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
import utils.SharedPreference;
import utils.Utility;

import static mobinationapps.com.shayarimafia.R.id.ivCatIcon;
import static mobinationapps.com.shayarimafia.R.id.textView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    CoordinatorLayout mainCoordLay;
    RecyclerView rvCategoryView;
    // List<Categories> categoriesList;
    ArrayList<Shayari> shayariList;
    ArrayList<Categories> categoriesList;
    List<Shayari> shayariByCatList;
    CategoryAdapter categoryAdapter;
    ShayariAdapter shayariAdapter;
    //ProgressDialog pDialog;
    ShayariFragment shayariFragment;
    FragmentManager fm;
    Fragment fragment = null;
    String catTitle;
    String catImgUrl;
    ImageView ivTransition;
    TextView tvTransition;
    boolean isNetAvailable;
    ApiInterface apiService;
    static FloatingActionButton fab, fab1, fab2, fab3;
    public static boolean FAB_Status = false;

    //Animations
    static Animation show_fab_1, hide_fab_1, show_fab_2, hide_fab_2, show_fab_3, hide_fab_3;


    private static final String TAG = MainActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mainCoordLay = (CoordinatorLayout) findViewById(R.id.mainCoordLay);

        fab1 = (FloatingActionButton) findViewById(R.id.fab_1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab_3);

        show_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab2_hide);
        show_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_show);
        hide_fab_3 = AnimationUtils.loadAnimation(getApplication(), R.anim.fab3_hide);

        isNetAvailable = Utility.checkNetworkConnectivity(MainActivity.this);
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
        if (view != null && rvCategoryView.getChildAdapterPosition(view) == 0) {
            view.setTranslationY(-view.getTop() / 2);// or use view.animate().translateY();
        }


        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View view) {
                int sdk = android.os.Build.VERSION.SDK_INT;
                Bitmap overlyBitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(),
                        R.drawable.overlay_home);
                if (FAB_Status == false) {
                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        mainCoordLay.setBackgroundDrawable(new BitmapDrawable(getResources(), overlyBitmap));
                    } else {
                        mainCoordLay.setBackground(new BitmapDrawable(getResources(), overlyBitmap));
                    }

                    //Display FAB menu
                    expandFAB();
                    FAB_Status = true;
                } else {
                    //Close FAB menu
                    hideFAB();
                    FAB_Status = false;
                }
            }
        });

        fab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Floating Action Button 1 Rate us", Toast.LENGTH_SHORT).show();
                Utility.rateUs(MainActivity.this);
            }
        });

        fab2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Floating Action Button 2", Toast.LENGTH_SHORT).show();
            }
        });

        fab3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication(), "Floating Action Button 3", Toast.LENGTH_SHORT).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        apiService = ApiClient.getClient().create(ApiInterface.class);

        fm = getSupportFragmentManager();
        // shayariFragment = (ShayariFragment) fm.findFragmentById(R.id.container_body);
         /*  fragment = new CategoryFragment();
        replaceFrag(fragment);*/
        if (isNetAvailable) {
            //  getCategories(apiService);
        } else {
            Toast.makeText(this, "no internet connectivity found", Toast.LENGTH_SHORT).show();

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setMessage("No internet connectivity found, Check settings?");

            alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface arg0, int arg1) {
                    Intent settingsIntent = new Intent(Settings.ACTION_WIRELESS_SETTINGS);
                    MainActivity.this.startActivityForResult(settingsIntent, 1);
                }
            });

            alertDialogBuilder.setNegativeButton("No,Exit app", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                  /*  SharedPreference sharedPreference = new SharedPreference();
                    ArrayList<Shayari> favorites = sharedPreference.getFavorites(MainActivity.this);
                    if(!favorites.isEmpty() || !favorites.equals(null)){
                        for (int i=0;i<favorites.size();i++)
                        {
                           // Toast.makeText(MainActivity.this, favorites.get(i).getTitle(),Toast.LENGTH_SHORT).show();
                        }
                    }
                    shayariFragment = new ShayariFragment();
                    Bundle bundle = new Bundle();
                    bundle.putString("catTitle", catTitle);
                    bundle.putString("catImgUrl", catImgUrl);

                    bundle.putParcelableArrayList("shayariArrayList", favorites);

                    shayariFragment.setArguments(bundle);
                    pDialog.hide();
                    replaceFrag(shayariFragment, 1*//*position*//*);
*/
                    finish();
                }
            });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            //on go to settings button click
        }

        rvCategoryView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (FAB_Status) {
                    hideFAB();
                    FAB_Status = false;
                }
                return false;
            }
        });
        CategoryFragment categoryFragment = new CategoryFragment();

        Utility.replaceFrag(MainActivity.this,categoryFragment,0,fm,null);

    }

    private void expandFAB() {

        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(show_fab_1);
        fab1.setClickable(true);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin += (int) (fab2.getWidth() * 1.5);
        layoutParams2.bottomMargin += (int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(show_fab_2);
        fab2.setClickable(true);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        layoutParams3.rightMargin += (int) (fab3.getWidth() * 0.25);
        layoutParams3.bottomMargin += (int) (fab3.getHeight() * 1.7);
        fab3.setLayoutParams(layoutParams3);
        fab3.startAnimation(show_fab_3);
        fab3.setClickable(true);
    }

    public static void hideFAB() {
        //Floating Action Button 1
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
        layoutParams.rightMargin -= (int) (fab1.getWidth() * 1.7);
        layoutParams.bottomMargin -= (int) (fab1.getHeight() * 0.25);
        fab1.setLayoutParams(layoutParams);
        fab1.startAnimation(hide_fab_1);
        fab1.setClickable(false);

        //Floating Action Button 2
        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
        layoutParams2.rightMargin -= (int) (fab2.getWidth() * 1.5);
        layoutParams2.bottomMargin -= (int) (fab2.getHeight() * 1.5);
        fab2.setLayoutParams(layoutParams2);
        fab2.startAnimation(hide_fab_2);
        fab2.setClickable(false);

        //Floating Action Button 3
        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
        layoutParams3.rightMargin -= (int) (fab3.getWidth() * 0.25);
        layoutParams3.bottomMargin -= (int) (fab3.getHeight() * 1.7);
        fab3.setLayoutParams(layoutParams3);
        fab3.startAnimation(hide_fab_3);
        fab3.setClickable(false);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //getCategories(apiService);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Toast.makeText(this, "result code: " + resultCode, Toast.LENGTH_SHORT).show();

        if (resultCode == 1) {
            switch (requestCode) {
                case 1:
          //          getCategories(apiService);
                    break;
            }
        }
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


   /* public void getCategories(final ApiInterface apiService) {
        pDialog = new ProgressDialog(this, R.style.MyTheme);

        ProgressBar spinner = new android.widget.ProgressBar(
                this,
                null,
                android.R.attr.progressBarStyle);

        spinner.getIndeterminateDrawable().setColorFilter(0xFFFF0000, android.graphics.PorterDuff.Mode.MULTIPLY);


        pDialog.setCancelable(true);
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

                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                // fragmentTransaction.addSharedElement(tvTransition , getString(R.string.category_title));
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

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

                                ivTransition = (ImageView) view.findViewById(R.id.ivCatIcon);
                                tvTransition = (TextView) view.findViewById(R.id.tvCatName);
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
    }*/

    public void getShayariByCatId(ApiInterface apiService, int catID, final int position) {

        Log.d("ankitTAG", "getShayariById");
        //pDialog = new ProgressDialog(this);
        // pDialog.setCancelable(true);
        //pDialog.setMessage("Loading...");

      //  pDialog = new ProgressDialog(this, R.style.MyTheme);
      //  pDialog.setCancelable(true);
        // pDialog.setMessage("Loading...");
     //   pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);

     //   pDialog.show();
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

                Bitmap bitmap = ((BitmapDrawable) ivTransition.getDrawable()).getBitmap();
                shayariFragment = new ShayariFragment();
                Bundle bundle = new Bundle();
                bundle.putString("catTitle", catTitle);
                bundle.putString("catImgUrl", catImgUrl);
                bundle.putInt("catTransitionNamePos", position);
                bundle.putParcelable("bitmap", bitmap);
                bundle.putParcelableArrayList("shayariArrayList", shayariList);

                //bundle.putString("categoryName", categoriesList.get();
                shayariFragment.setArguments(bundle);
            //    pDialog.hide();
               // replaceFrag(shayariFragment, position);
            }

            @Override
            public void onFailure(Call<List<Shayari>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "inside onFailure", Toast.LENGTH_SHORT).show();
                Log.e("ankitTAG", t.toString());
           //     pDialog.hide();
            }
        });
    }

    private void showFavorities() {
        SharedPreference sharedPreference = new SharedPreference();
        ArrayList<Shayari> favorites = sharedPreference.getFavorites(MainActivity.this);
        if (!favorites.isEmpty() || !favorites.equals(null)) {
            for (int i = 0; i < favorites.size(); i++) {
                // Toast.makeText(MainActivity.this, favorites.get(i).getTitle(),Toast.LENGTH_SHORT).show();
            }
        }


        Bitmap bitmap = BitmapFactory.decodeResource(MainActivity.this.getResources(),
                R.drawable.favorite);
        shayariFragment = new ShayariFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("bitmap", bitmap);
        bundle.putString("catTitle", "Favorite");
        bundle.putString("catImgUrl", catImgUrl);

        bundle.putParcelableArrayList("shayariArrayList", favorites);

        shayariFragment.setArguments(bundle);
      //  pDialog.hide();
//        replaceFrag(shayariFragment, 1);
    }

 /*   private void replaceFrag(Fragment fragment, int position) {
        if (fragment != null) {
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                // Inflate transitions to apply
                Transition changeTransform = TransitionInflater.from(this).
                        inflateTransition(R.transition.change_image_transform);
                Transition explodeTransform = TransitionInflater.from(this).
                        inflateTransition(android.R.transition.explode);

                // Setup exit transition on first fragment
                fragment.setSharedElementReturnTransition(changeTransform);
                fragment.setExitTransition(explodeTransform);

                // Add second fragment by replacing first
                tvTransition.setTransitionName(getString(R.string.category_title) + position);
                ivTransition.setTransitionName(getString(R.string.category_img) + position);

                TextView tvFrag;

                ShayariFragment fragInst = new ShayariFragment();
              //  fragInst.setTransitionNameStr(getString(R.string.category_title) + position);
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.addSharedElement(tvTransition, getString(R.string.category_title) + position);
                //  fragmentTransaction.addSharedElement(ivTransition, getString(R.string.category_img) + position);
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack("transaction");
                fragmentTransaction.commit();
            } else {
                FragmentTransaction fragmentTransaction = fm.beginTransaction();
                fragmentTransaction.replace(R.id.container_body, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }


            if (!shayariList.isEmpty() || !shayariList.equals(null)) {
                shayariList = new ArrayList<Shayari>();
            }

        }
    }*/

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
        if (id == R.id.action_show_fav) {
            showFavorities();
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
