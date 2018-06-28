package com.example.gss_mac.photofilter.Fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.gss_mac.photofilter.Adapters.ThumbnailsAdapter;
import com.example.gss_mac.photofilter.BitmapUtils;
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
public class FiltersFragment extends Fragment implements ThumbnailsAdapter.ThumbnailsAdapterListener {


    FiltersListFragmentListener listener;

    RecyclerView recyclerView;
    public static List<ThumbnailItem> thumbnailItemList;
    public static ThumbnailsAdapter mAdapter;

    //Progress Dialogue

    private static ProgressBar progressBar;

    PrepareThumbnailTask task;
    private boolean pictureUpdated = true;

    public void setListener(FiltersListFragmentListener listener) {
        this.listener = listener;
    }

    public void setPictureUpdated(boolean val) {
        this.pictureUpdated = val;
    }

    public FiltersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BitmapUtils.filtered_images.clear();
        task = new PrepareThumbnailTask(getActivity());

        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filters, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);
        progressBar = view.findViewById(R.id.progressBar);

        if (pictureUpdated) { // If Picture updated do not populate again just use previous one

            setPictureUpdated(false);
            thumbnailItemList = new ArrayList<>();
//        mAdapter = new ThumbnailsAdapter(getActivity(), thumbnailItemList, this);
            mAdapter = new ThumbnailsAdapter(thumbnailItemList, getActivity(), this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                    getResources().getDisplayMetrics());
            recyclerView.addItemDecoration(new SpacesItemDecoration(space));
            recyclerView.setAdapter(mAdapter);
            task.execute();

        } else {
            mAdapter = new ThumbnailsAdapter(thumbnailItemList, getActivity(), this);
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
    public void onFilterSelected(Filter filter) {

        if (listener != null) {
            listener.onFilterSelected(filter);
        }
    }

    public interface FiltersListFragmentListener {
        void onFilterSelected(Filter filter);
    }
//===================================================================
//                            Async Task
//===================================================================

    public static class PrepareThumbnailTask extends AsyncTask<Void, Void, Void> {

      //  private ProgressDialog progressDialog;
        private Bitmap local_image, rawImage;
        private Context mContext;


        PrepareThumbnailTask(Context context) {

          //  progressDialog = BitmapUtils.getProgressDialogue(context);
            local_image = BitmapUtils._selected_bitmap;
            mContext = context;
            rawImage = local_image.copy(Bitmap.Config.ARGB_8888, true);


        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
         //   progressDialog.show();


        }


        @Override
        protected Void doInBackground(Void... voids) {

            Bitmap thumbImage = null;

            if (local_image != null) {
                thumbImage = local_image;
            }


            if (thumbImage == null)
                return null;

            ThumbnailsManager.clearThumbs();
            thumbnailItemList.clear();

            // add normal bitmap first
            ThumbnailItem thumbnailItem = new ThumbnailItem();
            thumbnailItem.image = thumbImage;
            thumbnailItem.filterName = mContext.getString(R.string.label_normal);
            ThumbnailsManager.addThumb(thumbnailItem);

            List<Filter> filters = FilterPack.getFilterPack(mContext);

            for (Filter filter : filters) {
                ThumbnailItem tI = new ThumbnailItem();
                tI.image = thumbImage;
                tI.filter = filter;
                tI.filterName = filter.getName();
                ThumbnailsManager.addThumb(tI);

             //   BitmapUtils.filtered_images.add(filter.processFilter(rawImage));
            }

            thumbnailItemList.addAll(ThumbnailsManager.processThumbs(mContext));
            return null;

        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            mAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.GONE);



        }
    }

}
