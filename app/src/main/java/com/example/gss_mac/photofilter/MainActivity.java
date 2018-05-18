package com.example.gss_mac.photofilter;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionButton;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int PICK_IMAGE = 200;

    private static final int REQUEST_STORAGE_PERMISSION = 1;
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.gss_mac.photofilterr.fileprovider";

    private String mTempPhotoPath;


    @BindView(R.id.fab_camera)
    FloatingActionButton fab_camera;
    @BindView(R.id.fab_gallery)
    FloatingActionButton fab_gallery;
    @BindView(R.id.sample_image)
    ImageView sampleImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);


        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraInitialization();
            }
        });

        fab_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getImageFromGallery();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void cameraInitialization() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // If you do not have permission, request it
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_STORAGE_PERMISSION);
        } else {
            // Launch the camera if the permission exists
            launchCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        // Called when you request permission to read and write to external storage
        switch (requestCode) {
            case REQUEST_STORAGE_PERMISSION: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // If you get permission, launch the camera
                    launchCamera();
                } else {
                    // If you do not get permission, show a Toast
                    Toast.makeText(this, R.string.permission_denied, Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private void launchCamera() {

        // Create the capture image intent
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the temporary File where the photo should go
            File photoFile = null;
            try {
                photoFile = BitmapUtils.createTempImageFile(this);
            } catch (IOException ex) {
                // Error occurred while creating the File
                ex.printStackTrace();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                // Get the path of the temporary file
                mTempPhotoPath = photoFile.getAbsolutePath();

                // Get the content URI for the image file
                Uri photoURI = FileProvider.getUriForFile(this,
                        FILE_PROVIDER_AUTHORITY,
                        photoFile);

                // Add the URI so the camera can store the image
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Launch the camera activity
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // If the image capture activity was called and was successful
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // Process the image and set it to the TextView
            ImageFromCamera(this);
        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            ImageFromGallery(this, data);

        } else if (requestCode == PICK_IMAGE && resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplicationContext(), "Pictures Cancelled From Gallery", Toast.LENGTH_LONG).show();

        } else if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_CANCELED) {
            // Otherwise, delete the temporary image file
            BitmapUtils.deleteImageFile(this, mTempPhotoPath);
            Toast.makeText(getApplicationContext(), "Pictures Canceled From Camera", Toast.LENGTH_LONG).show();
        }


    }


    /**
     * Method for processing the captured image and setting it to the TextView.
     */
    private void ImageFromCamera(Context context) {

        Toast.makeText(getApplicationContext(), "Picture Captures", Toast.LENGTH_LONG).show();

        Intent mIntent = new Intent(context, FilterActivity.class);
        context.startActivity(mIntent);

    }

    private void ImageFromGallery(Context context, Intent data) {

        Toast.makeText(getApplicationContext(), "Image from Gallery", Toast.LENGTH_LONG).show();
        Intent mIntent = new Intent(context, FilterActivity.class);

        try {
            final Uri imageUri = data.getData();
            final InputStream imageStream = getContentResolver().openInputStream(imageUri);
            final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
            BitmapUtils._selected_bitmap = selectedImage;
//            mIntent.putExtra("image", selectedImage);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(context, "Something went wrong", Toast.LENGTH_LONG).show();
        }


        context.startActivity(mIntent);


    }

    private void getImageFromGallery() {

        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        Intent chooserIntent = Intent.createChooser(intent, "Select Picture");
        startActivityForResult(chooserIntent, PICK_IMAGE);
    }


}
