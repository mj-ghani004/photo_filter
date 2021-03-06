package com.example.gss_mac.photofilter.Utility;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.widget.Toast;

import com.example.gss_mac.photofilter.BitmapUtils;
import com.example.gss_mac.photofilter.R;

public class FrameUtils {

    //filename of frame
    private int mFrameName;

    //Rect of picture area in frame
    private final Rect mPictureRect;

    //degree of rotation to fit picture and frame.
    private final float mRorate;

    //Save Context
    Context mContext;

    public FrameUtils(int frameId,int left, int top, int right, int bottom, float rorate) {
        mFrameName = frameId;
        mPictureRect = new Rect(left, top, right, bottom);
        mRorate = rorate;
    }

    public Bitmap mergeWith(Context context, Bitmap pictureBitmap) {
       // Bitmap frameBitmap = AssetsUtil.getBitmapFromAsset(context, mFrameName);
       //  Bitmap frameBitmap = BitmapFactory.decodeResource(context.getResources() , R.drawable.pfm_dummy);
        mContext = context;
        Bitmap frameBitmap = getFrame(mFrameName);

        //Bitmap frameBitmap = BitmapFactory.decodeResource(context.getResources() , R.drawable.lpm_dummyframe);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(frameBitmap.getWidth(), frameBitmap.getHeight(), conf);
        Canvas canvas = new Canvas(bitmap);

//        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = Bitmap.createBitmap(pictureBitmap.getWidth(), pictureBitmap.getHeight(), conf);
//        Canvas canvas = new Canvas(bitmap);

        Matrix matrix = getMatrix (pictureBitmap);
        Matrix frame_matrix = getMatrix (frameBitmap);



        canvas.drawBitmap(pictureBitmap, matrix, null);
        canvas.drawBitmap(frameBitmap, 0,0, null);




        //canvas.drawBitmap(pictureBitmap, matrix, null);


        return bitmap;

    }
    public Bitmap mergeWithTesting(Context context, Bitmap pictureBitmap) {
        // Bitmap frameBitmap = AssetsUtil.getBitmapFromAsset(context, mFrameName);
        //  Bitmap frameBitmap = BitmapFactory.decodeResource(context.getResources() , R.drawable.pfm_dummy);
        mContext = context;
       // Bitmap frameBitmap = getFrame(mFrameName);

        Bitmap frameBitmap = BitmapFactory.decodeResource(context.getResources() , R.drawable.land_update);

        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = Bitmap.createBitmap(frameBitmap.getWidth(), frameBitmap.getHeight(), conf);
        Canvas canvas = new Canvas(bitmap);

//        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = Bitmap.createBitmap(pictureBitmap.getWidth(), pictureBitmap.getHeight(), conf);
//        Canvas canvas = new Canvas(bitmap);

        Matrix matrix = getMatrix (pictureBitmap);
        Matrix frame_matrix = getMatrix (frameBitmap);



        canvas.drawBitmap(pictureBitmap, matrix, null);
        canvas.drawBitmap(frameBitmap, 0,0, null);




        //canvas.drawBitmap(pictureBitmap, matrix, null);


        return bitmap;

    }
//
//    public Bitmap DummyMergeWith(Context context, Bitmap pictureBitmap , Bitmap frame) {
//        // Bitmap frameBitmap = AssetsUtil.getBitmapFromAsset(context, mFrameName);
//      //  Bitmap frameBitmap = BitmapFactory.decodeResource(context.getResources() , R.drawable.dummy_frame);
//
//        Bitmap frameBitmap = frame;
//        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
//        Bitmap bitmap = Bitmap.createBitmap(frameBitmap.getWidth(), frameBitmap.getHeight(), conf);
//        Canvas canvas = new Canvas(bitmap);
//
//        int displayWidth = pictureBitmap.getWidth();
//
////        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
////        Bitmap bitmap = Bitmap.createBitmap(pictureBitmap.getWidth(), pictureBitmap.getHeight(), conf);
////        Canvas canvas = new Canvas(bitmap);
//
//        Matrix matrix = getMatrix (pictureBitmap);
//        Matrix frame_matrix = getMatrix (frameBitmap);
//
//        BitmapFactory.Options options = new BitmapFactory.Options();
//        options.inJustDecodeBounds = true;
//
//        BitmapFactory.decodeResource(context.getResources(), R.drawable.startbg,options);
//        int width = options.outWidth;
//
//        if (width > displayWidth) {
//            int widthRatio = Math.round((float) width / (float) displayWidth);
//            options.inSampleSize = widthRatio;
//        }
//        options.inJustDecodeBounds = false;
//        Bitmap scaledBitmap = BitmapFactory.decodeResource(context.getResources(),
//                R.drawable.startbg, options);
//
//
//      //  canvas.drawBitmap(pictureBitmap, matrix, null);
//        canvas.drawBitmap(pictureBitmap, 0,0,null);
//        canvas.drawBitmap(scaledBitmap, 0,0, null);
//
//
//
//
//        //canvas.drawBitmap(pictureBitmap, matrix, null);
//
//
//        return bitmap;
//
//    }



    Matrix getMatrix(Bitmap pictureBitmap) {
        float widthRatio = mPictureRect.width() /  (float) pictureBitmap.getWidth();
        float heightRatio = mPictureRect.height() / (float) pictureBitmap.getHeight();

        float ratio;

        if (widthRatio > heightRatio) {
            ratio = widthRatio;

        } else {
            ratio = heightRatio;
        }

       // ratio = 0.9f;

        float width = pictureBitmap.getWidth() * ratio;
        float height = pictureBitmap.getHeight() * ratio;

//        float width = pictureBitmap.getWidth() ;
//        float height = pictureBitmap.getHeight() ;

        float left = mPictureRect.left - (width - mPictureRect.width()) / 2f;
        float top = mPictureRect.top - (height - mPictureRect.height()) / 2f;

        Matrix matrix = new Matrix();
        matrix.postRotate(mRorate);
        matrix.postScale(ratio, ratio);
        matrix.postTranslate(left, top);

        return matrix;
    }

    private Bitmap getFrame(int id )
    {
        Bitmap bitmap = null;
        if (BitmapUtils.isPortrait)
        {
            switch (id)
            {
                case 0 :
                {
                    bitmap =  BitmapFactory.decodeResource(mContext.getResources() , R.drawable.pfm_dummy);
                    break;
                }
                case 1 :
                {
                    bitmap =  BitmapFactory.decodeResource(mContext.getResources() , R.drawable.pfm_dummy);
                    break;
                }
                case 2 :
                {
                    bitmap =  BitmapFactory.decodeResource(mContext.getResources() , R.drawable.pfm_dummy);
                    break;
                }
                case 3 :
                {
                    bitmap =  BitmapFactory.decodeResource(mContext.getResources() , R.drawable.pfm_dummy);
                    break;
                }
                case 4 :
                {
                    bitmap =  BitmapFactory.decodeResource(mContext.getResources() , R.drawable.pfm_dummy);
                    break;
                }
                case 5 :
                {
                    bitmap =  BitmapFactory.decodeResource(mContext.getResources() , R.drawable.pfm_dummy);
                    break;
                }
                default:

                    Toast.makeText(mContext,"Cannot Select Frame",Toast.LENGTH_SHORT).show();
            }
        }

        else
        {
            switch (id)
            {
                case 0 :
                {
                    bitmap =  BitmapFactory.decodeResource(mContext.getResources() , R.drawable.land_update);
                    break;
                }
                case 1 :
                {
                    bitmap =  BitmapFactory.decodeResource(mContext.getResources() , R.drawable.land_final);
                    break;
                }
                case 2 :
                {
                    bitmap =  BitmapFactory.decodeResource(mContext.getResources() , R.drawable.lpm_dummyframe);
                    break;
                }
                case 3 :
                {
                    bitmap =  BitmapFactory.decodeResource(mContext.getResources() , R.drawable.lpm_dummyframe);
                    break;
                }

                case 4 :
                {
                    bitmap =  BitmapFactory.decodeResource(mContext.getResources() , R.drawable.lpm_dummyframe);
                    break;
                }

                case 5 :
                {
                    bitmap =  BitmapFactory.decodeResource(mContext.getResources() , R.drawable.lpm_dummyframe);
                    break;
                }



                default:

                    Toast.makeText(mContext,"Cannot Select Frame",Toast.LENGTH_SHORT).show();

            }
        }
        return  bitmap;
    }
}
