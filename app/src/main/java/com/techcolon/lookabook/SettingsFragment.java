package com.techcolon.lookabook;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import java.util.ArrayList;


public class SettingsFragment extends Fragment {

    public int currTheme;
    AutoCompleteTextView themeAutoComplete;

    public SettingsFragment() {
        // Required empty public constructor
    }


    public static SettingsFragment newInstance(String param1, String param2) {
        SettingsFragment fragment = new SettingsFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = null;
        v = inflater.inflate(R.layout.fragment_settings, container, false);


        themeAutoComplete = v.findViewById(R.id.themeautocomplete);


        ArrayList<String> themes = new ArrayList<>();
        themes.add("System Default");
        themes.add("Light");
        themes.add("Dark");
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_dropdown_item, themes);


        SharedPreferences prefs = getActivity().getSharedPreferences("LookABook_Storage", Context.MODE_PRIVATE);
        int theme = prefs.getInt("Theme", 0);

        themeAutoComplete.setText(themes.get(theme), false);
        themeAutoComplete.setAdapter(adapter);

        themeAutoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

//                    getActivity().finish();
//                    startActivity(new Intent(getActivity(), MainActivity.class));


                currTheme = i;


                SharedPreferences.Editor editor = getActivity().getSharedPreferences("LookABook_Storage", Context.MODE_PRIVATE).edit();
                editor.putInt("Theme", i);
                editor.apply();


                getActivity().recreate();
                Navigation.findNavController(container).popBackStack(R.id.settingsFragment, true);
                Navigation.findNavController(container).navigate(R.id.bookListFragment);

            }
        });

        return v;

    }
}