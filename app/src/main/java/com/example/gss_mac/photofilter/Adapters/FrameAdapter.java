package com.example.gss_mac.photofilter.Adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gss_mac.photofilter.BitmapUtils;
import com.example.gss_mac.photofilter.Models.FrameModel;
import com.example.gss_mac.photofilter.R;

import java.util.ArrayList;

public class FrameAdapter extends RecyclerView.Adapter<FrameAdapter.viewHolder> {

    private ArrayList<FrameModel> arrayList ;
    private Context ctx;
    private FrameAdapter.FrameClicked listener;
    private int selectedIndex = 0;
    private ProgressDialog progressDialog;




    public FrameAdapter(Context context ,ArrayList<FrameModel> arrayList , FrameClicked listener)
    {
        this.ctx = context;
        this.arrayList = arrayList;
        this.listener  = listener;
        progressDialog = BitmapUtils.getProgressDialogue(context);
    }
    @NonNull
    @Override
    public  viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

       View view =  LayoutInflater.from(parent.getContext()).inflate(R.layout.frame_list_item , parent , false);
       return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, final int position) {

        FrameModel fm = arrayList.get(position);
        holder.frameName.setText(fm.getFrameName());
        holder.frameThumbnail.setImageDrawable(fm.getFrameId());

        holder.frameThumbnail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(ctx , "Yoo I a")
                selectedIndex = position;
                notifyDataSetChanged();

                progressDialog.show();

                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Do something after 5s = 5000ms
                        //progressBar.setVisibility(View.GONE);
                        listener.OnFrameClicked(position);
                        progressDialog.dismiss();

                    }
                }, 500);


            }
        });

        if (selectedIndex == position) {
            holder.frameName.setTextColor(ContextCompat.getColor(ctx, R.color.white));
        } else {
            holder.frameName.setTextColor(ContextCompat.getColor(ctx, R.color.unslecteditem));
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }


    public static class viewHolder extends RecyclerView.ViewHolder {

         TextView frameName;
         ImageView frameThumbnail;

        public viewHolder(View itemView) {
            super(itemView);

            frameName = itemView.findViewById(R.id.frame_name);
            frameThumbnail = itemView.findViewById(R.id.frame_thumbnail);
        }


    }

    public interface FrameClicked
    {
        void OnFrameClicked(int id);
    }
}
