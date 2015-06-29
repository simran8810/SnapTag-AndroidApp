package com.samast.androidapptest;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ht on 28/6/15.
 */
public class HomeScreen extends AppCompatActivity {

    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final String IMAGE_DIRECTORY_NAME = "AppImages";
    private Uri fileUri; // file url to store image/video

    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    private Button btnCapturePicture, btnExplorePictures;
    private TextView txtUsername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        preferences = getApplicationContext().getSharedPreferences("user_info", 0);
        editor = preferences.edit();

        txtUsername = (TextView) findViewById(R.id.hiUser);
        txtUsername.setText("Hi "+preferences.getString(getResources().getString(R.string.userName),
                "Username"));

        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });


        btnExplorePictures = (Button) findViewById(R.id.explorePictures);
        btnExplorePictures.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // explore picture
                exploreImages();

            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device does't have camera
            finish();
        }
    }

    /**
     * Checking device has camera hardware or not
     */

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_splashscreen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                Fragment fragment = new ChangeNameFragment();
                replaceFragments(fragment, "changeName");
                break;
            case R.id.action_logout:
                editor.putBoolean(getResources().getString(R.string.isLoggedIn), false);
                editor.apply();
                Intent intent = new Intent(HomeScreen.this, LoginScreen.class);
                startActivity(intent);
                finish();
                break;

            default:
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {

        Fragment explorefrag = getFragmentManager().findFragmentByTag("exploreImages");
        Fragment savefrag = getFragmentManager().findFragmentByTag("saveImage");
        Fragment changename = getFragmentManager().findFragmentByTag("changeName");



        if((explorefrag != null && explorefrag.isVisible()) || (savefrag != null && savefrag.isVisible())
                || (changename != null && changename.isVisible())) {
            getFragmentManager().popBackStack();
            txtUsername.setText("Hi "+preferences.getString(getResources().getString(R.string.userName),
                    "Username"));
            //changeDrawerUpIndicator(true);
            //super.onBackPressed();
        }
        else {
            //getFragmentManager().popBackStack();
        }
    }

    /**
     * Here we store the file url as it will be null after returning from camera
    * app
    */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");


    }

    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Capturing Camera Image will lauch camera app requrest image capture
     */
    private void captureImage() {
        Log.e("xx","saving image");
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);
        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Receiving activity result method will be called after closing the camera
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image , display it in image view

                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }


    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {

        Bundle bundle = new Bundle();
        bundle.putString("fileUri", fileUri.getPath());
        Fragment fragment = new SaveImageFragment();
        fragment.setArguments(bundle);
        replaceFragments(fragment, "saveImage");

    }

    /**
     * Explore pictures
     */
    private void exploreImages() {
        Fragment fragment = new ExploreImagesFragment();
        replaceFragments(fragment, "exploreImages");
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

     /**
     * Common logic for replacing fragments
     */
    public void replaceFragments(Fragment newFragment, String tag)
    {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.mainContent, newFragment, tag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }


    /**
     * Creating file uri to store image
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image
     */
    private File getOutputMediaFile(int type) {
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");

        Log.e("full path",mediaFile.toString());


        } else {
            return null;
        }

        return mediaFile;
    }




}