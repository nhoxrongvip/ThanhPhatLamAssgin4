package thanh.lam.n01335598;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class DownloadFrag extends Fragment {

    String[] imageUrlList = {"https://wallpapercave.com/wp/wp7743040.jpg",
            "https://i.pinimg.com/564x/0b/ac/f6/0bacf62a4bd456d02d02c6b8a5c98f67.jpg",
            "https://wallpapercave.com/wp/wp2722822.jpg"
    };

    View view;
    ImageView background;
    Button downloadButton;
    ImageAdapter imgAdapter;
    Bitmap bm;
    int downloadLength;
    ProgressDialog p;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_download, container, false);
        view = v;


        Spinner imageSpinner = v.findViewById(R.id.ThanhImageSpinner);
        imgAdapter = new ImageAdapter(getActivity(), imageUrlList);
        imageSpinner.setAdapter(imgAdapter);
        downloadButton = (Button) v.findViewById(R.id.ThanhDownloadButton);
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            200);

                }else{
                    down();
                }
            }
        });



        return v;
    }

    private void down() {
        DownloadImage downloadImage = new DownloadImage();
        try {
            bm = downloadImage.execute(imgAdapter.getImageURL(imageUrlList)).get();
            background = view.findViewById(R.id.ThanhDownloadFragBackground);
            background.setImageBitmap(bm);

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
        Bitmap bm;


        @Override
        protected void onProgressUpdate(String... values) {
           p.setProgress(Integer.parseInt(values[0]));


        }

        @Override
        protected Bitmap doInBackground(String... strings) {
            bm = null;
            try {
                int count;
                int total = 0;
                link = new URL(strings[0]);//Get url
                urlConnection = (HttpURLConnection) link.openConnection();//Open url
                in = urlConnection.getInputStream();
                bm = BitmapFactory.decodeStream(in);

                urlConnection.connect();
                downloadLength = urlConnection.getContentLength();
                byte data[] = new byte[1024];
                while ((count = in.read(data)) != -1) {
                    total += count;
                    publishProgress(""+((total*100)/downloadLength));
                }


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
            p.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            p.setTitle("Downloading");
            p.setMessage("Downloading message");
            p.show();


        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            p.dismiss();
        }
    }


    private class SaveImage extends AsyncTask<Bitmap, Void, Bitmap> {


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















