package pl.vanhok.myapplication;

import android.app.Activity;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public abstract class ImageNews {
    private final static String defaultImageSrc = "https://www.wykop.pl/cdn/c2526412/no-picture,w207h139.jpg";
    public static void getImage(ImageView iv, String src, Activity v){
        try {
            if(src.length()<1) {
                throw new Exception("String is too short");
            }
                Picasso.with(v).load(src).into(iv);
        }catch (Exception e){
            e.printStackTrace();
            Picasso.with(v).load(defaultImageSrc).into(iv);
        }
    }
}
