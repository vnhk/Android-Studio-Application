package pl.vanhok.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;



public class SelectedNewsActivity extends AppCompatActivity {

    WebPage html;
    SelectedNews news;
    LinearLayout lLayout;
    ArrayList <CommentsStructure> cs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_news);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ImageView iv = findViewById(R.id.imageSelectedNews);

        final Intent in = getIntent();
        new Thread(new Runnable() {
            @Override
            public void run() {
                html = new WebPage(in.getCharSequenceExtra("WykopNews").toString());
                news = new SelectedNews(html);
                lLayout = findViewById(R.id.lLayoutSelectedNews);
                final TextView tvHeader = findViewById(R.id.selectedNewsHeader);
                final TextView tvInfoText = findViewById(R.id.selectedNewsInfoTextView);
                final TextView tvDiggs = findViewById(R.id.selectedNewsDiggs);

                cs = new ArrayList<>();

                for(int i = 0;i < news.getAmountOfComments(); i++){
                    cs.add(new CommentsStructure(SelectedNewsActivity.this,news.getComment(i)));
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Picasso.with(SelectedNewsActivity.this)
                                .load(news.getImageHref())
                                .into(iv);


                       tvHeader.setText(news.getHeaderText());
                       tvDiggs.setText(news.getAmountOfdiggs());
                       tvInfoText.setText(news.getMainText());

                        for(CommentsStructure csIterator:cs){
                           lLayout.addView(csIterator.getLinearLayout());
                        }

                    }
                });
            }
        }).start();


        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
