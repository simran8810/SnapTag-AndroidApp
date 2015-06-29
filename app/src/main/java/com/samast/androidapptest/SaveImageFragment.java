package com.samast.androidapptest;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by ht on 28/6/15.
 */
public class SaveImageFragment extends Fragment {

    private View view;
    private ImageView imgPreview;
    private ImageButton cancelImage, submitImage;
    private TextView latlong;
    private static Uri imageFileUri; // file uri
    private static String fileUri;
    private GPSTracker gps;

    public SaveImageFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_save_image, container, false);
        imgPreview = (ImageView) view.findViewById(R.id.imgPreview);
        latlong = (TextView) view.findViewById(R.id.txtlatlong);
        fileUri = getArguments().getString("fileUri");
        previewImage();


        submitImage = (ImageButton) view.findViewById(R.id.submitImage);
        submitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });

        cancelImage = (ImageButton) view.findViewById(R.id.cancelImage);
        cancelImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();

            }
        });


        // create class object
        gps = new GPSTracker(getActivity());

        // check if GPS enabled
        if(gps.canGetLocation()){

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            latlong.setText("(Lat, Lon): ("+latitude + ", " + longitude+")");

            Log.e("xxxx", latitude + "   " + longitude);
        }else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

        return view;
    }


    public void previewImage(){

        try {
            imgPreview.setVisibility(View.VISIBLE);
            imageFileUri = Uri.parse(fileUri);
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();
            // downsizing image as it throws OutOfMemory Exception for larger images
            options.inSampleSize = 8;
            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri, options);

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }



    }
}
