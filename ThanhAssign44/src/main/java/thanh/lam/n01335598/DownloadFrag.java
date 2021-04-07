package thanh.lam.n01335598;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DownloadFrag extends Fragment {

    String[] imageUrlList = {"https://cdn.pixabay.com/photo/2015/04/23/22/00/tree-736885_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/08/30/01/05/milky-way-2695569_960_720.jpg",
            "https://cdn.pixabay.com/photo/2018/01/12/10/19/fantasy-3077928_960_720.jpg"
    };

    View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_download, container, false);
        view = v;


        Spinner imageSpinner = v.findViewById(R.id.ThanhImageSpinner);
        ImageAdapter imgAdapter = new ImageAdapter(getActivity(), imageUrlList);
        imageSpinner.setAdapter(imgAdapter);





        return v;
    }







}
