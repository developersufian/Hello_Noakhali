package com.ncw.hellonoakhali;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.ncw.hellonoakhali.fragment.BloodDonarFragment;
import com.ncw.hellonoakhali.fragment.BloodRequestFragment;
import com.ncw.hellonoakhali.fragment.HomeFragment;
import com.ncw.hellonoakhali.fragment.NewsFragment;
import com.ncw.hellonoakhali.fragment.ProfileFragment;

import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SmoothBottomBar smoothBottomBar;
    Toolbar toolbar;
    DuoDrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Set up the Toolbar
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout =  findViewById(R.id.drawer_layout);
        smoothBottomBar = findViewById(R.id.bottom_navigation);


        DuoDrawerToggle drawerToggle = new DuoDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        drawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();

        View contentView = drawerLayout.getContentView();
        View menuView = drawerLayout.getMenuView();

        LinearLayout ll_Home = menuView.findViewById(R.id.ll_Home);
        LinearLayout ll_Profile = menuView.findViewById(R.id.ll_Profile);
        LinearLayout ll_Setting = menuView.findViewById(R.id.ll_Setting);
        LinearLayout ll_Share = menuView.findViewById(R.id.ll_Share);
        LinearLayout ll_Logout = menuView.findViewById(R.id.ll_Logout);


        ll_Home.setOnClickListener(this);
        ll_Profile.setOnClickListener(this);
        ll_Setting.setOnClickListener(this);
        ll_Share.setOnClickListener(this);
        ll_Logout.setOnClickListener(this);

// Bottom Navigation
        replace(new HomeFragment(), "Home");

        // Set up SmoothBottomBar item selection listener
        smoothBottomBar.setOnItemSelectedListener((OnItemSelectedListener) position -> {

            switch (position) {
                case 0:
                    replace(new HomeFragment(), "Home");
                    toolbar.setTitle(R.string.app_name);
                    break;
                case 1:
                    replace( new NewsFragment(), "News");
                    toolbar.setTitle("News");
                    break;
                case 2:
                    replace( new BloodRequestFragment(), "Blood Request");
                    toolbar.setTitle("Blood Request");
                    break;
                case 3:
                     replace( new BloodDonarFragment(), "Blood Donar");
                     toolbar.setTitle("Blood Donar");
                    break;
            }
            return true;
        });


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ll_Home) {
            replace(new HomeFragment(), "Home");
        } else if (view.getId() == R.id.ll_Profile) {
            replace(new ProfileFragment(), "Profile");
        } else if (view.getId() == R.id.ll_Setting) {
            replace(new NewsFragment(), "News");
        } else if (view.getId() == R.id.ll_Share) {
            Toast.makeText(this, "Share...", Toast.LENGTH_SHORT).show();
        } else if (view.getId() == R.id.ll_Logout) {
            Toast.makeText(this, "Logout...", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawer();
    }


    private void replace(Fragment fragment, String s) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container,fragment);
        transaction.addToBackStack(s);
        transaction.commit();
    }


}
