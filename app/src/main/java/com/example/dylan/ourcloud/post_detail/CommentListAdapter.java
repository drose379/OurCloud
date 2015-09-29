package com.example.dylan.ourcloud.post_detail;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dylan.ourcloud.Comment;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.util.GPhotoUrlCut;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by dylan on 9/1/15.
 */
public class CommentListAdapter extends BaseAdapter {

    private Context context;
    private List<Comment> comments;

    LayoutInflater inflater;

    public CommentListAdapter(Context context,List<Comment> comments) {
        this.context = context;
        this.comments = comments;

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return comments.size();
    }

    @Override
    public long getItemId(int item) {
        return 0;
    }

    @Override
    public Comment getItem(int item) {
        return comments.get(item);
    }

    @Override
    public View getView(int item,View recycledView,ViewGroup parent) {

        Comment comment = comments.get(item);

        View v  = inflater.inflate(R.layout.comment_card,parent,false);

        ImageView userImage = (ImageView) v.findViewById(R.id.userImage);
        TextView userName = (TextView) v.findViewById(R.id.userName);

        TextView commentText = (TextView) v.findViewById(R.id.commentText);

        Picasso.with(context).load(GPhotoUrlCut.getImageSized(comment.getUserImage(),40)).into(userImage);
        userName.setText(comment.getUserName());

        commentText.setText(comment.getCommentText());

        return v;
    }

}
