package mobinationapps.com.shayarimafia;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import adapter.CategoryAdapter;
import adapter.ShayariAdapter;
import model.Shayari;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import utils.BlurBuilder;
import utils.SharedPreference;

/**
 * Created by ankit.purwar on 6/16/2016.
 */

public class ShayariFragment extends Fragment {

    RelativeLayout rlFragmentShayriLay;
    CoordinatorLayout clFragmentTestLay;
    ImageView header;
    TextView tvTitle;
    RecyclerView rvShayari;
    ShayariAdapter shayariAdapter;
    ProgressDialog pDialog;
    final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    ArrayList<Shayari> shayariList;
    SharedPreference sharedPreference;
    String transitionStr;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shayariList = new ArrayList<Shayari>();
        sharedPreference = new SharedPreference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toast.makeText(getActivity(), "at fragment", Toast.LENGTH_SHORT).show();
        shayariList = getArguments().getParcelableArrayList("shayariArrayList");
        String catTitle = getArguments().getString("catTitle");
        String catImgUrl = getArguments().getString("catImgUrl");
        Bitmap bitmap = getArguments().getParcelable("bitmap");

        Bitmap blurredBitmap = BlurBuilder.blur( getActivity(), bitmap );

        int position = getArguments().getInt("catTransitionNamePos");
        View rootView = inflater.inflate(R.layout.test/*fragment_shayari*/, container, false);
        tvTitle = (TextView) rootView.findViewById(R.id.tvCatTitle);
       // rlFragmentShayriLay = (RelativeLayout) rootView.findViewById(R.id.rlFragmentShayriLay);
        clFragmentTestLay = (CoordinatorLayout)rootView.findViewById(R.id.clFragmentTestLay);
        header = (ImageView)rootView.findViewById(R.id.header);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("transitionNameRecvd", getString(R.string.category_title) + position);
            tvTitle.setTransitionName(transitionStr/*getString(R.string.category_title) + position*/);
           // rlFragmentShayriLay.setTransitionName(getString(R.string.category_img) + position);
            clFragmentTestLay.setTransitionName(getString(R.string.category_img) + position);
            header.setTransitionName(getString(R.string.category_img) + position);
            Log.d("transitionNameRecvd", "getTransitionName(): " + tvTitle.getTransitionName());
        }
        rvShayari = (RecyclerView) rootView.findViewById(R.id.rvShayari);
        rvShayari.setLayoutManager(new LinearLayoutManager(getActivity()));
        tvTitle.setText(catTitle);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            //rlFragmentShayriLay.setBackgroundDrawable(new BitmapDrawable(getActivity().getResources(), bitmap));
            clFragmentTestLay.setBackgroundDrawable(new BitmapDrawable(getActivity().getResources(), blurredBitmap));
            header.setImageBitmap(bitmap);
        } else {
//          //  rlFragmentShayriLay.setBackground(new BitmapDrawable(getActivity().getResources(), bitmap));
            clFragmentTestLay.setBackgroundDrawable(new BitmapDrawable(getActivity().getResources(), blurredBitmap));
            header.setImageBitmap(bitmap);
        }

        rvShayari.invalidate();
        shayariAdapter = new ShayariAdapter(shayariList, getActivity());
        shayariAdapter.notifyDataSetChanged();

        rvShayari.setAdapter(shayariAdapter);

        rvShayari.addOnItemTouchListener(new ShayariRecyclerTouchListener(getActivity(), rvShayari,
                new ShayariClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(getActivity(), shayariList.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onLongClick(View view, int position) {
                    }
                }));

        /*SharedPreference sharedPreference = new SharedPreference();
        Todo: //check for null shared preference
        List<Shayari> favorites = sharedPreference.getFavorites(getActivity());
        if(!favorites.isEmpty() || !favorites.equals(null)){
            for (int i=0;i<favorites.size();i++)
            {
                Toast.makeText(getActivity(), favorites.get(i).getTitle(),Toast.LENGTH_SHORT).show();
            }
        }*/
        return rootView;
    }

    public void setTransitionNameStr(String transStr){
        transitionStr = transStr;
    }

    public static interface ShayariClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class ShayariRecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private ShayariFragment.ShayariClickListener shayariClickListener;

        public ShayariRecyclerTouchListener(final Context context, final RecyclerView recyclerView, final ShayariFragment.ShayariClickListener categoryClickListener) {
            this.shayariClickListener = categoryClickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && categoryClickListener != null) {
                        shayariClickListener.onLongClick(child, recyclerView.getChildLayoutPosition(child));
                    }
                    super.onLongPress(e);
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && shayariClickListener != null && gestureDetector.onTouchEvent(e)) {
                shayariClickListener.onClick(child, rv.getChildLayoutPosition(child));
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

    public void getShayariByCatId(ApiInterface apiService, int catID, final int position) {

        Log.d("ankitTAG", "getShayariById");
        //pDialog = new ProgressDialog(this);
        // pDialog.setCancelable(true);
        //pDialog.setMessage("Loading...");

        pDialog = new ProgressDialog(getActivity(), R.style.MyTheme);
        pDialog.setCancelable(true);
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
                pDialog.hide();
                replaceFrag(shayariFragment, position);
            }

            @Override
            public void onFailure(Call<List<Shayari>> call, Throwable t) {
                Toast.makeText(getActivity(), "inside onFailure", Toast.LENGTH_SHORT).show();
                Log.e("ankitTAG", t.toString());
                pDialog.hide();
            }
        });
    }

}
