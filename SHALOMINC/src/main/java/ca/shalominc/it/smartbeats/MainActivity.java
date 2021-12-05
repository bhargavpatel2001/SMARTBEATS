//Bhargav Patel (N01373029) & Ripal Patel (N01354619) & Vidhi Kanhye (N01354573) & Nicholas Mohan (N01361663), Section-RNA

package ca.shalominc.it.smartbeats;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity
{

    private int PERMISSION_CODE = 1;
    WifiManager wifiManager;
    int flag = 1;
    Boolean visibleFrag = false;

    Spinner musicSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);



        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_music, R.id.navigation_lights, R.id.navigation_settings, R.id.navigation_review)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        ColorDrawable colorActionBar = new ColorDrawable(ContextCompat.getColor(this,R.color.purple_200));

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(colorActionBar);


    }

    //Creates the Menu bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        if (menu != null)
        {
            menu.findItem(R.id.lightsPwrBtn).setVisible(false);
        }
        return true;
    }

    public boolean isConnected()
    {
        boolean connected = false;
        try
        {
            ConnectivityManager cm = (ConnectivityManager)getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo nInfo = cm.getActiveNetworkInfo();
            connected = nInfo != null && nInfo.isAvailable() && nInfo.isConnected();
            return connected;
        }
        catch (Exception e)
        {
            Log.e("Connectivity Exception", e.getMessage());
        }
        return connected;
    }

    //Menu button configurations
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {

        // Handle item selection
        Intent intent = null;
        musicSpinner = findViewById(R.id.shalom_music_spinner);

        switch (item.getItemId())
        {

            case R.id.helpBtn:

                intent = new Intent(Intent.ACTION_VIEW, Uri.parse(getString(R.string.menu_bar_help_btn_link)));
                startActivity(intent);

                break;

            case R.id.musicBtn:

                //Refactored code to check if user has internet connection enabled in order to display list of songs that can be downloaded.
                if(isConnected())
                {

                    if (visibleFrag)
                    {
                        Toast.makeText(getApplicationContext(), getString(R.string.must_be_in_music_fragment_label), Toast.LENGTH_LONG).show();
                    }
                    else
                    {

                        if (musicSpinner.getVisibility() != View.VISIBLE)
                        {
                            musicSpinner.setVisibility(View.VISIBLE);
                        }
                        else
                        {
                            musicSpinner.setVisibility(View.INVISIBLE);
                        }

                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), getString(R.string.must_enable_an_internet_connection), Toast.LENGTH_LONG).show();
                }

                break;

            case R.id.lightsPwrBtn:

                pwrOutput();

                if(flag == 1){
                    item.setIcon(R.drawable.poweroff);
                }
                if(flag == 2){
                    item.setIcon(R.drawable.poweron);
                }

                break;

            case R.id.bluetoothBtn:

                    requestBluetoothPermission();

                break;

            default:
                return super.onOptionsItemSelected(item);
        }
        return true;

    }

    // On and Off switch inside the menu for power
    void pwrOutput()
    {
       switch (flag)
       {
          case 1:

                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.snackbar_pwr_btn_on, Snackbar.LENGTH_LONG);
                    snackbar.setTextColor(getResources().getColor(R.color.black));
                    snackbar.setBackgroundTint(getResources().getColor(R.color.purple_200));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                    snackbar.show();


                flag++;

                break;

            case 2:

                    snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.snackbar_pwr_btn_off, Snackbar.LENGTH_LONG);
                    snackbar.setTextColor(getResources().getColor(R.color.black));
                    snackbar.setBackgroundTint(getResources().getColor(R.color.purple_200));
                    snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                    snackbar.show();

                    flag = 1;

                    break;
        }
    }

    // Requesting user for Bluetooth Runtime  permissions
    private void requestBluetoothPermission()
    {

            new AlertDialog.Builder(this)
                    .setTitle(R.string.request_perms_alert_dialog_title)
                    .setMessage(R.string.request_perms_alert_dialog_message)
                    .setCancelable(false)
                    .setPositiveButton(R.string.request_perms_alert_dialog_positive_btn, new DialogInterface.OnClickListener()                                         //Turns On the bluetooth
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.bluetooth_enabled_snackbar_message, Snackbar.LENGTH_LONG);
                            snackbar.setTextColor(getResources().getColor(R.color.black));
                            snackbar.setBackgroundTint(getResources().getColor(R.color.purple_200));
                            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                            snackbar.show();

                            requestPermissions(new String[]{Manifest.permission.BLUETOOTH}, PERMISSION_CODE);
                            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_ADMIN}, PERMISSION_CODE);
                            requestPermissions(new String[]{Manifest.permission.BLUETOOTH_CONNECT}, PERMISSION_CODE);

                        }
                    })
                    .setNegativeButton(R.string.request_perms_alert_dialog_negative_btn, new DialogInterface.OnClickListener()                                      //Turns off the bluetooth
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.request_perms_deny_snackbar_message, Snackbar.LENGTH_LONG);
                            snackbar.setTextColor(getResources().getColor(R.color.black));
                            snackbar.setBackgroundTint(getResources().getColor(R.color.purple_200));
                            snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                            snackbar.show();

                            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                            if (mBluetoothAdapter.isEnabled())
                            {
                                mBluetoothAdapter.disable();
                            }

                            dialog.dismiss();
                        }
                    })
                    .create().show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        if(requestCode == PERMISSION_CODE)
        {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

                BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                if (!mBluetoothAdapter.isEnabled())
                {
                    Intent enable_Bluetooth = new Intent(
                            BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enable_Bluetooth, 1);

                }

            }
            else
            {
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), R.string.deny_permission_snackbar_message, Snackbar.LENGTH_LONG);
                snackbar.setTextColor(getResources().getColor(R.color.black));
                snackbar.setBackgroundTint(getResources().getColor(R.color.purple_200));
                snackbar.setAnimationMode(BaseTransientBottomBar.ANIMATION_MODE_FADE);
                snackbar.show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }


    //If BackKey Pressed configuration
    @Override
    public void onBackPressed()
    {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.ic_baseline_warning_24)
                .setTitle(R.string.back_key_pressed_title)
                .setMessage(R.string.back_key_pressed_message)
                .setPositiveButton(R.string.back_key_pressed_positive_btn, new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                        //Closes the current Activity and cannot be accessed by other pages.
                        finish();
                    }
                })
                .setNegativeButton(R.string.back_key_pressed_negative_btn, null)
                .show();
    }
}