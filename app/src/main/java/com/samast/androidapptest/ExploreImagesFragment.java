package com.samast.androidapptest;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by ht on 28/6/15.
 */
public class ExploreImagesFragment extends Fragment {

    private static final String IMAGE_DIRECTORY_NAME = "AppImages";

    private View view;
    private GridView gridView;
    private GridViewAdapter customGridAdapter;


    public ExploreImagesFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_explore_images, container, false);

        gridView = (GridView) view.findViewById(R.id.gridView);
        customGridAdapter = new GridViewAdapter(getActivity(), R.layout.row_grid, getData());
        gridView.setAdapter(customGridAdapter);

        return view;
    }

    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<ImageItem>();

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);
        File[] imageFiles = mediaStorageDir.listFiles();

        // bimatp factory
        BitmapFactory.Options options = new BitmapFactory.Options();
        String imageName, finalImageName;

        for (int i = 0; i < imageFiles.length; i++) {
            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
            imageName = imageFiles[i].getName();
            finalImageName = imageName.substring(0, imageName.lastIndexOf('.'));

            Bitmap bitmap = BitmapFactory.decodeFile(imageFiles[i].getPath(),options);
            imageItems.add(new ImageItem(bitmap, finalImageName));
        }

        return imageItems;}
}
