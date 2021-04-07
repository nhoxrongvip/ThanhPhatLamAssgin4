package thanh.lam.n01335598;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextClock;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFrag extends Fragment {
    TextView currentDate;
    public static TextClock currentHour;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);

        //Get ID
        currentDate = (TextView) v.findViewById(R.id.ThanhTVCurrentDate);
        currentHour = (TextClock) v.findViewById(R.id.ThanhTCCurrentHour);

        //Set Current Date
        currentDate.setText(getCurrentDate());

        //Set current Time
        currentHour.setFormat12Hour("hh:mm:ss a");



        return v;
    }


    private String getCurrentDate() {
        //Format: day-month-year
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
        String date = sdf.format(new Date());
        return date;


    }


}
