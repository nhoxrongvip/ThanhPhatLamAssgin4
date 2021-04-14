package thanh.lam.n01335598;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import androidx.constraintlayout.widget.ConstraintLayout;

import thanh.lam.n01335598.ImageAdapter;

public class DownloadFrag extends Fragment {

    String[] imageUrlList = {"https://wallpapercave.com/wp/wp7743040.jpg",
            "https://i.pinimg.com/564x/0b/ac/f6/0bacf62a4bd456d02d02c6b8a5c98f67.jpg",
            "https://wallpapercave.com/wp/wp2722822.jpg"
    };
    Bitmap bm;

    View view;
    ImageView background, spinner_image;
    Button downloadButton;
    ImageAdapter imgAdapter;
    int downloadLength;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_download, container, false);
        view = v;


        Spinner imageSpinner = v.findViewById(R.id.ThanhImageSpinner);
        imgAdapter = new ImageAdapter(getActivity(), imageUrlList);
        imageSpinner.setAdapter(imgAdapter);
        downloadButton = (Button) v.findViewById(R.id.ThanhDownloadButton);
        downloadButton.setOnClickListener(v1 -> down());


        return v;
    }

    private void down() {
        DownloadImage downloadImage = new DownloadImage();
        try {
            Bitmap bm = downloadImage.execute(imgAdapter.getImageURL(imageUrlList)).get();
            background = view.findViewById(R.id.ThanhDownloadFragBackground);
            background.setImageBitmap(bm);


            //Permission for download is requested in the AsyncTask
            new SaveImage().execute(bm);

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private class DownloadImage extends AsyncTask<String, String, Bitmap> {
        URL link;
        HttpURLConnection urlConnection;
        InputStream in;
        ProgressDialog p;

        private void showDialog() {
            p = new ProgressDialog(getActivity());
            p.setMessage("Downloading The Image");
            p.setIndeterminate(false);
            p.setTitle("Download the Image");
            p.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            p.setIcon(R.drawable.downloading);
            //p.setMax(100);

            p.show();


        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showDialog();

        }

        @Override
        protected void onProgressUpdate(String... values) {
            p.setProgress(Integer.parseInt(values[0]));

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            p.dismiss();
        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            bm = null;

            try {
                int count;
                int total=0;
                link = new URL(strings[0]);//Get url
                urlConnection = (HttpURLConnection) link.openConnection();//Open url
                in = urlConnection.getInputStream();
                urlConnection.connect();
                downloadLength = urlConnection.getContentLength();
                byte data[] = new byte[1024];
                while((count = in.read(data))!= -1){
                    total+=count;
                    publishProgress(""+((total*100)/downloadLength));
                }

                bm = BitmapFactory.decodeStream(in);


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bm;
        }


    }

    private class SaveImage extends AsyncTask<Bitmap, Void, Bitmap> {
        ProgressDialog p;



        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Bitmap doInBackground(Bitmap... bitmaps) {
            //Request permission in here
            return bm;
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            p.setProgress(downloadLength);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if (background != null) {
                background.setImageBitmap(bitmap);
                background.invalidate();
                //Get Bitmap Drawable
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

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } finally {
                        try {
                            fos.flush();
                            fos.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }


            }
        }
    }


}










