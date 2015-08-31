package com.example.dylan.ourcloud.post_detail;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.Comment;
import com.example.dylan.ourcloud.R;

import java.util.List;

/**
 * Created by dylan on 8/31/15.
 */
public class PostCommentsFrag extends Fragment implements View.OnClickListener,CommentController.Callback {

    private CommentController commentController;

    MaterialDialog newComment;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        commentController = new CommentController(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.post_comments,container,false);

        TextView addButton = (TextView) v.findViewById(R.id.addComButton);
        addButton.setOnClickListener(this);
        initDialogs();
        return v;
    }

    @Override
    public void commentSubmitted() {
        //called after a comment is successfully submitted, reload now
    }

    @Override
    public void getComments(List<Comment> comments) {
        //List of comments for this post, need to be sent to a ListView adapter as cards 
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addComButton :
                newComment.show();
                Log.i("comClick","Com Click");
                break;
        }
    }

    public void initDialogs() {

        View newCommentView = LayoutInflater.from(getActivity()).inflate(R.layout.new_comment, null);
        //load user image into imageview
        newComment = new MaterialDialog.Builder(getActivity())
                .title("New Comment")
                .customView(newCommentView,true)
                .positiveText("Done")
                .positiveColor(getResources().getColor(R.color.ColorPrimary))
                .build();
    }

}
