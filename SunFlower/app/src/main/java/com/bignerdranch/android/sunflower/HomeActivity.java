package com.bignerdranch.android.sunflower;

import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;

import com.bignerdranch.android.sunflower.helper.DBHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class HomeActivity extends AppCompatActivity {
    public static SQLiteOpenHelper dbHelper;

//    private MusicService musicService;

//    DBHelper dbHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        dbHelper=new DBHelper(this);
        BottomNavigationView navView = findViewById(R.id.nav_view);
//        navView.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_recommend, R.id.navigation_see, R.id.navigation_listen,R.id.navigation_me)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

//    @Override
//    public void onFragmentInteraction(Uri uri) {
//
//    }

//    public void executeDownloadUtil(DownloadUtils util) {
//        Toast.makeText(this, "正在下载", Toast.LENGTH_LONG)
//                .show();
//        System.out.println("111111111111111111111111111"+util);
//        musicService.downloadMusic(util);
//    }

}