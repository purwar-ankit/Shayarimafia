package adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mobinationapps.com.shayarimafia.R;
import model.Categories;
import model.Shayari;
import utils.Utility;

/**
 * Created by ankit.purwar on 6/10/2016.
 */

public class ShayariAdapter extends RecyclerView.Adapter<ShayariAdapter.ShayariViewHolder> {

    private List<Shayari> shayariList = new ArrayList<Shayari>();
    private int rowLayout;
    private Context context;
    ViewGroup mViewGroup;
    private LayoutInflater inflater;


    public ShayariAdapter(List<Shayari> shayariList, Context context) {
        this.shayariList = shayariList;
        this.context = context;
        inflater = LayoutInflater.from(context);
        Log.d("atShayariAdapter", shayariList.get(2).getContent());
    }

    @Override
    public ShayariViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        mViewGroup = parent;
        View view = inflater.inflate(R.layout.shayari_tile, parent, false);
        ShayariViewHolder shayariViewHolder = new ShayariViewHolder(view);
        return shayariViewHolder;
    }

    @Override
    public void onBindViewHolder(final ShayariViewHolder holder, int position) {
        Shayari shayari = shayariList.get(position);
        Log.d("ankitTAG", "onBindViewHolder" + shayari.getTitle());
        holder.tvShayari.setText(Html.fromHtml(shayari.getContent()));

       /* if (position % 2 == 1) {
            holder.cardRelativeLayout.setBackgroundColor(Color.parseColor("#820000"));
        } else *//*if (position == 1)*//* {
            holder.cardRelativeLayout.setBackgroundColor(Color.parseColor("#008100"));
        }
*/
        holder.ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.toCopy(holder.tvShayari.getText().toString(), context);
            }
        });
        holder.ivShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utility.toShare(holder.tvShayari.getText().toString(), context);

            }
        });
    }



    @Override
    public int getItemCount() {
        return shayariList.size();
    }


    public static class ShayariViewHolder extends RecyclerView.ViewHolder {
        protected ImageView ivAddToFavourite;
        protected TextView tvShayari;
        protected CardView cardView;
        protected RelativeLayout cardRelativeLayout;
        protected FloatingActionButton fabMenu, fabSpeak, fabCopy, fabShare;
        protected ImageView ivSpeak, ivCopy, ivShare;

        public ShayariViewHolder(View itemView) {
            super(itemView);
            this.ivAddToFavourite = (ImageView) itemView.findViewById(R.id.ivFavourite);
            this.cardView = (CardView) itemView.findViewById(R.id.card_view);
            this.tvShayari = (TextView) itemView.findViewById(R.id.tvShayari);
            this.cardRelativeLayout = (RelativeLayout) itemView.findViewById(R.id.cardRelativeLayout);
            this.fabMenu = (FloatingActionButton) itemView.findViewById(R.id.fab);
            this.fabShare = (FloatingActionButton) itemView.findViewById(R.id.fabShare);
            this.fabSpeak = (FloatingActionButton) itemView.findViewById(R.id.fabSpeak);
            this.fabCopy = (FloatingActionButton) itemView.findViewById(R.id.fabCopy);

            this.ivSpeak = (ImageView) itemView.findViewById(R.id.ivSpeak);
            this.ivCopy = (ImageView) itemView.findViewById(R.id.ivCopy);
            this.ivShare = (ImageView) itemView.findViewById(R.id.ivShare);
        }
    }


}
