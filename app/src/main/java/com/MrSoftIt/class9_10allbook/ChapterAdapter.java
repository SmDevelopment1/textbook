package com.MrSoftIt.class9_10allbook;

import android.content.Context;
import android.content.Intent;
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

public class ChapterAdapter extends FirestoreRecyclerAdapter<ChapterNote, ChapterAdapter.NotViewHolde> {



    int PageNumber;

    private OnItemClickListener listenerC;



    public ChapterAdapter(@NonNull FirestoreRecyclerOptions<ChapterNote> options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull final NotViewHolde holder, int position, @NonNull ChapterNote model) {
        holder.cName.setText(model.getCname());
        PageNumber = model.getPageNumber();



       // Picasso.get().load(Url).into(holder.BookIamge);


    }

    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chapter_list_sinngle_item,
                parent, false);
        return new NotViewHolde(v);
    }


    class NotViewHolde extends RecyclerView.ViewHolder{

        TextView cName;
       // ImageView BookIamge;


        public NotViewHolde(@NonNull final View itemView) {
            super(itemView);

            cName = itemView.findViewById(R.id.chapterID);
           // BookIamge = itemView.findViewById(R.id.Book_Image);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listenerC != null) {
                        listenerC.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }

        public interface OnItemClickListener {
            void onItemClick(DocumentSnapshot documentSnapshot, int position);
        }

        public void setOnItemClickListener(ChapterAdapter.OnItemClickListener listener) {
            this.listenerC = listener;
        }
}
