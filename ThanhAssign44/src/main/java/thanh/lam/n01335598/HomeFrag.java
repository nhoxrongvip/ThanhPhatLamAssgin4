package thanh.lam.n01335598;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HomeFrag extends Fragment {
    TextView currentDate;
    public TextView currentHour;
    ConstraintLayout homeBackground;
    SharedPreferences preferences,preferences_color;
    String format="",color_name="";
    int color;
    Context c;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        c = container.getContext();

        //Get ID
        currentDate = (TextView) v.findViewById(R.id.ThanhTVCurrentDate);
        currentHour = (TextView) v.findViewById(R.id.ThanhTCCurrentHour);
        homeBackground = v.findViewById(R.id.ThanhHomeBackground);

        preferences = this.getActivity().getApplication().getSharedPreferences("HourFormat", Context.MODE_PRIVATE);
        format = preferences.getString("format","24h");

        preferences_color = this.getActivity().getApplication().getSharedPreferences("BackgroundColor",Context.MODE_PRIVATE);
        color = preferences_color.getInt("color",getResources().getColor(R.color.bright_green));
        color_name = preferences_color.getString("color_name","GREEN");
        getColor(color_name);
        homeBackground.setBackgroundColor(color);





        //Set Current Date
        currentDate.setText(getCurrentDate());

        //Set current Time
        Date hour = Calendar.getInstance().getTime();
        changeFormat(format);
        currentHour.setText(hour.toString());




        return v;
    }


    private String getCurrentDate() {
        //Format: day-month-year
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdf.format(new Date());
        return date;
    }

    private void changeFormat(String time){
        if(time.equals("12-hour Format")){
            final Handler update = new Handler(Looper.getMainLooper());
            update.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentHour.setText(new SimpleDateFormat("hh:mm:ss a", Locale.getDefault()).format(new Date()));
                    update.postDelayed(this,10);
                }
            },10);
        }
        else if(time.equals("24-hour Format")){
            final Handler update = new Handler(Looper.getMainLooper());
            update.postDelayed(new Runnable() {
                @Override
                public void run() {
                    currentHour.setText(new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date()));
                    update.postDelayed(this,10);
                }
            },10);
        }


    }
    private void getColor(String color_name){
        if(color_name.equals("GREEN")){
            final Handler update = new Handler(Looper.getMainLooper());
            update.postDelayed(new Runnable() {
                @Override
                public void run() {
                    homeBackground.setBackgroundColor(ContextCompat.getColor(c,R.color.bright_green));
                    update.postDelayed(this,10);
                }
            },10);
        }
        else if(color_name.equals("BLUE")){
            final Handler update = new Handler(Looper.getMainLooper());
            update.postDelayed(new Runnable() {
                @Override
                public void run() {
                    homeBackground.setBackgroundColor(Color.BLUE);
                    update.postDelayed(this,10);
                }
            },10);
        }
        else if(color_name.equals("BLACK")){
            final Handler update = new Handler(Looper.getMainLooper());
            update.postDelayed(new Runnable() {
                @Override
                public void run() {
                    homeBackground.setBackgroundColor(Color.GRAY);
                    update.postDelayed(this,10);
                }
            },10);
        }

    }





}
