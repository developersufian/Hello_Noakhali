package com.ncw.hellonoakhali;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
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
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.common.api.ApiException;
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

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.ibrahimsn.lib.OnItemSelectedListener;
import me.ibrahimsn.lib.SmoothBottomBar;
import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    // UI Elements
    private SmoothBottomBar smoothBottomBar;
    private ImageView img_drawer;
    private DuoDrawerLayout drawerLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeUI();
        setupFirebase();
        setupGoogleSignIn();
        setupDrawerMenu();
        setupBottomNavigation();

        // Load HomeFragment by default
        replaceFragment(new HomeFragment(), "Home");



    }

    // Initialize UI components
    private void initializeUI() {
        drawerLayout = findViewById(R.id.drawer_layout);
        img_drawer = findViewById(R.id.img_drawer);
        smoothBottomBar = findViewById(R.id.bottom_navigation);

        View menuView = drawerLayout.getMenuView();
        name = menuView.findViewById(R.id.name);
        email = menuView.findViewById(R.id.email);
        picture = menuView.findViewById(R.id.picture);
        btn_login = menuView.findViewById(R.id.btn_login);
        menu_header = menuView.findViewById(R.id.menu_header);

        img_drawer.setOnClickListener(v -> toggleDrawer());
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
        signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                        .setSupported(true)
                        .setServerClientId(getString(R.string.default_web_client_id))
                        .setFilterByAuthorizedAccounts(false)
                        .build())
                .setAutoSelectEnabled(false)
                .build();

        btn_login.setOnClickListener(v -> startGoogleSignIn());
    }

    // Setup Drawer Menu and Click Listeners
    private void setupDrawerMenu() {
        View menuView = drawerLayout.getMenuView();

        menuView.findViewById(R.id.ll_Home).setOnClickListener(this);
        menuView.findViewById(R.id.ll_Profile).setOnClickListener(this);
        menuView.findViewById(R.id.ll_Setting).setOnClickListener(this);
        menuView.findViewById(R.id.ll_Share).setOnClickListener(this);
        menuView.findViewById(R.id.ll_Logout).setOnClickListener(this);
    }

    // Setup Bottom Navigation Bar
    private void setupBottomNavigation() {
        smoothBottomBar.setOnItemSelectedListener((OnItemSelectedListener) position -> {
            switch (position) {
                case 0:
                    replaceFragment(new HomeFragment(), "Home");
                    break;
                case 1:
                    replaceFragment(new NewsFragment(), "News");
                    break;
                case 2:
                    replaceFragment(new BloodRequestFragment(), "Blood Request");
                    break;
                case 3:
                    replaceFragment(new BloodDonorFragment(), "Blood Donor");
                    break;
            }
            return true;
        });
    }

    // Update UI based on Firebase User
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            btn_login.setVisibility(View.GONE);
            menu_header.setVisibility(View.VISIBLE);
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            Glide.with(this).load(user.getPhotoUrl()).into(picture);


        } else {
            btn_login.setVisibility(View.VISIBLE);
            menu_header.setVisibility(View.GONE);
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
    private void replaceFragment(Fragment fragment, String tag) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    // Handle Drawer Menu Clicks


    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.ll_Home) {
            replaceFragment(new HomeFragment(), "Home");
        } else if (id == R.id.ll_Profile) {
            replaceFragment(new ProfileFragment(), "Profile");
        } else if (id == R.id.ll_Setting) {
            replaceFragment(new NewsFragment(), "News");
        } else if (id == R.id.ll_Share) {
            Toast.makeText(this, "Share...", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.ll_Logout) {
            logoutUser();

        }
        drawerLayout.closeDrawer();
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
                mAuth.signInWithCredential(firebaseCredential).addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        updateUI(mAuth.getCurrentUser());
                    } else {
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                    }
                });
            }
        } catch (ApiException e) {
            Log.e(TAG, "Sign-in error: " + e.getLocalizedMessage());
        }
    }


}
