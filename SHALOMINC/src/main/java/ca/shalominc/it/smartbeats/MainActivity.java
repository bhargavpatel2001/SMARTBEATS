//Bhargav Patel (N01373029) & Ripal Patel (N01354619) & Vidhi Kanhye (N01354573) & Nicholas Mohan (N01361663), Section-RNA

package ca.shalominc.it.smartbeats;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        // Handle item selection
        Intent intent = null;
        switch (item.getItemId())
        {

            case R.id.nicholasHelp:

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.ca"));
                startActivity(intent);

                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }


    //OnBackKeyPressed
    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setTitle("Are you sure ?")
                .setMessage("This will exit the application")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        //Closes the current Activity and cannot be accessed by other pages.
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}