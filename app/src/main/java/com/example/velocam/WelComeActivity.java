package com.example.velocam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.velocam.Model.User;
import com.example.velocam.fragment.ChangePasswordFragment;
import com.example.velocam.fragment.Dashboard;
import com.example.velocam.fragment.EditProfileFragment;
import com.example.velocam.fragment.ViewUserFragment;
import com.example.velocam.preference.UserPrefs;
import com.example.velocam.rest.ApiClient;
import com.example.velocam.rest.ApiInterface;
import com.example.velocam.utils.Config;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WelComeActivity extends AppCompatActivity implements
        NavigationView.OnNavigationItemSelectedListener {
    TextView tvName, tvEmail;
    ImageView imgProfile;
    private String id;
    LinearLayout llDrawerHeader;

    private Toolbar toolbar;
    private NavigationView mDrawer;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private int mSelectedId;
    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        init();
        bindView();
        setToolbar();
        getUser();

        drawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar,
                R.string.drawer_open, R.string.drawer_close);
        mDrawerLayout.setDrawerListener(drawerToggle);
        drawerToggle.syncState();
        // default it set first item as selected
        mSelectedId = savedInstanceState == null ? R.id.navigation_item_view_user
                : savedInstanceState.getInt("SELECTED_ID");
        itemSelection(mSelectedId);

    }

    private void init() {

        Intent intent = getIntent();
        id = intent.getStringExtra(Config.TAG_PHONE);
        UserPrefs userPrefs = new UserPrefs(getApplicationContext());

        if (id == null) {
            id = userPrefs.getUserPhone();
        }
        mDrawer = (NavigationView) findViewById(R.id.main_drawer);
        mDrawer.setNavigationItemSelectedListener(this);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

    }
    private void bindView() {
        View header = mDrawer.getHeaderView(0);
        llDrawerHeader = (LinearLayout) header.findViewById(R.id.llDrawerHeader);
        tvName = (TextView) header.findViewById(R.id.tvUserName);
        tvEmail = (TextView) header.findViewById(R.id.tvUserEmail);
        imgProfile = (ImageView) header.findViewById(R.id.imgUser);

    }

    private void setToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        if (toolbar != null) {
            setSupportActionBar(toolbar);
        }
    }
    private void itemSelection(int mSelectedId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction();
        switch (mSelectedId) {
          case R.id.navigation_dashboard:
          mDrawerLayout.closeDrawer(GravityCompat.START);
          Dashboard dashboard = new Dashboard();
          fragmentTransaction.replace(R.id.main_content, dashboard);
          fragmentTransaction.commit();
          break;
            case R.id.navigation_item_view_user:
                ViewUserFragment viewUser = new ViewUserFragment();
                fragmentTransaction.replace(R.id.main_content, viewUser);
                fragmentTransaction.commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.navigation_item_edit:
                EditProfileFragment editProfile = new EditProfileFragment();
                fragmentTransaction.replace(R.id.main_content, editProfile);
                fragmentTransaction.commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.navigation_item_change_password:
                ChangePasswordFragment changePassword = new ChangePasswordFragment();
                fragmentTransaction.replace(R.id.main_content, changePassword);
                fragmentTransaction.commit();
                mDrawerLayout.closeDrawer(GravityCompat.START);
                break;

            case R.id.navigation_item_logout:
                mDrawerLayout.closeDrawer(GravityCompat.START);
                Intent i = new Intent(WelComeActivity.this, LoginActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(i);
                break;

        }

    }

    private void getUser() {

        //Creating an object of our api interface
        ApiInterface api = ApiClient.getApiService();

        /**
         * Calling JSON
         */

        Call<User> call = api.getUserById(id);

        call.enqueue(new Callback<User>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(@NotNull Call<User> call, @NotNull Response<User> response) {
                if (response.isSuccessful()) {
                    Config.currentUser=response.body();
                    assert Config.currentUser != null;
                    tvName.setText(Config.currentUser.getFirst_name()+""+Config.currentUser.getLast_name());
                    tvEmail.setText(Config.currentUser.getEmail());
                    assert response.body() != null;
                    Picasso.with(getApplicationContext())
                            .load(Config.URL_IMAGES + response.body().getImage())
                            .memoryPolicy(MemoryPolicy.NO_CACHE)
                            .networkPolicy(NetworkPolicy.NO_CACHE).into(imgProfile);

                    UserPrefs userPrefs = new UserPrefs(getApplicationContext());
                    if (id != null) {
                        userPrefs.setUserPhone(id);
                    }

                    userPrefs.setUserFirstName(response.body().getFirst_name());
                    userPrefs.setUserLastName(response.body().getLast_name());
                    userPrefs.setUserDesignation(response.body().getDesignation());
                    userPrefs.setUserEmail(response.body().getEmail());
                    userPrefs.setUserImage(response.body().getImage());
                    userPrefs.setUserPassword(response.body().getPassword());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onNavigationItemSelected(@NotNull MenuItem menuItem) {
        menuItem.setChecked(true);
        mSelectedId = menuItem.getItemId();
        itemSelection(mSelectedId);
        return true;
    }


    @Override
    public void onSaveInstanceState(@NotNull Bundle outState,
                                    PersistableBundle outPersistentState) {
        outState.putInt("SELECTED_ID", mSelectedId);
    }



}