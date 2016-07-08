package mobinationapps.com.shayarimafia;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adapter.CategoryAdapter;
import adapter.ShayariAdapter;
import model.Categories;
import model.Shayari;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.Utility;

/**
 * Created by ankit.purwar on 6/17/2016.
 */

public class CategoryFragment extends Fragment {

    TextView tvTitle;
    RecyclerView rvCategoryView;
    ArrayList<Categories> categoriesList;
    ProgressDialog pDialog;
    CategoryAdapter categoryAdapter;
    final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    FragmentManager fm;
    ImageView ivTransition;
    TextView tvTransition;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoriesList = new ArrayList<Categories>();
        fm = getActivity().getSupportFragmentManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toast.makeText(getActivity(), "at fragment", Toast.LENGTH_SHORT).show();
//        categoriesList = getArguments().getParcelableArrayList("categoryArrayList");
      //  Log.d("categoryArrayList", categoriesList.get(1).getCategory_title());

        View rootView = inflater.inflate(R.layout.fragment_category, container, false);
        tvTitle = (TextView) rootView.findViewById(R.id.tvCatTitle);
        rvCategoryView = (RecyclerView) rootView.findViewById(R.id.rvCategory);

        rvCategoryView.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCategoryView.setHasFixedSize(true);
        rvCategoryView.setItemViewCacheSize(20);
        rvCategoryView.setDrawingCacheEnabled(true);
        rvCategoryView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_AUTO);

        View view = rvCategoryView.getChildAt(0);
        if (view != null && rvCategoryView.getChildAdapterPosition(view) == 0) {
            view.setTranslationY(-view.getTop() / 2);// or use view.animate().translateY();
        }

        rvCategoryView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MainActivity.FAB_Status) {
                    MainActivity.hideFAB();
                    MainActivity.FAB_Status = false;
                }
                return false;
            }
        });

        getCategories(apiService);
        /*categoryAdapter = new CategoryAdapter(categoriesList, getActivity());
        rvCategoryView.setAdapter(categoryAdapter);*/

      /*  rvCategoryView.addOnItemTouchListener(new CategoryRecyclerTouchListener(getActivity(), rvCategoryView,
                new CategoryClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(getActivity(), "calling getShayaris()", Toast.LENGTH_SHORT).show();


                      //   ShayariFragment shayariFragment = new ShayariFragment();
                       // MainActivity mainActivity = new MainActivity();
                       // mainActivity.replaceFrag(shayariFragment);

                        //MainActivity mainActivity = new MainActivity();
                       // mainActivity.getShayariByCatId(apiService, categoriesList.get(position).getCategory_id());
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));*/

        return rootView;
    }

    public void getCategories(final ApiInterface apiService) {
        pDialog = new ProgressDialog(getActivity());
        pDialog.setCancelable(false);
        pDialog.setMessage("Loading...");
        pDialog.show();
        Call<List<Categories>> call = apiService.getAllCategories();
        call.enqueue(new Callback<List<Categories>>() {
            @Override
            public void onResponse(Call<List<Categories>> call, Response<List<Categories>> response) {
                Toast.makeText(getActivity(), "inside onResponse", Toast.LENGTH_SHORT).show();
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

               /* fragment = new CategoryFragment();
                Bundle bundle = new Bundle();
                bundle.putParcelableArrayList("categoryArrayList", categoriesList);
                fragment.setArguments(bundle);
                pDialog.hide();
                replaceFrag(fragment);*/
                /*
                categoryAdapter = new CategoryAdapter(categoriesList, MainActivity.this);
                rvCategoryView.setAdapter(categoryAdapter);*/
                categoryAdapter = new CategoryAdapter(categoriesList, getActivity());
                rvCategoryView.setAdapter(categoryAdapter);

                pDialog.hide();


                rvCategoryView.addOnItemTouchListener(new CategoryRecyclerTouchListener(getActivity(), rvCategoryView,
                        new CategoryClickListener() {
                            @Override
                            public void onClick(View view, int position) {
                                Toast.makeText(getActivity(), "calling getShayaris()", Toast.LENGTH_SHORT).show();
                               // getShayariByCatId(apiService, categoriesList.get(position).getCategory_id());

                                ivTransition = (ImageView) view.findViewById(R.id.ivCatIcon);
                                tvTransition = (TextView) view.findViewById(R.id.tvCatName);

                                Map<String, View> transitionMap = new HashMap<>();
                                transitionMap.put(getString(R.string.category_title) + position , tvTransition);
                                transitionMap.put(getString(R.string.category_img) + position, ivTransition);

                                ShayariFragment shayariFragment = new ShayariFragment();
                                Utility.replaceFrag(getActivity(),shayariFragment,position,fm,transitionMap);

                            }

                            @Override
                            public void onLongClick(View view, int position) {
                            }
                        }));
            }

            @Override
            public void onFailure(Call<List<Categories>> call, Throwable t) {
                Toast.makeText(getActivity(), "inside onFailure", Toast.LENGTH_SHORT).show();
                Log.e("ankitTAG", t.toString());
                pDialog.hide();
            }
        });
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
}
