# ThanhPhatLamAssgin4<!--Thanh Phat Lam N01335598 CENG258 RNC-->

Thanh Phat Lam N01335598 CENG258 RNC Assignment 4 <br>
This assignment is about Download, get Longitude and Latitude, Navigation Drawable,get location details and SharedPreferences

## Contribution
Give it up to [Thanh Phat Lam](https://www.instagram.com/thanhphat.tee/) <br>


## Usage
This app has 4 fragments containing in one activity.

# Activity
## Main Activity
The main activity contains 2 things: Navigation Drawable and Location longitude and latitude. 
### Navigation Drawable
Create a menu item and include it in the main xml file NavigationView
```
<com.google.android.material.navigation.NavigationView
        android:id="@+id/ThanhNavView"
        android:layout_width="wrap_content"
        android:layout_height="match_parent"
        android:layout_gravity="start"
        app:headerLayout="@layout/nav_header"
        app:menu="@menu/drawer_menu"

        />
```
### Location longitude and latitude
First, we need to check the requirement to get the location data:
```
if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
```
Then, when the permissions are granted, we get the info we want
```java
latitude = l.getLatitude();
longitude = l.getLongitude();
String display = "Latitude:" + latitude + " Longitude:" +longitude;
Toast.makeText(ThanhActivity.this, display,Toast.LENGTH_SHORT).show();
```
# Fragment
## Home Fragment
In this fragment, 2 main things are setting hour format and background color. <br>
The following code represents how can I change the hour format
```java
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
```
However, to change this in Setting Fragment, we need a key
```java
 preferences = this.getActivity().getApplication().getSharedPreferences("HourFormat", Context.MODE_PRIVATE);
        format = preferences.getString("format","24h"); //Default value is 24h
```
The same concept applies to change background color
```java
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
```
Get Background INT color code
```java
 color = preferences_color.getInt("color",getResources().getColor(R.color.bright_green));
        color_name = preferences_color.getString("color_name","GREEN");
        getColor(color_name);
        homeBackground.setBackgroundColor(color);
```
## Download Fragment
To display image in the spinner, Picasso library does this task
```java
        convertView = inflater.inflate(R.layout.image_spinner,null);
        ImageView img = convertView.findViewById(R.id.ThanhImageView);
        Picasso.get().load(imgUrl[position]).into(img);
        return convertView;
```
In this fragment, I get the image from URL and set that image to background. Also, I can save it to the device.<br>
This is how you get image from URL
```java
link = new URL(strings[0]);//Get url
                urlConnection = (HttpURLConnection) link.openConnection();//Open url
                in = urlConnection.getInputStream();
                bm = BitmapFactory.decodeStream(in);
```
This is how you download it to your device. After getting the right path, I use bitmap.compress to save image to device
```java
BitmapDrawable drawable = (BitmapDrawable) background.getDrawable();
                Bitmap bit = drawable.getBitmap();

                //Create File outputstream
                FileOutputStream fos = null;

                //Get External Storage location
                File sdCard = Environment.getExternalStorageDirectory();
                File dir = new File(sdCard.getAbsolutePath() + "/Download");

                //Make directory if not exists
                dir.mkdirs();

                //Image name
                String filename = String.format("picture%d.jpg", imgAdapter.pos);
                File output = new File(dir, filename);

                //If Image exists, toast, display to background but not download again
                if (output.exists()) {
                    p.dismiss();
                    Toast.makeText(getActivity(), "Image already downloaded. Check you file", Toast.LENGTH_SHORT).show();

                } else {
                    try {
                        //Get ouputstream
                        fos = new FileOutputStream(output);

                        //Save Image
                        bit.compress(Bitmap.CompressFormat.JPEG, 100, fos);

                        //Save image to Gallery or Google photo
                        Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        i.setData(Uri.fromFile(output));
                        getActivity().sendBroadcast(i);
```
## WebService Fragment
I use the openWeather API to do this task. Also, if you want to use a sample output, uncomment this line
```
//String url = "https://samples.openweathermap.org/data/2.5/weather?zip="+zipcode+",us&appid=b6907d289e10d714a6e88b30761fae22";
```
The format of output is JSON so I have to read JSON object and JSON array
```java
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

```
When entering the valid ZIP code, the result appears on the screen
## Setting Fragment
Mainly in this fragment is sending key and value to Home Fragment<br>
This is how to send hour format
```java
String format = "";
        SharedPreferences.Editor edit = preferences.edit();
        if (format12hour.isChecked()) {
            format = format12hour.getText().toString();

        } else if (format24hour.isChecked()) {
            format = format24hour.getText().toString();

        }
        edit.putString("format", format);
        edit.apply();
```
This is how to send color background 
```java
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
```
