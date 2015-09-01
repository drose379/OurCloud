package com.example.dylan.ourcloud.post_detail;

import android.app.Activity;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.example.dylan.ourcloud.Comment;
import com.example.dylan.ourcloud.Post;
import com.example.dylan.ourcloud.R;
import com.example.dylan.ourcloud.UserInfo;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by dylan on 8/31/15.
 */
public class PostCommentsFrag extends Fragment implements View.OnClickListener,CommentController.Callback {

    private CommentController commentController;
    private Post post;

    MaterialDialog newComment;
    MaterialDialog loadingDialog;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        commentController = new CommentController(this);
    }

    @Override
    public void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);

        post = (Post) getArguments().getSerializable("postInfo");

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
        //called after a comment is successfully submitted, reload now (use getComments)
        loadingDialog.hide();
        //call getComments
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
                break;
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        loadingDialog.hide();
    }

    public void initDialogs() {

        View newCommentView = LayoutInflater.from(getActivity()).inflate(R.layout.new_comment, null);

        CircleImageView userImage = (CircleImageView) newCommentView.findViewById(R.id.userImageMini);
        TextView userName = (TextView) newCommentView.findViewById(R.id.userName);

        Picasso.with(getActivity()).load(UserInfo.getInstance().getProfileImageSized(80)).into(userImage);
        userName.setText(UserInfo.getInstance().getDisplayName());

        newComment = new MaterialDialog.Builder(getActivity())
                .title("New Comment")
                .customView(newCommentView, true)
                .positiveText("Done")
                .positiveColor(getResources().getColor(R.color.ColorPrimary))
                .negativeText("Cancel")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        EditText commentArea = (EditText) dialog.getCustomView().findViewById(R.id.newCommentArea);
                        if (!commentArea.getText().toString().isEmpty()) {
                            loadingDialog.show();
                            String comment = commentArea.getText().toString();
                            commentController.newComment(comment, post);
                            //why can I not show Loading dialog here? Saying window has leaked...
                        }
                    }
                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        EditText commentArea = (EditText) dialog.getCustomView().findViewById(R.id.newCommentArea);
                        commentArea.setText("");
                    }
                })
                //Need to implement onDismiss to clear edittext whenever dialog is dismissed, Use LayoutInflater to inflate view instead
                .build();

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
