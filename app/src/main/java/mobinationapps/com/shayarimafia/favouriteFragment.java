package mobinationapps.com.shayarimafia;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
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

import java.util.ArrayList;
import java.util.List;

import adapter.ShayariAdapter;
import animator.SlideInUpAnimator;
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

public class FavouriteFragment extends Fragment {

    RelativeLayout rlFragmentShayriLay;
    CoordinatorLayout clFragmentTestLay;
    ImageView header;
    TextView tvTitle, tvError;
    RecyclerView rvShayari;
    ShayariAdapter shayariAdapter;
    ProgressDialog pDialog;
    final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    String tvTransitionStr, ivTransitionStr;
    View rootView;
    SharedPreference sharedPreference ;
    List<Shayari> favorites;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        favorites = new ArrayList<Shayari>();
        sharedPreference = new SharedPreference();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toast.makeText(getActivity(), "at fragment", Toast.LENGTH_SHORT).show();
        Log.d("ankitFragTag", "inside fav frag");
        String catTitle = getArguments().getString("catTitle");
        //   String catImgUrl = getArguments().getString("catImgUrl");
        Bitmap bitmap = getArguments().getParcelable("bitmap");
        Bitmap blurredBitmap = BlurBuilder.blur(getActivity(), bitmap);
        int position = getArguments().getInt("catTransitionNamePos");

         shayariAdapter = new ShayariAdapter(favorites, getActivity());

        if (rootView == null) {
            rootView = inflater.inflate(R.layout.test/*fragment_shayari*/, container, false);
            tvTitle = (TextView) rootView.findViewById(R.id.tvCatTitle);
            tvError = (TextView) rootView.findViewById(R.id.tvError);
            clFragmentTestLay = (CoordinatorLayout) rootView.findViewById(R.id.clFragmentTestLay);
            header = (ImageView) rootView.findViewById(R.id.header);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Log.d("transitionNameRecvd", getString(R.string.category_title) + position);
                tvTitle.setTransitionName(tvTransitionStr);
                header.setTransitionName(ivTransitionStr);
                clFragmentTestLay.setTransitionName(getString(R.string.category_img) + position);
                Log.d("transitionNameRecvd", "getTransitionName(): " + tvTitle.getTransitionName());
            }
            rvShayari = (RecyclerView) rootView.findViewById(R.id.rvShayari);
            //rvShayari.setAdapter(shayariAdapter);
            rvShayari.setItemAnimator(new SlideInUpAnimator());
            rvShayari.setLayoutManager(new LinearLayoutManager(getActivity()));
            tvTitle.setText(catTitle);
            int sdk = Build.VERSION.SDK_INT;
            if (sdk < Build.VERSION_CODES.JELLY_BEAN) {

                clFragmentTestLay.setBackgroundDrawable(new BitmapDrawable(getActivity().getResources(), blurredBitmap));
                header.setImageBitmap(bitmap);
            } else {

                clFragmentTestLay.setBackgroundDrawable(new BitmapDrawable(getActivity().getResources(), blurredBitmap));
                header.setImageBitmap(bitmap);
            }
            rvShayari.invalidate();

            rvShayari.addOnItemTouchListener(new ShayariRecyclerTouchListener(getActivity(), rvShayari,
                    new ShayariClickListener() {
                        @Override
                        public void onClick(View view, int position) {
                            Toast.makeText(getActivity(), favorites.get(position).getTitle(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onLongClick(View view, int position) {
                        }
                    }));
            getFavShayaris();
            //getShayariByCatId(apiService, catId, position);
        }
        return rootView;
    }

    public void setTransitionNameStr(String tvTransStr, String ivTransStr) {
        tvTransitionStr = tvTransStr;
        ivTransitionStr = ivTransStr;
    }


    public static interface ShayariClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class ShayariRecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector gestureDetector;
        private FavouriteFragment.ShayariClickListener shayariClickListener;

        public ShayariRecyclerTouchListener(final Context context, final RecyclerView recyclerView, final FavouriteFragment.ShayariClickListener categoryClickListener) {
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

    private void getFavShayaris(){
        Log.d("ankitFragTag", "inside getFavShayari");
        favorites = sharedPreference.getFavorites(getActivity());

        if(!favorites.isEmpty() || !favorites.equals(null)){
            rvShayari.setVisibility(View.VISIBLE);
            tvError.setVisibility(View.GONE);

            shayariAdapter = new ShayariAdapter(favorites, getActivity());
            rvShayari.setAdapter(shayariAdapter);
           /* for (int i=0; i< favorites.size(); i++)
            {   Shayari shayari = new Shayari();
                shayari.setContent(favorites.get(i).getContent());
                shayari.setId(favorites.get(i).getId());
                shayari.setTitle(favorites.get(i).getTitle());
                favorites.add(shayari);
                //Toast.makeText(getActivity(), favorites.get(i).getTitle(),Toast.LENGTH_SHORT).show();
            }*/
            if (favorites != null && favorites.size() > 0){
                shayariAdapter.notifyItemRangeInserted(0, favorites.size()  - 1);
            }
        }else {
            rvShayari.setVisibility(View.GONE);
            tvError.setVisibility(View.VISIBLE);
            tvError.setText(getString(R.string.no_favorites));
        }
    }

}
