package pl.vanhok.myapplication;

import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

class SelectedNews {

    private String href;
    private String headerText;
    private String mainText;
    private String amountOfdiggs;
    private String imageHref;
    private boolean hot;
    private ArrayList<Comments> comments = new ArrayList<>();

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