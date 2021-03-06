package pl.vanhok.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.widget.LinearLayout;
import android.widget.TextView;

class CommentsStructure{
    private TextView tVotes,tAuthor,tTime,tCommentText;
    private LinearLayout linearLayout;

    CommentsStructure(Activity v, Comments comment){
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

        tAuthor.setBackgroundColor((v.getResources().getColor(R.color.commentContent)));
        tTime.setTextColor(v.getResources().getColor(R.color.commentContent));
        tCommentText.setTextColor(Color.WHITE);

        tAuthor.setTextColor(comment.getNickColor());
        tVotes.setTextColor(comment.getVotesColor());

        linearLayout = new LinearLayout(v);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPaddingRelative(10,10,10,10);
        linearLayout.addView(tAuthor);
        linearLayout.addView(tTime);
        linearLayout.addView(tVotes);
        linearLayout.addView(tCommentText);

    }

    LinearLayout getLinearLayout() {
        return linearLayout;
    }
}
