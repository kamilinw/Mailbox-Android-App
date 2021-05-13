package com.example.mailbox.ui.mailbox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mailbox.R;
import com.example.mailbox.data.UserDatabase;
import com.example.mailbox.databinding.ActivityMailboxBinding;
import com.example.mailbox.ui.main.MainActivity;
import com.example.mailbox.util.UserUtil;
import com.google.android.material.navigation.NavigationView;

import org.jetbrains.annotations.NotNull;


public class MailboxActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMailboxBinding binding;
    DrawerLayout drawer;
    String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMailboxBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar = binding.appBar.toolbar;
        setSupportActionBar(toolbar);

        drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;

        // set username and email on drawer header
        View view = navigationView.getHeaderView(0);
        TextView usernameTextView = view.findViewById(R.id.usernameTextView);
        TextView emailTextView = view.findViewById(R.id.emailTextView);
        UserDatabase userDatabase = UserDatabase.getInstance(getApplicationContext());
        usernameTextView.setText(userDatabase.getUsername());
        emailTextView.setText(userDatabase.getEmail());
        userDatabase.close();

        // add drawer toggle to bar
        ActionBarDrawerToggle toggle =
                new ActionBarDrawerToggle(
                        this,
                        drawer,
                        toolbar,
                        R.string.navigation_drawer_open,
                        R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        // Start home fragment
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,new HomeFragment())
                .commit();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new HomeFragment())
                        .commit();
                        break;
            case R.id.nav_profile:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new ProfileFragment())
                        .commit();
                break;
            case R.id.nav_about:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.fragment_container,new AboutFragment())
                        .commit();
                break;
            case R.id.nav_logout:
                //TODO make confirm dialog
                Toast.makeText(getApplicationContext(), "Logging out", Toast.LENGTH_SHORT).show();
                UserUtil.logoutUser(getApplicationContext());
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


}