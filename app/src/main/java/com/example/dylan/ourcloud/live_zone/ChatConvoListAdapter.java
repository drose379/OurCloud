package com.example.dylan.ourcloud.live_zone;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.dylan.ourcloud.R;

import java.util.List;

/**
 * Created by dylan on 9/17/15.
 */
public class ChatConvoListAdapter extends BaseAdapter {

    private Context context;
    private List<Message> messages;

    public ChatConvoListAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
    }

    public void updateMessages(List<Message> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    public int getCount() {
        return messages.size();
    }
    public long getItemId(int item) {
        return 0;
    }
    public Message getItem(int item) {
        return messages.get(item);
    }

    public View getView(int item,View recycledView,ViewGroup parent) {
        View v = recycledView;
        v = v == null ? LayoutInflater.from(context).inflate(R.layout.chat_message_card,parent,false) : v;
        CardView container = (CardView) v.findViewById(R.id.card);
        TextView messageTextTest = (TextView) v.findViewById(R.id.messageText);
        TextView senderNameTest = (TextView) v.findViewById(R.id.senderName);

        Message currentMessage = messages.get(item);
        int origin = currentMessage.getOrigin();

        messageTextTest.setText(currentMessage.getText());
        senderNameTest.setText(currentMessage.getOrigin() == 1 ? "Me" : currentMessage.getOtherUserName());
        senderNameTest.setTextColor(currentMessage.getOrigin() == 1 ? Color.DKGRAY : context.getResources().getColor(R.color.ColorPrimary));
        container.setCardBackgroundColor(Color.parseColor( origin == 1 ? "#F5F5F5" : (origin == 2 ? "#E0E0E0" : "#BDBDBD") ));


        return v;
    }


}
