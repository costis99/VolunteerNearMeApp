package Comp3200.volunteernearmeapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Objects;

public class MessageAdapter extends RecyclerView.Adapter {
    private final List<MessageType> listOfMsgs;
    private final FirebaseAuth auth = FirebaseAuth.getInstance();
    private final FirebaseFirestore fStore = FirebaseFirestore.getInstance();
    public MessageAdapter(List<MessageType> listOfMsg) {
        listOfMsgs = listOfMsg;
    }

    // Inflates the appropriate layout according to the ViewType.
    @NotNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        View v;
        //check if message belongs to the current user (1) or to any other user (2)
        if (i == 1) {
            //add the layout to the view type
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_my_chat, viewGroup, false);
            return new MessageSender(v);
        } else if (i == 2) {
            //add the layout to the view type
            v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_other_people_chat, viewGroup, false);
            return new MessageReceiver(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int psn) {
        MessageType msgType = (MessageType) listOfMsgs.get(psn);
        //takes a messageType and passes it to the ViewHolder
        //check if message belongs to the current user (1) or to any other user (2)
        switch (viewHolder.getItemViewType()) {
            case 1:
                //bind message contents to the UI
                ((MessageSender) viewHolder).bind(msgType);
                break;
            case 2:
                //bind message contents to the UI
                ((MessageReceiver) viewHolder).bind(msgType);
        }
    }

    @Override
    public int getItemViewType(int psn) {
        MessageType msg;
        msg = (MessageType) listOfMsgs.get(psn);
        //check if message belongs to the current user (1) or to any other user (2)
        // and return appropriate number
        if (msg.getUser().equals(Objects.requireNonNull(auth.getCurrentUser()).getUid())) {
            return 1;
        } else {
            return 2;
        }
    }

    //Get size of list Of all Messages Sent
    @Override
    public int getItemCount() {
        return listOfMsgs.size();
    }

    class MessageReceiver extends RecyclerView.ViewHolder {
        TextView msg,
                msgUserId,
                msgTime;

        MessageReceiver(View itemView) {
            super(itemView);
            msgUserId = (TextView) itemView.findViewById(R.id.message_of_user);

            msgTime = (TextView) itemView.findViewById(R.id.time_chat);
            msg = (TextView) itemView.findViewById(R.id.chat_of_user);

        }

        void bind(MessageType msgType) {
            msg.setText(msgType.getMessageContent());
            //need to access the database in order to get the name of the user (since I used uid as the sender which is a unique id)
            DocumentReference documentReference = fStore.collection("users").document(msgType.getUser());
            documentReference.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        msgUserId.setText(Objects.requireNonNull(document.get("Nickname")).toString());
                    }
                } else {
                    System.out.println("Error!");
                }
            });
            // Format the stored timestamp into a readable String using method.
            msgTime.setText(msgType.getTime());
        }
    }

    private static class MessageSender extends RecyclerView.ViewHolder {
        TextView msg,
                msgTime;

        public MessageSender(View v) {
            super(v);

            msgTime = (TextView) itemView.findViewById(R.id.my_chat_time);
            msg = (TextView) itemView.findViewById(R.id.my_text_msg);

        }

        public void bind(MessageType msgType) {
            msgTime.setText(msgType.getTime());
            msg.setText(msgType.getMessageContent());
        }
    }


}