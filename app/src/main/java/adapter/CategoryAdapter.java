package adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

import mobinationapps.com.shayarimafia.R;
import model.Categories;


/**
 * Created by ankit.purwar on 6/14/2016.
 */

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    private List<Categories> categoriesList = new ArrayList<>();
    private int rowLayout;
    private Context context;
    ViewGroup mViewGroup;
    private LayoutInflater inflater;
    ProgressDialog pDialog;

    public CategoryAdapter(List<Categories> categories, Context context) {
        this.categoriesList = categories;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        pDialog = new ProgressDialog(context);
        pDialog.setCancelable(true);
        pDialog.setMessage("Loading...");
        pDialog.show();
    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewGroup = parent;
        View view = inflater.inflate(R.layout.category_tile, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(final CategoryViewHolder holder, int position) {
        final Categories categories = categoriesList.get(position);
        Log.d("ankitTAG", "onBindViewHolder" + categories.getCategory_title());
        Log.d("ankitTAGImg","img_url: "+categories.getImg_url());
        holder.tvCatName.setText(categories.getCategory_title() +" " +categories.getCategory_count());
        Picasso.with(context).load(categories.getImg_url())
                .placeholder(R.drawable.img3)
                .resize(200,200).into(holder.ivCatIcon);
        pDialog.hide();
       /* Picasso.with(context).load(categories.getImg_url())
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        holder.ivCatIcon.setImageBitmap(bitmap);
                        pDialog.hide();
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        pDialog.show();


                    }
                });*/
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCatName;
        ImageView ivCatIcon;
        RelativeLayout rlCatTileLayerLayout;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            //this.rlCatTileLayerLayout = (RelativeLayout)itemView.findViewById(R.id.rlCatTileLayerLayout);
            this.tvCatName = (TextView) itemView.findViewById(R.id.tvCatName);
            this.ivCatIcon = (ImageView) itemView.findViewById(R.id.ivCatIcon);
        }
    }
}
