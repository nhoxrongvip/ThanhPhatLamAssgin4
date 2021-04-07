package thanh.lam.n01335598;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public class ImageAdapter extends BaseAdapter {
    Context context;
    String[] imgUrl;
    LayoutInflater inflater;
    public ImageAdapter(Context context,String[] imgUrl){
        this.context = context;
        this.imgUrl = imgUrl;
        inflater = (LayoutInflater.from(context));

    }

    @Override
    public int getCount() {
        return imgUrl.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.image_spinner,null);
        ImageView img = convertView.findViewById(R.id.ThanhImageView);
        Picasso.get().load(imgUrl[position]).into(img);
        return convertView;
    }
}
