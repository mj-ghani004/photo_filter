package com.example.gss_mac.photofilter.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.gss_mac.photofilter.BitmapUtils;
import com.example.gss_mac.photofilter.R;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;

import java.io.ByteArrayOutputStream;
import java.util.List;

public class ThumbnailsAdapter extends RecyclerView.Adapter<ThumbnailsAdapter.viewHolder> {

    private ThumbnailsAdapterListener listener;
    private List<ThumbnailItem> list;
    private Context ctx;
    private int selectedIndex = 0;
    private ProgressDialog progressDialog;


    public ThumbnailsAdapter(List<ThumbnailItem> list, Context context, ThumbnailsAdapterListener listener) {
        this.list = list;
        this.ctx = context;
        this.listener = listener;
        progressDialog = BitmapUtils.getProgressDialogue(context);

    }


    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumbnail_list_item, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {

        final ThumbnailItem item = list.get(position);
        holder.filterName.setText(item.filterName);
        holder.thumbnail.setImageBitmap(item.image);

        //BitmapUtils.filtered_images.add(item.image);
       // Glide.with(ctx).load(item.image).asBitmap().into(holder.thumbnail);

       // ByteArrayOutputStream stream = new ByteArrayOutputStream();
       // item.image.compress(Bitmap.CompressFormat.PNG, 100, stream);

//        Glide.with(ctx)
//                .load(stream.toByteArray())
//                .asBitmap()
//                .into(holder.thumbnail);

        holder.thumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.onFilterSelected(item.filter);
                selectedIndex = position;
                notifyDataSetChanged();
                progressDialog.show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        //progressBar.setVisibility(View.GONE);
                        progressDialog.dismiss();

                    }
                }, 3000);
               // BitmapUtils.current_filter_index = position;
            }
        });

        if (selectedIndex == position) {
            holder.filterName.setTextColor(ContextCompat.getColor(ctx, R.color.white));
        } else {
            holder.filterName.setTextColor(ContextCompat.getColor(ctx, R.color.unslecteditem));
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        TextView filterName;
        ImageView thumbnail;

        public viewHolder(View itemView) {
            super(itemView);

            filterName = itemView.findViewById(R.id.filter_name);
            thumbnail = itemView.findViewById(R.id.thumbnail);
        }
    }

    public interface ThumbnailsAdapterListener {
        void onFilterSelected(Filter filter);
    }


}
