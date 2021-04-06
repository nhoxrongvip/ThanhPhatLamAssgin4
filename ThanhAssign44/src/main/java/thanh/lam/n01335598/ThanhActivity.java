//Thanh Phat Lam N01335598 CENG258 RNC
package thanh.lam.n01335598;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;


public class ThanhActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private static final int LOCATION_REQUEST_CODE = 1;
    private DrawerLayout drawer;
    Double longitude, latitude;
    FusedLocationProviderClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        //Get elements id
        drawer = findViewById(R.id.ThanhDrawerLayout);
        NavigationView navView = findViewById(R.id.ThanhNavView);
        Toolbar toolbar = findViewById(R.id.ThanhToolBar);
        MenuItem location = findViewById(R.id.ThanhLocationMenu);

        //Replace default toolbar with your toolbar
        setSupportActionBar(toolbar);

        //Change Fragment when selected
        navView.setNavigationItemSelectedListener(this);
        //Create "3-dash icon"
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        //Set default fragment: Home Fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.ThanhFragContainer, new HomeFrag()).commit();

        //Display Location
        client = LocationServices.getFusedLocationProviderClient(ThanhActivity.this);

    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            new AlertDialog.Builder(this)
                    .setTitle(R.string.app_name)
                    .setCancelable(false)
                    .setMessage("Do you want to quit?")
                    .setPositiveButton("Yes", (dialog, which) -> finish())
                    .setNegativeButton("No", (dialog, which) -> dialog.cancel())
                    .create()
                    .show();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment frag = null;
        switch (item.getItemId()) {
            //Home Fragment
            case R.id.ThanhHomeFrag:
                frag = new HomeFrag();
                break;
            //Download Fragment
            case R.id.ThanhDownloadFrag:
                frag = new DownloadFrag();
                break;
            //Web Service Fragment
            case R.id.ThanhWebServiceFrag:
                frag = new WebServiceFrag();
                break;

        }
        item.setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.ThanhFragContainer, frag).commit();
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ThanhLocationMenu:
                //Check Conditions
                if(ActivityCompat.checkSelfPermission(ThanhActivity.this,
                        android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(ThanhActivity.this,
                        Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                            //When both permission are granted, call the method to get Long and Lat
                            getCurrentLocation();
                }
                else {
                    //When permission is not granted, request permission

                    ActivityCompat.requestPermissions(ThanhActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 100);
                }
                return true;
            default:
                super.onContextItemSelected(item);
                return true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //Check condition
        if(requestCode == 100 && grantResults.length > 0 && (grantResults[0] + grantResults[1]) == PackageManager.PERMISSION_GRANTED){
            getCurrentLocation();

        }else {
            Toast.makeText(getApplicationContext(), "Permission Denied",Toast.LENGTH_SHORT).show();
        }
    }


    @SuppressLint("MissingPermission")
    private void getCurrentLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //Check Condition: GPS_PROVIDER and NETWORK_PROVIDER
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
            //When both permissions are granted, get last location
            client.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location l = task.getResult();
                    if (l != null){
                        //When location is not null, get Latitude and Longitude and display Toast
                        latitude = l.getLatitude();
                        longitude = l.getLongitude();
                        String display = "Latitude:" + latitude + " Longitude:" +longitude;
                        Toast.makeText(ThanhActivity.this, display,Toast.LENGTH_SHORT).show();

                    }
                    else {
                        //Initialize locaiton request
                        LocationRequest locationRequest = new LocationRequest().setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(10000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        //Initialize location call back
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location l1 = locationResult.getLastLocation();
                                latitude = l1.getLatitude();
                                longitude = l1.getLongitude();
                                String display = "Latitude:" + latitude + "\tLongitude:" +longitude;
                                Toast.makeText(ThanhActivity.this, display,Toast.LENGTH_SHORT).show();

                            }
                        };
                        //Request locaiton update
                        client.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                    }
                }
            });

        }else {
            //When location service is not enable, go to location setting
            startActivity(new Intent(Settings.ACTION_LOCALE_SETTINGS).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }


}
