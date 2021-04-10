package thanh.lam.n01335598;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.SwitchPreference;


public class SettingFrag extends Fragment {

    RadioButton format12hour, format24hour, background_green, background_blue, background_black;
    RadioGroup hourFormatGroup, background_color;
    SharedPreferences preferences, preferences_color;
    Button save;
    String hourFormat, color_name;
    int background;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_settings, container, false);
        //Get Id
        format12hour = v.findViewById(R.id.ThanhFormat12h);
        format24hour = v.findViewById(R.id.ThanhFormat24h);
        hourFormatGroup = v.findViewById(R.id.ThanhHourRBGRoup);
        background_color = v.findViewById(R.id.ThanhBackgroundColor);
        background_green = v.findViewById(R.id.ThanhBackGroundWhite);
        background_blue = v.findViewById(R.id.ThanhBackGroundBlue);
        background_black = v.findViewById(R.id.ThanhBackGroundBlack);
        save = v.findViewById(R.id.ThanhSaveButton);


        preferences = this.getActivity().getSharedPreferences("HourFormat", Context.MODE_PRIVATE);
        //Hour Format Preference
        hourFormat = preferences.getString("format", format24hour.getText().toString());//Default value is 24 hour format
        if (hourFormatGroup.getCheckedRadioButtonId() == -1) {
            if (hourFormat.equals(format12hour.getText().toString())) {
                format12hour.setChecked(true);

            } else if (hourFormat.equals(format24hour.getText().toString())) {
                format24hour.setChecked(true);
            }

        }


        //Background color Preference
        preferences_color = this.getActivity().getSharedPreferences("BackgroundColor", Context.MODE_PRIVATE);
        background = preferences_color.getInt("color", getResources().getColor(R.color.bright_green));
        color_name = preferences_color.getString("color_name", "GREEN");
        //Save the checked
        if (background_color.getCheckedRadioButtonId() == -1) {
            if (color_name.equals(background_green.getText().toString())) {
                background_green.setChecked(true);
            } else if (color_name.equals(background_blue.getText().toString())) {
                background_blue.setChecked(true);
            }
            if (color_name.equals(background_black.getText().toString())) {
                background_black.setChecked(true);
            }
        }


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendHourFormat();
                sendBackgroundColor();
            }
        });

        return v;

    }

    private void sendHourFormat() {
        String format = "";
        SharedPreferences.Editor edit = preferences.edit();
        if (format12hour.isChecked()) {
            format = format12hour.getText().toString();

        } else if (format24hour.isChecked()) {
            format = format24hour.getText().toString();

        }
        edit.putString("format", format);
        edit.apply();
        Toast.makeText(getActivity(), "Setting Saved", Toast.LENGTH_SHORT).show();

    }

    private void sendBackgroundColor() {
        String color_name = "";
        int color = getResources().getColor(R.color.bright_green);
        SharedPreferences.Editor editor = preferences_color.edit();
        if (background_green.isChecked()) {
            color_name = background_green.getText().toString();
            color = getResources().getColor(R.color.bright_green);
        } else if (background_blue.isChecked()) {
            color_name = background_blue.getText().toString();
            color = Color.BLUE;
        } else if (background_black.isChecked()) {
            color_name = background_black.getText().toString();
            color = Color.GRAY;
        }
        editor.putString("color_name", color_name);
        editor.putInt("color", color);
        editor.apply();
    }


}