package pl.vanhok.myapplication;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.AndroidException;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.squareup.picasso.Picasso;

import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;

import static android.graphics.Color.rgb;


class CommentStructure{
    private TextView tVotes,tAuthor,tTime,tCommentText;
    private LinearLayout linearLayout;

    CommentStructure(Activity v, Comments comment){
        try {
            tVotes = new TextView(v);
            tAuthor = new TextView(v);
            tTime = new TextView(v);
            tCommentText = new TextView(v);
        }catch (Exception e){
            e.printStackTrace();
        }
        tAuthor.setText(comment.getAuthor());
        tVotes.setText(comment.getVotes());
        tTime.setText(comment.getPostTime());
        tCommentText.setText(comment.getCommentText());

        tAuthor.setTextColor(comment.getNickColor());
        tVotes.setTextColor(comment.getVotesColor());

        linearLayout = new LinearLayout(v);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.addView(tAuthor);
        linearLayout.addView(tTime);
        linearLayout.addView(tVotes);
        linearLayout.addView(tCommentText);
    }

    LinearLayout getLinearLayout() {
        return linearLayout;
    }
}


class Comments{
    private String author;
    private String commentText;
    private String postTime;
    private String votes;
    private String authorHref;
    private int nickColor;



    private int votesColor;



    String getAuthor() {
        return author;
    }

    String getCommentText() {
        return commentText;
    }

    String getPostTime() {
        return postTime;
    }

    int getNickColor(){
        return nickColor;
    }

    String getVotes() {
        if(nickColor == Color.RED)
            return votes;

        return "+"+votes;
    }

    String getAuthorHref() {
        return authorHref;
    }

    int getVotesColor() {
        return votesColor;
    }

    Comments(Element wb) {
        Element commentInformation = wb.selectFirst("div.author.ellipsis");

        Element authorEl = commentInformation.selectFirst("a");
        author = authorEl.text();
        if(authorEl.hasClass("color-0")){
            nickColor = Color.GREEN;
        }
        else if(authorEl.hasClass("color-1")){
            nickColor = rgb(255, 134, 0);
        }
        else{
            nickColor = rgb(128, 0, 0);
        }

        authorHref = commentInformation.selectFirst("a").attr("href");
        postTime = commentInformation.selectFirst("time").text();
        votes = commentInformation.selectFirst("p.vC").attr("data-vc");

        if(Integer.parseInt(votes) > 0)
            votesColor = Color.GREEN;
        else
            votesColor = Color.RED;

        commentText = wb.selectFirst("div.text > p").text();
    }


}

class SelectedNews {

   private String href;
   private String headerText;
   private String mainText;
   private String amountOfdiggs;
   private String imageHref;
   private boolean hot;
   private ArrayList <Comments> comments = new ArrayList<>();

   String getImageHref() {
        return imageHref;
    }

    Comments getComment(int id){
        return comments.get(id);
    }

    String getHref() {
        return href;
    }

    String getHeaderText() {
        return headerText;
    }

    String getMainText() {
        return mainText;
    }

    int getAmountOfComments(){
        return comments.size();
    }

    String getAmountOfdiggs() {
        return amountOfdiggs;
    }

    boolean isHot() {
        return hot;
    }

    SelectedNews(WebPage wb){
        Element article = wb.getHtml().selectFirst(".article.fullview.clearfix.dC");
        href = wb.getWebAddress();
        Element diggs = article.selectFirst("div > a > span");
        amountOfdiggs = diggs.text();
        if(article.selectFirst("div.diggbox > i")!=null)
            hot = true;
        else
            hot = false;

        imageHref = article.selectFirst("a[href] > img").attr("src");

        headerText = article.selectFirst("h2").text();
        mainText = article.selectFirst("p.text").text();
        Elements c = wb.getHtml().select(".wblock.lcontrast.dC");
        if(c==null){
            return;
        }
        for(int i = 0;i < c.size();i++) {
                comments.add(new Comments(c.get(i)));
        }
    }
}

public class SelectedNewsActivity extends AppCompatActivity {

    WebPage html;
    SelectedNews news;
    LinearLayout lLayout;
    ArrayList <CommentStructure> cs;

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
                    cs.add(new CommentStructure(SelectedNewsActivity.this,news.getComment(i)));
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

                        for(CommentStructure csIterator:cs){
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
