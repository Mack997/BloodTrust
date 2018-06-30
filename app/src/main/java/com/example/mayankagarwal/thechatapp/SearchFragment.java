package com.example.mayankagarwal.thechatapp;


import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class SearchFragment extends Fragment {

    private Button mSearchBtn, openMaps;
    private Spinner bld_grp_spinner;
    private View mMainView;


    public SearchFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mMainView = inflater.inflate(R.layout.fragment_search, container, false);
        mSearchBtn = (Button) mMainView.findViewById(R.id.req_search_btn);
        bld_grp_spinner = (Spinner) mMainView.findViewById(R.id.find_spinner);
        openMaps = (Button) mMainView.findViewById(R.id.open_maps);

        mSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String blood_group = String.valueOf(bld_grp_spinner.getSelectedItem());
                if (TextUtils.isEmpty(blood_group)){

                    Toast.makeText(getContext(), "Please Select Blood Group to proceed", Toast.LENGTH_SHORT).show();
                }else{
                    Search(blood_group);
                }
            }
        });
        openMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent maps = new Intent(getContext(),MapsActivity.class);
                startActivity(maps);
            }
        });


        return mMainView;
    }
    public void Search(final String blood_group){
        Intent searchResults = new Intent(getContext(), DonorResults.class);
        searchResults.putExtra("blood_group", blood_group);
        startActivity(searchResults);
    }
}