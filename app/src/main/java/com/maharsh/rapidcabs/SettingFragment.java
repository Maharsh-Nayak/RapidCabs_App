package com.maharsh.rapidcabs;

import static android.content.Context.MODE_PRIVATE;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class SettingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_setting, container, false);
        Button lgt = root.findViewById(R.id.btn_logout);

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
        String UserName = sharedPreferences.getString("UserName", "");
        String UserPhone = sharedPreferences.getString("UserPhone", "");

        TextView un, up;
        un = root.findViewById(R.id.SettingsUserName);
        up = root.findViewById(R.id.SettingUserPhone);

        un.setText(UserName);
        up.setText(UserPhone);

        lgt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sh = getActivity().getSharedPreferences("MyAppPrefs", MODE_PRIVATE);
                SharedPreferences.Editor myEdit = sh.edit();
                myEdit.putBoolean("isLoggedIn", false);
                myEdit.putString("UserName", "");
                myEdit.putString("UserPhone", "");
                myEdit.apply();
                Log.d("Logout", "details removed");
                Intent intent = new Intent(getActivity(), MainActivity.class);
                Log.d("Logout", "Leaving");
                startActivity(intent);
            }
        });

        Button abt = root.findViewById(R.id.btn_about);
        abt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AboutDialog aboutDialog = new AboutDialog();
                aboutDialog.show(getParentFragmentManager(), "about_dialog");
            }
        });

        // Inflate the layout for this fragment
        return root;

    }
}