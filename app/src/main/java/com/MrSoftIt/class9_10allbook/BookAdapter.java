package com.MrSoftIt.class9_10allbook;

import android.content.Intent;
import android.media.Image;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;

public class BookAdapter extends FirestoreRecyclerAdapter<NoteClass,BookAdapter.NotViewHolde> {


    String bookName;

    private OnItemClickListener listener;

    public BookAdapter(@NonNull FirestoreRecyclerOptions<NoteClass> options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull final NotViewHolde holder, int position, @NonNull NoteClass model) {
        bookName = model.getName();
        //holder.BookName.setText(model.getName());
        String Url = model.getImageUrl();
        Picasso.get().load(Url).into(holder.BookIamge);


    }

    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.book_list_single_item,
                parent, false);
        return new NotViewHolde(v);
    }

    class NotViewHolde extends RecyclerView.ViewHolder{

       // TextView BookName;
        ImageView BookIamge;



        public NotViewHolde(@NonNull final View itemView) {
            super(itemView);


            BookIamge = itemView.findViewById(R.id.Book_Image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

    public interface OnItemClickListener {
        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
