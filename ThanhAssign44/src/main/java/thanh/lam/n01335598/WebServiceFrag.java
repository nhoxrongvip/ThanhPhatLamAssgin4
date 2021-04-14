package thanh.lam.n01335598;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

import static com.google.android.gms.internal.zzahn.runOnUiThread;

public class WebServiceFrag extends Fragment implements TextWatcher, View.OnClickListener {
    EditText zipcodeText;
    Button webButton;
    TextView weatherDisplay;
    View view;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_webservice, container, false);
        view = v;


        //Get id
        zipcodeText = (EditText) v.findViewById(R.id.ThanhzipCode);
        webButton = (Button) v.findViewById(R.id.ThanhWebButton);
        weatherDisplay = (TextView) v.findViewById(R.id.ThanhWeatherTV);

        zipcodeText.addTextChangedListener(this);
        webButton.setOnClickListener(this);



        return v;
    }

    void displayAlert(String message) {
        new AlertDialog.Builder(getContext())
                .setIcon(R.drawable.warning)
                .setCancelable(false)
                .setTitle("Zip Code Error")
                .setMessage(message)
                .setNegativeButton("Okay", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        if (zipcodeText.getText().length() < 5) {
            zipcodeText.setError("ZIP Code must be 5 number");

        }
    }

    @Override
    public void onClick(View v) {
        if(zipcodeText.getText().length() == 0){
            displayAlert("Zip code must not be empty");
        }
        else if (zipcodeText.getText().length() < 5){
            displayAlert("Zip code must be 5 number");
        }
        else {
            getWeather();
        }
    }

    private void getWeather(){
        zipcodeText = (EditText) view.findViewById(R.id.ThanhzipCode);
        String zipcode = zipcodeText.getText().toString();
        //I use my api on openweathermap, it only legitimates for 16 days.
        //Uncomment the line below to get the sample JSON
        //String url = "https://samples.openweathermap.org/data/2.5/weather?zip="+zipcode+",us&appid=b6907d289e10d714a6e88b30761fae22";
        String url = "https://api.openweathermap.org/data/2.5/weather?zip="+zipcode+",us&appid=41d654d4863a9b869d397a8d42e6ea8a";

        new ReadJSONFeed().execute(url);
        //more
    }

    private String readJSON(String address){
        URL url = null;
        StringBuilder sb = new StringBuilder();
        HttpsURLConnection urlConnection = null;
        try {
            //Get URL
            url = new URL(address);
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }

        try {
            //Open URL
            urlConnection = (HttpsURLConnection) url.openConnection();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            InputStream content = new BufferedInputStream(urlConnection.getInputStream());

            BufferedReader reader = new BufferedReader(new InputStreamReader(content));
            String line;
            while((line = reader.readLine()) != null){
                sb.append(line);
            }
        } catch (IOException e) {
            //Catch invalid zip code
            getActivity().runOnUiThread(() -> displayAlert("Invalid zip code"));
        }finally {
            urlConnection.disconnect();
        }
        return sb.toString();

    }
    private class ReadJSONFeed extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... urls) {

            return readJSON(urls[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try{
                JSONObject weatherObject = new JSONObject(result);

                String strResult = "Weather\n";

                //Get Longitude and Latitude in Object "coord"
                JSONObject dataObject = weatherObject.getJSONObject("coord");
                strResult+="lon:"+dataObject.getString("lon");
                strResult+="\nlat:"+dataObject.getString("lat");

                //Get Humid and temp in Object "main"
                dataObject = weatherObject.getJSONObject("main");
                strResult+="\nhumidity:"+dataObject.getString("humidity");
                strResult+="\ntemp:"+dataObject.getString("temp");

                //Get name in Object "weather"
                strResult+="\nname:"+ weatherObject.getString("name");
                strResult+="\nZip Code:" + zipcodeText.getText().toString();
                weatherDisplay.setText(strResult);

            } catch (JSONException e) {
                //Dialog appears when invalid zip code so doesnt need to printStackTrace here
               // e.printStackTrace();
            }
        }
    }
}
