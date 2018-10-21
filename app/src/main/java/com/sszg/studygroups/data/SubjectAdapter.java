package com.sszg.studygroups.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sszg.studygroups.R;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.MyViewHolder> {

    private Context mContext;
    private List<Subject> subjectList;
    private ItemClickListener mClickListener;

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title, count, time;
        public ImageView thumbnail, overflow;

        public MyViewHolder(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            count = view.findViewById(R.id.count);
            thumbnail = view.findViewById(R.id.thumbnail);
            overflow = view.findViewById(R.id.overflow);
            time = view.findViewById(R.id.time_text);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) {
                mClickListener.onItemClick(view, getAdapterPosition());
            }
        }
    }


    public SubjectAdapter(Context mContext) {
        this.mContext = mContext;
        this.subjectList = new ArrayList<>();
    }

    public void addSubject(Subject subject) {
        subjectList.add(subject);
    }

    public Subject getSubject(int i) {
        return subjectList.get(i);
    }

    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.study_group_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Subject subject = subjectList.get(position);
        holder.title.setText(subject.getCourseName());
        Date currentTime = new Date();
        long diff = currentTime.getTime() - subject.getTimestamp().getTime();
        long diffSeconds = diff / 1000;
        long diffMinutes = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        holder.count.setText(String.valueOf("Taught by " + subject.getProfessorName()));
        if (diffHours > 0) {
            if (diffMinutes >= 60) {
                diffMinutes = diffMinutes % 60;
            }
            if (diffMinutes < 10) {
                holder.time.append(String.valueOf("Created " + diffHours + ":0" + diffMinutes + " hour ago"));
            } else {
                holder.time.append(String.valueOf("Created " + diffHours + ":" + diffMinutes + " hour ago"));
            }
        } else {
            holder.time.append(String.valueOf("Created " + diffMinutes + " min ago"));
        }

        // loading album cover using Glide library
        //Glide.with(mContext).load(subject.getProfileURL()).into(holder.thumbnail);
        Bitmap image = base64ToBitmap(subject.getProfileURL());
        holder.thumbnail.setImageBitmap(image);

        holder.overflow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupMenu(holder.overflow);
            }
        });
    }

    public Bitmap base64ToBitmap(String base64String) {
        byte[] decodedString = Base64.decode(base64String.getBytes(), Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    /**
     * Showing popup menu when tapping on 3 dots
     */
    private void showPopupMenu(View view) {
        // inflate menu
        PopupMenu popup = new PopupMenu(mContext, view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.menu_subject, popup.getMenu());
        popup.setOnMenuItemClickListener(new MyMenuItemClickListener());
        popup.show();
    }

    /**
     * Click listener for popup menu items
     */
    class MyMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {

        public MyMenuItemClickListener() {
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.action_add_favourite:
                    //Toast.makeText(mContext, "Add to favourite", Toast.LENGTH_SHORT).show();
                    return true;
                case R.id.action_play_next:
                    //Toast.makeText(mContext, "Play next", Toast.LENGTH_SHORT).show();
                    return true;
                default:
            }
            return false;
        }
    }

    @Override
    public int getItemCount() {
        return subjectList.size();
    }
}
