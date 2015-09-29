package com.example.dylan.ourcloud.post_detail;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.Comment;
import com.example.dylan.ourcloud.LocalUser;
import com.example.dylan.ourcloud.LocalUserDBHelper;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.R;
import com.github.clans.fab.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dylan on 8/31/15.
 */
public class PostCommentsFrag extends Fragment implements View.OnClickListener,CommentController.Callback,ListView.OnScrollListener {

    private CommentController commentController;
    private Post post;

    int previousVisibleItem;

    ListView commentList;
    TextView noCommentsText;

    MaterialDialog newComment;
    MaterialDialog loadingDialog;
    FloatingActionButton menuButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        post = (Post) getArguments().getSerializable("postInfo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.post_comments,container,false);

        commentList = (ListView) v.findViewById(R.id.commentListView);

        TextView addButton = (TextView) v.findViewById(R.id.addComButton);
        noCommentsText = (TextView) v.findViewById(R.id.noCommentsText);

        addButton.setOnClickListener(this);
        commentList.setOnScrollListener(this);

        initDialogs();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        CommentController.getInstance(getActivity()).grabComments(post.getId(),this);
    }

    @Override
    public void getComments(List<Comment> comments) {
        if (!comments.isEmpty()) {
            commentList.setVisibility(View.VISIBLE);
            noCommentsText.setVisibility(View.GONE);
            commentList.setAdapter(new CommentListAdapter(getActivity(), comments));
        } else {
            commentList.setVisibility(View.GONE);
            noCommentsText.setVisibility(View.VISIBLE);
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addComButton :
                newComment.show();
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        loadingDialog.hide();
    }

    @Override
    public void onScroll(AbsListView list,int firstVisible,int visibleItems,int totalItems) {
        //int topMargin = commentList.getChildAt(0) != null ? commentList.getChildAt(0).getTop() : 0;
        if (firstVisible > previousVisibleItem) {menuButton.hide(true);} else if (firstVisible < previousVisibleItem) {menuButton.show(true);}
        previousVisibleItem = firstVisible;
    }

    @Override public void onScrollStateChanged(AbsListView list,int state) {}

    public void initDialogs() {

        loadingDialog = new MaterialDialog.Builder(getActivity())
                .title("Loading")
                .customView(R.layout.load_dialog,true)
                .dismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        loadingDialog.show();
                    }
                })
                .build();
    }

}
