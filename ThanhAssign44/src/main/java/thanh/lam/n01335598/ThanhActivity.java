//Thanh Phat Lam N01335598 CENG258 RNC
package thanh.lam.n01335598;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;


import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;


public class ThanhActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.ThanhToolBar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.ThanhDrawerLayout);
        NavigationView navView = findViewById(R.id.ThanhNavView);
        navView.setNavigationItemSelectedListener(this);
        //Create "3-dash icon"
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction().replace(R.id.ThanhFragContainer, new HomeFrag()).commit(); // Default Fragment


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
            case R.id.ThanhHomeFrag:
                frag = new HomeFrag();
                break;
            case R.id.ThanhDownloadFrag:
                frag = new DownloadFrag();

                break;
            case R.id.ThanhWebServiceFrag:
                frag = new WebServiceFrag();
                break;
        }
        item.setChecked(true);
        getSupportFragmentManager().beginTransaction().replace(R.id.ThanhFragContainer, frag).commit();
        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}