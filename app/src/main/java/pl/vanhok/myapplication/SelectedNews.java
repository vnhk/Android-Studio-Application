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

    SelectedNews(WebPage wb, String imageSrc){
        Element article = wb.getHtml().selectFirst(".article.fullview.clearfix");
        href = wb.getWebAddress();
        Element diggs = article.selectFirst("div.diggbox > a > span");
        if(diggs!=null)
            amountOfdiggs = "+"+diggs.text();
        else
            amountOfdiggs = "+0";

        if(article.selectFirst("div.diggbox > i")!=null)
            hot = true;
        else
            hot = false;

        imageHref = imageSrc;

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