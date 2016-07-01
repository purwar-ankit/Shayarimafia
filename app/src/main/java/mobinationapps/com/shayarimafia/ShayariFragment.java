package mobinationapps.com.shayarimafia;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by ankit.purwar on 6/16/2016.
 */

public class ShayariFragment extends Fragment {

    RelativeLayout rlFragmentShayriLay;
    TextView tvTitle;
    RecyclerView rvShayari;
    ShayariAdapter shayariAdapter;
    ProgressDialog pDialog;
    final ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
    ArrayList<Shayari> shayariList;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        shayariList = new ArrayList<Shayari>();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Toast.makeText(getActivity(), "at fragment", Toast.LENGTH_SHORT).show();
        shayariList = getArguments().getParcelableArrayList("shayariArrayList");
        String catTitle = getArguments().getString("catTitle");
        String catImgUrl = getArguments().getString("catImgUrl");
        Bitmap bitmap = getArguments().getParcelable("bitmap");
        int position = getArguments().getInt("catTransitionNamePos");
        View rootView = inflater.inflate(R.layout.fragment_shayari, container, false);
        tvTitle = (TextView) rootView.findViewById(R.id.tvCatTitle);
        rlFragmentShayriLay = (RelativeLayout) rootView.findViewById(R.id.rlFragmentShayriLay);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Log.d("transitionNameRecvd", getString(R.string.category_title) + position);
            tvTitle.setTransitionName(getString(R.string.category_title) + position);
            rlFragmentShayriLay.setTransitionName(getString(R.string.category_img) + position);
            Log.d("transitionNameRecvd", "getTransitionName(): " + tvTitle.getTransitionName());
        }
        rvShayari = (RecyclerView) rootView.findViewById(R.id.rvShayari);
        rvShayari.setLayoutManager(new LinearLayoutManager(getActivity()));
        tvTitle.setText(catTitle);
        int sdk = android.os.Build.VERSION.SDK_INT;
        if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            rlFragmentShayriLay.setBackgroundDrawable(new BitmapDrawable(getActivity().getResources(), bitmap));
        } else {
            rlFragmentShayriLay.setBackground(new BitmapDrawable(getActivity().getResources(), bitmap));
            //return new BitmapDrawable(context.getResources(), canvasBitmap);
        }
        /*Picasso.with(getActivity()).load(catImgUrl).into(new Target() {
            int sdk = android.os.Build.VERSION.SDK_INT;
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    rlFragmentShayriLay.setBackgroundDrawable(new BitmapDrawable(getActivity().getResources(), bitmap));
                } else {
                    rlFragmentShayriLay.setBackground(new BitmapDrawable(getActivity().getResources(), bitmap));
                    //return new BitmapDrawable(context.getResources(), canvasBitmap);
                }
            }

            @Override
            public void onBitmapFailed(Drawable errorDrawable) {
            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {
            }
        });*/

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

        return rootView;

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
}
