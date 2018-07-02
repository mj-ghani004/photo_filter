package com.example.gss_mac.photofilter.Fragments;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gss_mac.photofilter.Adapters.FrameAdapter;
import com.example.gss_mac.photofilter.Adapters.ThumbnailsAdapter;
import com.example.gss_mac.photofilter.BitmapUtils;
import com.example.gss_mac.photofilter.Models.FrameModel;
import com.example.gss_mac.photofilter.R;
import com.example.gss_mac.photofilter.Utility.SpacesItemDecoration;
import com.zomato.photofilters.FilterPack;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.utils.ThumbnailItem;
import com.zomato.photofilters.utils.ThumbnailsManager;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FramesFragment extends Fragment implements FrameAdapter.FrameClicked {


    FramesFragment.FrameListFragmentListener listener;

    public  RecyclerView recyclerView;
    public static ArrayList<FrameModel> FrameListItem = new ArrayList<>();
    public static FrameAdapter mAdapter;

    //Progress Dialogue

    private static ProgressBar progressBar;

    FramesFragment.PrepareFramesThumbnail task;
    private boolean pictureUpdated = true;

    public void setListener(FramesFragment.FrameListFragmentListener listener) {
        this.listener = listener;
    }

    public void setPictureUpdated(boolean val) {
        this.pictureUpdated = val;
    }

    public FramesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //BitmapUtils.filtered_images.clear();
        task = new FramesFragment.PrepareFramesThumbnail(getActivity());

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_frames, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progressBar);

        if (pictureUpdated) { // If Picture updated do not populate again just use previous one

            setPictureUpdated(false);
           // FrameListItem = new ArrayList<>();
//        mAdapter = new ThumbnailsAdapter(getActivity(), thumbnailItemList, this);
            mAdapter = new FrameAdapter(getActivity() , FrameListItem , this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                    getResources().getDisplayMetrics());
            recyclerView.addItemDecoration(new SpacesItemDecoration(space));
            recyclerView.setAdapter(mAdapter);

            task.execute();


        } else {
            mAdapter = new FrameAdapter(getActivity() , FrameListItem ,this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                    getResources().getDisplayMetrics());
            recyclerView.addItemDecoration(new SpacesItemDecoration(space));
            recyclerView.setAdapter(mAdapter);
            progressBar.setVisibility(View.GONE);
        }
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
      //  mAdapter.notifyDataSetChanged();
    }

    @Override
    public void OnFrameClicked(int id) {
        if (listener != null) {
            listener.onFrameClicked(id);
        }
//        Toast.makeText(getActivity(),"Yoo i am Clicked",Toast.LENGTH_SHORT).show();
    }

//
//    @Override
//    public void onFilterSelected(Filter filter) {
//
//        if (listener != null) {
//            listener.onFilterSelected(filter);
//        }
//    }

    public interface FrameListFragmentListener {
        void onFrameClicked(int id);
    }
//===================================================================
//                            Async Task
//===================================================================

    public static class PrepareFramesThumbnail extends AsyncTask<Void, Void, Void> {

        //  private ProgressDialog progressDialog;
        private Bitmap local_image, rawImage;
        private Context mContext;

        PrepareFramesThumbnail(Context context) {

            //  progressDialog = BitmapUtils.getProgressDialogue(context);
//            local_image = BitmapUtils._selected_bitmap;
            mContext = context;

//            rawImage = local_image.copy(Bitmap.Config.ARGB_8888, true);


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
            //   progressDialog.show();


        }


        @Override
        protected Void doInBackground(Void... voids) {


            ArrayList<FrameModel> arrayList = BitmapUtils.getFramesList(mContext);
            FrameListItem.clear();
            FrameListItem.addAll(arrayList);


            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


            mAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);


        }
    }

//    public interface FrameClickedFragmentListener
//    {
//        void FrameClicked();
//    }

}
