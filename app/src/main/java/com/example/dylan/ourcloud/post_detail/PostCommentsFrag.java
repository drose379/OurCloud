package com.example.dylan.ourcloud.post_detail;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
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
public class PostCommentsFrag extends Fragment implements CommentController.Callback,ListView.OnScrollListener {

    private Post post;
    private BroadcastReceiver newCommentReceiver;

    int previousVisibleItem;

    ListView commentList;
    TextView noCommentsText;

    MaterialDialog newComment;
    MaterialDialog loadingDialog;
    FloatingActionButton newComButton;

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
        newComButton = (FloatingActionButton) getActivity().findViewById(R.id.newCommentButton);

        commentList = (ListView) v.findViewById(R.id.commentListView);

        noCommentsText = (TextView) v.findViewById(R.id.noCommentsText);

        commentList.setOnScrollListener(this);

        initDialogs();
        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        CommentController.getInstance(getActivity()).grabComments(post.getId(), this);
        initBroadcastReceiver();
    }

    @Override
    public void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(newCommentReceiver);
        loadingDialog.hide();
        newComButton.show(true);
    }


    public void initBroadcastReceiver() {

        newCommentReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                CommentController.getInstance(getActivity()).grabComments(post.getId(),PostCommentsFrag.this);
            }
        };

        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(newCommentReceiver,new IntentFilter(CommentController.NEW_COMMENT_SUBMIT));

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
    public void onScroll(AbsListView list,int firstVisible,int visibleItems,int totalItems) {
        if (firstVisible > previousVisibleItem) {newComButton.hide(true);} else if (firstVisible < previousVisibleItem) {newComButton.show(true);}
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
