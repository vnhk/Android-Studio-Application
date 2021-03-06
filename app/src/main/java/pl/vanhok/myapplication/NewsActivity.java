package pl.vanhok.myapplication;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

class News{

    private final String defaultImageSrc = "https://www.wykop.pl/cdn/c2526412/no-picture,w207h139.jpg";

    private String href = "error";
    private String headerText  = "error";
    private String mainText  = "error";
    private String imageSrc  = "error";

    void setReady(boolean ready) {
        this.ready = ready;
    }

    boolean isReady() {
        return ready;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    private boolean ready = false;

    News(Element el) {
        Element e;
        if((e = el.selectFirst("div.lcontrast.m-reset-margin > h2 > a"))!=null) {
            headerText = e.text();
            href = e.attr("href");
        }
        if((e = el.selectFirst("div.lcontrast.m-reset-margin > div.description > p"))!=null) {
            mainText = e.text();
        }
        if((e = el.selectFirst("div.media-content"))!=null) {

            if((e = e.select("img").first())!=null){
                imageSrc = e.absUrl("src");
                if(imageSrc.length()<1)
                    imageSrc = e.attr("src");
                if(imageSrc.length()<1)
                    imageSrc = e.attr("data-original");
            }
            else
                imageSrc = defaultImageSrc;
        }
        else
            imageSrc = defaultImageSrc;

    }

    String getHeaderText() {
        return headerText;
    }
    String getHref(){
        return href;
    }
    String getMainText(){
        return mainText;
    }

}

public class NewsActivity extends AppCompatActivity {
    private WebPage html;
    private int number = 0;
    private ArrayList<News> news;
    private TextView result;
    private ImageView iv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        news = new ArrayList<>();
        Thread createNews = new Thread(new Runnable() {
            @Override
            public void run() {
                iv = findViewById(R.id.imageNews);
                    html = new WebPage("https://www.wykop.pl/");
                    Elements elements = html.getHtml().select("div.article.clearfix.preview.dC");
                    if(elements != null) {
                        int i = 0, y = 0;
                        while (i < elements.size()) {
                            while (y < elements.size()) {
                                if (elements.get(y) == null) {
                                    y++;
                                } else {
                                    news.add(new News(elements.get(y)));
                                    y++;
                                    i++;
                                }
                            }

                        }
                    }
            }
        });

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        createNews.start();

        result = findViewById(R.id.result);
        TextView resultHref = findViewById(R.id.resultHref);

        resultHref.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openNews();
            }
        });

        final Button buttonPrev = findViewById(R.id.buttonPrev);
        Button buttonNext = findViewById(R.id.buttonNext);

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(news.size()<=0)
                    return;
                --number;
                if(number<0)
                    number = news.size()-1;
                getText();
            }
        });

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(news.size()<=0)
                    return;
                ++number;
                if(number>news.size()-1)
                    number = 0;
                getText();
            }
        });
    }

    public void getText(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                final StringBuilder builder = new StringBuilder();

                builder.append(news.get(number).getHeaderText());
                builder.append("\n");
                builder.append("\n");
                builder.append(news.get(number).getMainText());
                news.get(number).setReady(true);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        ImageNews.getImage(iv,news.get(number).getImageSrc(),NewsActivity.this);

                        result.setText(builder.toString());
                    }
                });
            }
        }).start();
    }

    public void openNews(){
        if(news.get(number).isReady()){
            Intent i = new Intent(this,SelectedNewsActivity.class);
            i.putExtra("WykopNews",news.get(number).getHref());
            i.putExtra("ImageSrc",news.get(number).getImageSrc());
            startActivity(i);
        }


    }


}
