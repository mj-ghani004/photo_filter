package com.example.gss_mac.photofilter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.gss_mac.photofilter.Fragments.FiltersFragment;
import com.example.gss_mac.photofilter.Fragments.FramesFragment;
import com.example.gss_mac.photofilter.Fragments.SettingFragment;
import com.example.gss_mac.photofilter.Utility.FrameUtils;
import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubFilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;

import java.io.FileNotFoundException;
import java.io.InputStream;


public class FilterActivity extends AppCompatActivity implements FiltersFragment.FiltersListFragmentListener
        , SettingFragment.EditImageFragmentListener
        , FramesFragment.FrameListFragmentListener {

    private static final int PICK_IMAGE = 200;

    private ImageView image_preview;
    private BottomNavigationView mBottomNav;
    private FrameLayout mContainer;

    private Toolbar mToolbar;
    Bitmap selected_image;
    Bitmap filtered_image;
    Bitmap final_image;

    // Fragments

    private FiltersFragment filtersFragment;
    private FramesFragment framesFragment;
    private SettingFragment settingFragment;

    // Loading Spinner

    private ProgressDialog progressDialog;
    private ProgressBar progressBar;

    //Edit Image Setting

    private int mBrighntess;
    private float mContrast;
    private float mSaturation;
    private ColorMatrixColorFilter colorMatrixColorFilter;

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_filter_new);

        progressDialog = BitmapUtils.getProgressDialogue(this);

        // initializing Toolbar

        mToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        getSupportActionBar().setTitle(" ");


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Getting View
        image_preview = findViewById(R.id.image_preview);
        mBottomNav = findViewById(R.id.botton_nav);
        mContainer = findViewById(R.id.fragment_container);
        progressBar = findViewById(R.id.progressBar);

        // initializing Fragments

        filtersFragment = new FiltersFragment();
        framesFragment = new FramesFragment();
        settingFragment = new SettingFragment();

        setFragment(filtersFragment);
        filtersFragment.setListener(this);
        framesFragment.setListener(this);
        settingFragment.setListener(this);


        //Setting Variables

        mBrighntess = 0;
        mContrast = 1;
        mSaturation = 1;

        final_image = BitmapUtils._selected_bitmap.copy(Bitmap.Config.ARGB_8888, true);


        mBottomNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.menu_filters:
                        setFragment(filtersFragment);
                        return true;

                    case R.id.menu_frames:
                        setFragment(framesFragment);


                        return true;

                    case R.id.menu_settings:


                        setFragment(settingFragment);


                        return true;

                    default:
                        Toast.makeText(getApplicationContext(), "Please Make a Right Selection", Toast.LENGTH_LONG).show();
                        return false;


                }
            }
        });


        selected_image = BitmapUtils._selected_bitmap;
        filtered_image = selected_image.copy(Bitmap.Config.ARGB_8888, true);
        image_preview.setImageBitmap(selected_image);


    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_open) {

            getImageFromGallery();
            return true;
        } else if (id == R.id.action_save) {

            saveImage();
            return true;
        } else if (id == R.id.action_share) {


            BitmapUtils.shareImage(getApplicationContext(),
                    BitmapUtils.saveImage(
                            this,
                            BitmapUtils.createFiteredBitmap(mBrighntess, mSaturation, mContrast, final_image)
                    ));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the image capture activity was called and was successful

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            imageFromGallery(this, data);

        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Pictures Cancelled From Gallery", Toast.LENGTH_LONG).show();

        }


    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        super.onBackPressed();

    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void imageFromGallery(Context context, Intent data) {

        Toast.makeText(getApplicationContext(), "Image from Gallery", Toast.LENGTH_LONG).show();

        try {
            final Uri imageUri = data.getData();
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            selected_image = BitmapFactory.decodeStream(imageStream);
            filtered_image = selected_image;

            BitmapUtils._selected_bitmap = selected_image;
            image_preview.setImageBitmap(selected_image);
            final_image = selected_image.copy(Bitmap.Config.ARGB_8888, true);


            filtersFragment = new FiltersFragment();
            filtersFragment.setListener(this);
            filtersFragment.setPictureUpdated(true);
            setFragment(filtersFragment);
            settingFragment.resetSeekbars(true);
            framesFragment.setPictureUpdated(true);

//            progressDialog.dismiss();

//            mIntent.putExtra("image", selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
        }


    }

    private void getImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(intent, "Select Picture");
        startActivityForResult(chooserIntent, PICK_IMAGE);
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        //fragmentTransaction.commit();
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onFilterSelected(final Filter filter) {

        progressDialog = BitmapUtils.getProgressDialogue(getApplicationContext());
//        progressDialog.show();
        //progressBar.setVisibility(View.VISIBLE);
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                // Do something after 5s = 5000ms
//                //progressBar.setVisibility(View.GONE);
//                progressDialog.dismiss();
//
//            }
//        }, 3000);

        final Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Do something after 5s = 5000ms
                Bitmap bitmap = filter.processFilter(filtered_image);
                image_preview.setImageBitmap(bitmap);
                changeFinalImage(bitmap);
//                image_preview.setImageBitmap(filter.processFilter(filtered_image));
            }
        }, 1000);

        image_preview.clearColorFilter();

        selected_image = BitmapUtils._selected_bitmap;
//        selected_image = image_preview.

        filtered_image = selected_image.copy(Bitmap.Config.ARGB_8888, true);


        final_image = filtered_image.copy(Bitmap.Config.ARGB_8888, true);


        settingFragment.resetSeekbars(true);

    }

    @Override
    public void onBrightnessChanged(int brightness) {

        mBrighntess = brightness;
        image_preview.setColorFilter(BitmapUtils.SetColors(mBrighntess, mContrast, mSaturation, final_image));
    }

    @Override
    public void onSaturationChanged(float saturation) {

        mSaturation = saturation;
        image_preview.setColorFilter(BitmapUtils.SetColors(mBrighntess, mContrast, mSaturation, final_image));

    }

    @Override
    public void onContrastChanged(float contrast) {

        mContrast = contrast;
        image_preview.setColorFilter(BitmapUtils.SetColors(mBrighntess, mContrast, mSaturation, final_image));
    }

    @Override
    public void onEditStarted() {

    }

    @Override
    public void onEditCompleted() {

    }

    private void saveImage() {
        // progressDialog.show();
        //final_image = filtered_image.copy(Bitmap.Config.ARGB_8888, true);


        BitmapUtils.saveImage(getApplicationContext(),
                BitmapUtils.createFiteredBitmap(mBrighntess, mContrast, mSaturation, final_image));

        // progressDialog.dismiss();
        Snackbar.make(mContainer, "Image Saved Successfully!", Snackbar.LENGTH_LONG)
                .setAction("OK", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                    }
                })
                .setActionTextColor(getResources().getColor(android.R.color.holo_red_light))
                .show();
    }


    @Override

    public void onFrameClicked(int id) {

        FrameUtils frameA;
        if (BitmapUtils.isPortrait) {
            //Old
            // frameA = new FrameUtils(id, 0, 5350, 3500, 400, 0);
             // NEW MULTI RES
             frameA = new FrameUtils(id, 0, 1950, 1150, 0, 0);
//            frameA = new FrameUtils(1, 150, 5100, 3500, 700, 0);
        } else {
            //OLD
            //frameA = new FrameUtils(id, 700, 2700, 5150, 480, 0);
            //NEW MULTI RES
            frameA = new FrameUtils(id, 100, 1250, 2050, 0, 0);
        }
        Bitmap mergedBitmap = frameA.mergeWith(this, filtered_image);
//        Bitmap mergedBitmap = frameA.mergeWith(this, BitmapUtils._selected_bitmap);

        image_preview.setImageBitmap(mergedBitmap);
        changeFinalImage(mergedBitmap);
        final_image = mergedBitmap.copy(Bitmap.Config.ARGB_8888, true);

        Toast.makeText(getApplicationContext(), "Yoo i am Clicked from Activity " + Integer.toString(id), Toast.LENGTH_SHORT).show();


    }

    private void changeFinalImage(Bitmap bitmap) {
        //filtered_image = bitmap.copy(Bitmap.Config.ARGB_8888,true);
        final_image = bitmap.copy(Bitmap.Config.ARGB_8888, true);
    }
}
