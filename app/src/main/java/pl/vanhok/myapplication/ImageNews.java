package pl.vanhok.myapplication;

import android.app.Activity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public abstract class ImageNews {
    public static void getImage(ImageView iv, String src, Activity v){
        Picasso.with(v).load(src).into(iv);
    }
}
