package com.example.camerahardware;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
//import android.hardware.camera2.*;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private Camera mCamera;
    private CameraPreview mCameraPreview;
    boolean cam;
    private String currentPhotoPath="default path";
//    SurfaceView preview;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCamera = getCameraInstance();
        cam = checkCameraHardware(this);
        mCameraPreview = new CameraPreview(this, mCamera);


        FrameLayout preview = (FrameLayout) findViewById(R.id.camera_Preview);

        preview.addView(mCameraPreview);

        Button captureButton = (Button) findViewById(R.id.button_capture);
        captureButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("MyCameraApp", "mcamera access"+"   "+mCamera);
                Log.d("MyCameraApp", "camera open"+"   "+cam);
//                Log.d("MyCameraApp", "path"+"   "+currentPhotoPath)

                //mCameraPreview.surfaceCreated(mCameraPreview.mSurfaceHolder);

              // mCamera.startPreview();
                mCamera.takePicture(null, null, mPicture);

                //                mCameraPreview = new CameraPreview(this ,mCamera);
                Log.d("MyCameraApp", "path"+"   "+currentPhotoPath);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
//Do something after 1000ms
                        mCamera.startPreview();
                    }
                }, 1000);
//                preview.removeView(mCameraPreview);
//                preview.addView(mCameraPreview);
            }
        });

    }

    /**
     * Helper method to access the camera returns null if it cannot get the
     * camera or does not exist
     *
     * @return
     */
    private Camera getCameraInstance() {
        Camera camera = null;
        try {
            camera = Camera.open();


        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Unable to aceess cameara.", Toast.LENGTH_SHORT).show();
	        setResult(RESULT_CANCELED);
			finish();
        }
        return camera;
    }

    private boolean checkCameraHardware(Context context) {
        if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }
    Camera.PictureCallback mPicture = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            File pictureFile = null;
            try {
                pictureFile = getOutputMediaFile();
            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "Input file problem.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
                e.printStackTrace();
            }
            if (pictureFile == null) {
                return;
            }
            try {
                FileOutputStream fos = new FileOutputStream(pictureFile);
                fos.write(data);
                fos.close();
//                finish();
            } catch (FileNotFoundException e) {
                Toast.makeText(getApplicationContext(), "Input file problem.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();

            } catch (IOException e) {
                Toast.makeText(getApplicationContext(), "input exception0.", Toast.LENGTH_SHORT).show();
                setResult(RESULT_CANCELED);
                finish();
            }
        }
    };
    private File getOutputMediaFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
//    private static File getOutputMediaFile() {
//        File mediaStorageDir = new File(
//                Environment
//                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
//                "MyCameraApp");
//        if (!mediaStorageDir.exists()) {
//            if (!mediaStorageDir.mkdirs()) {
//                Log.d("MyCameraApp", "failed to create directory");
//                return null;
//            }
//        }
//        // Create a media file name
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
//                .format(new Date());
//        File mediaFile;
//        mediaFile = new File(mediaStorageDir.getPath() + File.separator
//                + "IMG_" + timeStamp + ".jpg");
//        Log.d("MyCameraApp", "Path"+"   "+mediaFile);
//
//
//        return mediaFile;
//    }
}