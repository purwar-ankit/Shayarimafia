package adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    public CategoryAdapter(List<Categories> categories, Context context) {
        this.categoriesList = categories;
        this.context = context;
        this.inflater = LayoutInflater.from(context);

    }

    @Override
    public CategoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewGroup = parent;
        View view = inflater.inflate(R.layout.category_tile, parent, false);
        CategoryViewHolder categoryViewHolder = new CategoryViewHolder(view);
        return categoryViewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryViewHolder holder, int position) {
        final Categories categories = categoriesList.get(position);
        Log.d("ankitTAG", "onBindViewHolder" + categories.getCategory_title());
        holder.tvCatName.setText(categories.getCategory_title() +" " +categories.getCategory_count());
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView tvCatName;
        ImageView tvCatIcon;
        RelativeLayout rlCatTileLayerLayout;

        public CategoryViewHolder(View itemView) {
            super(itemView);
            this.rlCatTileLayerLayout = (RelativeLayout)itemView.findViewById(R.id.rlCatTileLayerLayout);
            this.tvCatName = (TextView) itemView.findViewById(R.id.tvCatName);
            this.tvCatIcon = (ImageView) itemView.findViewById(R.id.ivCatIcon);
        }
    }
}
