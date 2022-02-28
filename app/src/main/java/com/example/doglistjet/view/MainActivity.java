package com.example.doglistjet.view;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.doglistjet.R;

public class MainActivity extends AppCompatActivity {

    private NavController navController;
    private  static  final  int PERMISSION_SEND_SMS= 555;
    private Fragment fragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navController = Navigation.findNavController(this, R.id.fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);
        fragment =getSupportFragmentManager().findFragmentById(R.id.fragment);

    }

    @Override
    public boolean onSupportNavigateUp() {

        return NavigationUI.navigateUp(navController, (DrawerLayout) null);

    }


    public void checkSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)) {
                new AlertDialog.Builder(this).setTitle("Send Sms Permission")
                        .setMessage("This app requires access to the send an SMS")
                        .setPositiveButton("Ask me", (dialog, which) -> {
                            requestSmsPermission();
                        })
                        .setNegativeButton("No", (dialog, which) -> {

                            notifyDetailFragment(false);
                        })
                        .show();
            } else {

                requestSmsPermission();
            }

        } else {

            notifyDetailFragment(true);
        }

    }

    private void requestSmsPermission() {

        String[] permissions = {Manifest.permission.SEND_SMS};

        ActivityCompat.requestPermissions(this,permissions,PERMISSION_SEND_SMS);
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case  PERMISSION_SEND_SMS:{
                if (grantResults.length>0 && grantResults[0]==PackageManager.PERMISSION_GRANTED){

                    notifyDetailFragment(true);
                }
                else {
                    notifyDetailFragment(false);
                }
                break;
            }
        }

    }

    public void notifyDetailFragment(Boolean permissionGranted) {

        Fragment activeFragment = fragment.getChildFragmentManager().getPrimaryNavigationFragment();
        if (activeFragment instanceof DetailFragment){
            ((DetailFragment)activeFragment).onPermissionResult(permissionGranted);
        }
    }
}
