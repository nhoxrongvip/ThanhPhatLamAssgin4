package thanh.lam.n01335598;


import android.annotation.SuppressLint;
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

    View view;
    ImageView background, spinner_image;
    Button downloadButton;
    ImageAdapter imgAdapter;


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

        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    private class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        URL link;
        HttpURLConnection urlConnection;
        InputStream in;
        Bitmap bm;
        ProgressDialog p;

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);


        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            bm = null;
            try {
                link = new URL(strings[0]);//Get url
                urlConnection = (HttpURLConnection) link.openConnection();//Open url
                in = urlConnection.getInputStream();
                bm = BitmapFactory.decodeStream(in);

                DownloadImageToExeternalStorage(bm,link.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return bm;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            p = new ProgressDialog(getActivity());
            p.setTitle("Downloading");
            p.setMessage("Downloading message");
            p.show();
        }

        private void DownloadImageToExeternalStorage(Bitmap bm,String imgURL){

            try{
                File sdCard = Environment.getExternalStorageDirectory();
                @SuppressLint("DefaultLocale") String fileName = String.format("%d.jpg",System.currentTimeMillis());
                File dir = new File(sdCard.getAbsolutePath()+"/savedImage");
                if(!dir.exists()){
                    dir.mkdirs();
                }

                File imgFile = new File(dir, fileName);
                FileOutputStream fos = null;
                try {
                    fos = new FileOutputStream(imgFile);
                    bm = Picasso.get().load(imgURL).get();
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, fos);
                    Intent i = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    i.setData(Uri.fromFile(imgFile));
                    getActivity().sendBroadcast(i);
                    Toast.makeText(getActivity(),"Image downloaded",Toast.LENGTH_LONG).show();

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

            }catch (Exception e){e.printStackTrace();}

        }

    }


}










