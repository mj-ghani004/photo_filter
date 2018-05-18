package com.example.gss_mac.photofilter.Fragments;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

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

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FiltersFragment extends Fragment implements ThumbnailsAdapter.ThumbnailsAdapterListener {

    private static final int PICK_IMAGE = 200;

    FiltersListFragmentListener listener;

    RecyclerView recyclerView;
    List<ThumbnailItem> thumbnailItemList;
    ThumbnailsAdapter mAdapter;

    //Progress Dialogue

    ProgressDialog progressDialog ;

    public void setListener(FiltersListFragmentListener listener) {
        this.listener = listener;
    }

    public FiltersFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = BitmapUtils.getProgressDialogue(getActivity());
        progressDialog.show();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_filters, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

        thumbnailItemList = new ArrayList<>();

//        mAdapter = new ThumbnailsAdapter(getActivity(), thumbnailItemList, this);
        mAdapter = new ThumbnailsAdapter(thumbnailItemList, getActivity() , this);


        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        int space = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8,
                getResources().getDisplayMetrics());
        recyclerView.addItemDecoration(new SpacesItemDecoration(space));
        recyclerView.setAdapter(mAdapter);

        prepareThumbnail(BitmapUtils._selected_bitmap);
        return view;
    }



//    @Override
//    public void onCreateOptionsMenu(Menu menu ,  MenuInflater inflater) {
//
//        super.onCreateOptionsMenu(menu, inflater);
//
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getActivity().getMenuInflater().inflate(R.menu.filter_menu, menu);
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_open) {
//
//            getImageFromGallery();
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        // If the image capture activity was called and was successful
//
//        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
//            //progressDialog.show();
////            imageFromGallery(this, data);
//
//        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_CANCELED) {
//            Toast.makeText(getActivity(), "Pictures Cancelled From Gallery", Toast.LENGTH_LONG).show();
//
//        }
//
//
//    }

    /**
     * Renders thumbnails in horizontal list
     * loads default image from Assets if passed param is null
     *
     * @param bitmap
     */
    public void prepareThumbnail(final Bitmap bitmap) {
        Runnable r = new Runnable() {
            public void run() {
                Bitmap thumbImage;

                if (bitmap == null) {
//                    thumbImage = Bitmap.getBitmapFromAssets(getActivity(), MainActivity.IMAGE_NAME, 100, 100);
                    thumbImage = null;
                } else {
//                    thumbImage = Bitmap.createScaledBitmap(bitmap, 100, 100, false);
                    thumbImage = bitmap;
                 //   thumbImage = BitmapUtils.BITMAP_RESIZER(bitmap,100,100);


                }

                if (thumbImage == null)
                    return;

                ThumbnailsManager.clearThumbs();
                thumbnailItemList.clear();

                // add normal bitmap first
                ThumbnailItem thumbnailItem = new ThumbnailItem();
                thumbnailItem.image = thumbImage;
                thumbnailItem.filterName = getString(R.string.label_normal);
                ThumbnailsManager.addThumb(thumbnailItem);

                List<Filter> filters = FilterPack.getFilterPack(getActivity());

                for (Filter filter : filters) {
                    ThumbnailItem tI = new ThumbnailItem();
                    tI.image = thumbImage;
                    tI.filter = filter;
                    tI.filterName = filter.getName();
                    ThumbnailsManager.addThumb(tI);
                }

                thumbnailItemList.addAll(ThumbnailsManager.processThumbs(getActivity()));

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mAdapter.notifyDataSetChanged();
                        progressDialog.dismiss();
                    }
                });
            }
        };

        new Thread(r).start();
    }

//    private void getImageFromGallery() {
//
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.setType("image/*");
//        Intent chooserIntent = Intent.createChooser(intent, "Select Picture");
//        startActivityForResult(chooserIntent, PICK_IMAGE);
//    }

    @Override
    public void onFilterSelected(Filter filter) {

        if (listener != null) {
            listener.onFilterSelected(filter);
        }
    }

    public interface FiltersListFragmentListener {
        void onFilterSelected(Filter filter);
    }


}
