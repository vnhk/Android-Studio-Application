package pl.vanhok.myapplication;

import android.graphics.Color;

import org.jsoup.nodes.Element;

import static android.graphics.Color.rgb;

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