package com.ncw.hellonoakhali;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.window.OnBackInvokedDispatcher;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.ncw.hellonoakhali.fragment.BloodDonorFragment;
import com.ncw.hellonoakhali.fragment.BloodRequestFragment;
import com.ncw.hellonoakhali.fragment.HomeFragment;
import com.ncw.hellonoakhali.fragment.NewsFragment;
import com.ncw.hellonoakhali.fragment.ProfileFragment;
import com.ncw.hellonoakhali.fragment.SettingsFragment;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;

public class MainActivity extends AppCompatActivity {

    // UI Elements
    private SmoothBottomBar smoothBottomBar;
    private ImageView img_drawer;

    private TextView name, email;
    private LinearLayout menu_header;
    private CircleImageView picture;
    private Button btn_login;

    // Firebase
    private FirebaseAuth mAuth;
    private FirebaseDatabase database;
    private DatabaseReference usersRef;
    private FirebaseUser currentUser;


    // Google Sign-In
    private static final int REQ_ONE_TAP = 2;
    private SignInClient oneTapClient;
    private BeginSignInRequest signInRequest;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        drawerLayout = findViewById(R.id.drawer_layout);
        img_drawer = findViewById(R.id.img_drawer);
        smoothBottomBar = findViewById(R.id.bottom_navigation);
        navigationView = findViewById(R.id.nav_view);

        img_drawer.setOnClickListener(v -> toggleDrawer());


        setupDrawerMenu();
        setupFirebase();
        setupGoogleSignIn();
        setupBottomNavigation();

        // Load HomeFragment by default
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment());
        }

        // Drawer item click listener
        navigationView.setNavigationItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_home) {
                replaceFragment(new HomeFragment());
            } else if (item.getItemId() == R.id.nav_profile) {
                replaceFragment(new ProfileFragment());
            } else if (item.getItemId() == R.id.nav_settings) {
                replaceFragment(new SettingsFragment());
            } else if (item.getItemId() == R.id.nav_logout) {
                logoutUser();
            }
            drawerLayout.closeDrawer(GravityCompat.START);  // Close drawer after item click
            return true;
        });
    }


    // Setup Firebase Authentication and Database
    private void setupFirebase() {
        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("users");

        currentUser = mAuth.getCurrentUser();
        updateUI(currentUser);
    }

    // Configure Google One-Tap Sign-In
    private void setupGoogleSignIn() {
        oneTapClient = Identity.getSignInClient(this);
        signInRequest = BeginSignInRequest.builder().setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder().setSupported(true).setServerClientId(getString(R.string.default_web_client_id)).setFilterByAuthorizedAccounts(false).build()).setAutoSelectEnabled(false).build();

        // Ensure this is only done after btn_login is initialized
        if (btn_login != null) {
            btn_login.setOnClickListener(v -> startGoogleSignIn());
        } else {
            Log.e(TAG, "btn_login is not initialized");
        }
    }

    // Setup Drawer Menu and Click Listeners
    private void setupDrawerMenu() {
        View headerView = navigationView.getHeaderView(0);
        menu_header = headerView.findViewById(R.id.menu_header);
        name = headerView.findViewById(R.id.name);
        email = headerView.findViewById(R.id.email);
        picture = headerView.findViewById(R.id.picture);
        btn_login = headerView.findViewById(R.id.btn_login);

        // Ensure that btn_login is initialized
        if (btn_login != null) {
            btn_login.setOnClickListener(v -> startGoogleSignIn());
        } else {
            Log.e(TAG, "btn_login is null in the drawer menu header");
        }
    }

    // Setup Bottom Navigation Bar
    private void setupBottomNavigation() {
        smoothBottomBar.setOnItemSelectedListener((OnItemSelectedListener) position -> {
            switch (position) {
                case 0:
                    replaceFragment(new HomeFragment());
                    break;
                case 1:
                    replaceFragment(new NewsFragment());
                    break;
                case 2:
                    replaceFragment(new BloodRequestFragment());
                    break;
                case 3:
                    replaceFragment(new BloodDonorFragment());
                    break;
            }
            return true;
        });
    }

    // Update UI based on Firebase User
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Log.d(TAG, "User is logged in: " + user.getDisplayName());

            btn_login.setVisibility(View.GONE);
            menu_header.setVisibility(View.VISIBLE);
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).into(picture);

        } else {
            Log.d(TAG, "User is not logged in.");
            if (btn_login != null) {
                btn_login.setVisibility(View.VISIBLE);
            }
            if (menu_header != null) {
                menu_header.setVisibility(View.GONE);
            }
        }
    }

    // Drawer Toggle
    private void toggleDrawer() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            drawerLayout.openDrawer(GravityCompat.START);
        }
    }

    // Replace Fragments
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    // Log out the user
    private void logoutUser() {
        mAuth.signOut();
        updateUI(null);
        Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show();
    }

    // Start Google One-Tap Sign-In
    private void startGoogleSignIn() {
        oneTapClient.beginSignIn(signInRequest).addOnSuccessListener(this, result -> {
            try {
                startIntentSenderForResult(result.getPendingIntent().getIntentSender(), REQ_ONE_TAP, null, 0, 0, 0);
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "Couldn't start One Tap UI: " + e.getLocalizedMessage());
            }
        }).addOnFailureListener(this, e -> Log.e(TAG, "beginSignIn:onFailure", e));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQ_ONE_TAP && data != null) {
            handleSignInResult(data);
        }
    }

    // Handle Google Sign-In Result
    private void handleSignInResult(Intent data) {
        try {
            SignInCredential credential = oneTapClient.getSignInCredentialFromIntent(data);
            String idToken = credential.getGoogleIdToken();
            if (idToken != null) {
                AuthCredential firebaseCredential = GoogleAuthProvider.getCredential(idToken, null);
                mAuth.signInWithCredential(firebaseCredential)
                        .addOnCompleteListener(this, task -> {
                            if (task.isSuccessful()) {
                                updateUI(mAuth.getCurrentUser());  // Ensure you're passing the updated FirebaseUser
                            } else {
                                Log.w(TAG, "signInWithCredential:failure", task.getException());
                            }
                        });
            }
        } catch (ApiException e) {
            Log.e(TAG, "Google Sign-In failed: " + e.getLocalizedMessage());
        }
    }
}
