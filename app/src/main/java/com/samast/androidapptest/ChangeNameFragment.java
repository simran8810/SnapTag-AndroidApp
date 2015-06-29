package com.samast.androidapptest;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;

/**
 * Created by ht on 28/6/15.
 */
public class ChangeNameFragment extends Fragment {

    private View view;
    private Button btnSaveName;
    private EditText newName;
    private static SharedPreferences.Editor editor;

    public ChangeNameFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_change_name, container, false);

        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("user_info", 0);
        editor = preferences.edit();

        newName = (EditText) view.findViewById(R.id.newName);
        btnSaveName = (Button) view.findViewById(R.id.newNameBtn);
        btnSaveName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                editor.putString(getResources().getString(R.string.userName),
                        String.valueOf(newName.getText()));
                editor.apply();
                getFragmentManager().popBackStack();


            }
        });
        return view;
    }

}
