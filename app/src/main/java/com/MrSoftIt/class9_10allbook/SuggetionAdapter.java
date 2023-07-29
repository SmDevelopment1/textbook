package com.MrSoftIt.class9_10allbook;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;

public class SuggetionAdapter extends FirestoreRecyclerAdapter<Sugg_Note, SuggetionAdapter.NotViewHolde> {



    int PageNumber;

    private OnItemClickListener listenerC;



    public SuggetionAdapter(@NonNull FirestoreRecyclerOptions<Sugg_Note> options) {
        super(options);
    }



    @Override
    protected void onBindViewHolder(@NonNull final NotViewHolde holder, int position, @NonNull Sugg_Note model) {
        holder.sub_name.setText(model.getSubName());
        holder.sub_topic.setText(model.getSubTopic());
        if (holder.sub_topic.getText().toString().contains("_n")){
            String subName = holder.sub_topic.getText().toString().replaceAll("_n",System.getProperty("line.separator"));
            holder.sub_topic.setText(subName);
        }


         holder.date.setText(model.getDate());



       // Picasso.get().load(Url).into(holder.BookIamge);


    }

    @NonNull
    @Override
    public NotViewHolde onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.suggestion_single_laout,
                parent, false);
        return new NotViewHolde(v);
    }




    class NotViewHolde extends RecyclerView.ViewHolder{

        TextView sub_name,sub_topic,date;



        public NotViewHolde(@NonNull final View itemView) {
            super(itemView);

            sub_name = itemView.findViewById(R.id.suggestion_sub_name);
            sub_topic = itemView.findViewById(R.id.suggestion_topic);
            date = itemView.findViewById(R.id.date);


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

        public void setOnItemClickListener(SuggetionAdapter.OnItemClickListener listener) {
            this.listenerC = listener;
        }
}
