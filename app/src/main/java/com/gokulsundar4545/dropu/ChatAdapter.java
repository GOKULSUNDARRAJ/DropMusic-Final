package com.gokulsundar4545.dropu;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> messages;
    private String currentUserUid;

    private static final int VIEW_TYPE_INCOMING = 1;
    private static final int VIEW_TYPE_OUTGOING = 2;

    public ChatAdapter(List<Message> messages, String currentUserUid) {
        this.messages = messages;
        this.currentUserUid = currentUserUid;
    }

    @Override
    public int getItemViewType(int position) {
        Message message = messages.get(position);
        if (message.getSender().equals(currentUserUid)) {
            return VIEW_TYPE_OUTGOING;
        } else {
            return VIEW_TYPE_INCOMING;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == VIEW_TYPE_INCOMING) {
            View itemView = inflater.inflate(R.layout.item_message_sent, parent, false);
            return new IncomingViewHolder(itemView);
        } else {
            View itemView = inflater.inflate(R.layout.item_message_received, parent, false);
            return new OutgoingViewHolder(itemView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messages.get(position);
        if (holder instanceof IncomingViewHolder) {
            ((IncomingViewHolder) holder).bind(message);
        } else if (holder instanceof OutgoingViewHolder) {
            ((OutgoingViewHolder) holder).bind(message);
        }

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class IncomingViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView,time;
        ImageView imageView; // Add ImageView

        ConstraintLayout carproduct;

        IncomingViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextViewOutgoing);
            imageView = itemView.findViewById(R.id.imageView); // Initialize ImageView

            time=itemView.findViewById(R.id.time);
            carproduct=itemView.findViewById(R.id.carproduct);


        }

        void bind(Message message) {
            if (message.getType().equals("image")) {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(itemView)
                        .load(message.getMessage())
                        .into(imageView);

                messageTextView.setVisibility(View.GONE); // Hide TextView if it's an image message
            } else {
                messageTextView.setVisibility(View.VISIBLE);
                messageTextView.setText(message.getMessage());
                imageView.setVisibility(View.GONE);
            }
            String indianTime = convertToIndianTime(message.getTimestamp());
            time.setText(indianTime);

            messageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(messageTextView.getText());
                    Toast.makeText(imageView.getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });

            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Message message = messages.get(getAdapterPosition());
                    if (message.getType().equals("image")) {
                        String imageUrl = message.getMessage();
                        // Create intent to start ZoomActivity
                        Intent intent = new Intent(itemView.getContext(), ZoomActivity.class);
                        // Pass image URL as an extra
                        intent.putExtra("imageUrl", imageUrl);

                        Pair[] pairs=new Pair[1];
                        pairs[0] =new Pair<View,String>(imageView,"transaction_player");

                        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(),pairs);

                        itemView.getContext().startActivity(intent,options.toBundle());
                    }
                }
            });


        }
    }

    class OutgoingViewHolder extends RecyclerView.ViewHolder {
        TextView messageTextView,time;
        ImageView imageView; // Add ImageView

        ConstraintLayout carproduct;

        OutgoingViewHolder(@NonNull View itemView) {
            super(itemView);
            messageTextView = itemView.findViewById(R.id.messageTextViewIncoming);
            imageView = itemView.findViewById(R.id.imageView); // Initialize ImageView
            time=itemView.findViewById(R.id.time);

            carproduct=itemView.findViewById(R.id.carproduct);
        }

        void bind(Message message) {
            if (message.getType().equals("image")) {
                imageView.setVisibility(View.VISIBLE);
                Glide.with(itemView)
                        .load(message.getMessage())
                        .into(imageView);

                messageTextView.setVisibility(View.GONE); // Hide TextView if it's an image message
            } else {
                messageTextView.setVisibility(View.VISIBLE);
                messageTextView.setText(message.getMessage());
                imageView.setVisibility(View.GONE);
            }

            String indianTime = convertToIndianTime(message.getTimestamp());

            time.setText(indianTime);

            messageTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ClipboardManager cm = (ClipboardManager)view.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                    cm.setText(messageTextView.getText());
                    Toast.makeText(imageView.getContext(), "Copied to clipboard", Toast.LENGTH_SHORT).show();
                }
            });






            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Message message = messages.get(getAdapterPosition());
                    if (message.getType().equals("image")) {
                        String imageUrl = message.getMessage();
                        // Create intent to start ZoomActivity
                        Intent intent = new Intent(itemView.getContext(), ZoomActivity.class);
                        // Pass image URL as an extra
                        intent.putExtra("imageUrl", imageUrl);

                        Pair[] pairs=new Pair[1];
                        pairs[0] =new Pair<View,String>(imageView,"transaction_player");

                        ActivityOptions options=ActivityOptions.makeSceneTransitionAnimation((Activity) view.getContext(),pairs);

                        itemView.getContext().startActivity(intent,options.toBundle());
                    }
                }
            });


        }
    }

    private String convertToIndianTime(long timestamp) {
        // Create a SimpleDateFormat object with the desired date format and time zone (IST)
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Kolkata")); // Set time zone to IST

        // Convert timestamp to Date object
        Date date = new Date(timestamp);

        // Format the date to a string using SimpleDateFormat
        return sdf.format(date);
    }
}
