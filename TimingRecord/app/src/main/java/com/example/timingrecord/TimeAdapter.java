package com.example.timingrecord;


import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.ViewHolder>{
    private ArrayList<Time> listTime;
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.time_adapter,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position,listener);
    }

    @Override
    public int getItemCount() {
        return listTime.size();
    }

    public interface OnItemClickListener{
        void onItemClick(Time time);
    }

    public TimeAdapter(ArrayList<Time>listTime,OnItemClickListener listener){
        this.listTime=listTime;
        this.listener=listener;
    }
    //llamamos al view holder
    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTime;
        private TextView txtDistance;
        private TextView txtDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTime=(TextView)itemView.findViewById(R.id.txtTime);
            txtDistance=(TextView)itemView.findViewById(R.id.txtDistance);
            txtDelete=(TextView)itemView.findViewById(R.id.txtDelete);
        }

        public void bind(final int position, final OnItemClickListener listener){
            final Time time=listTime.get(position);
            txtDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (new Time(itemView.getContext(),true).delete(time.getId())){
                        listTime.remove(position);
                        notifyDataSetChanged();
                    }
                }
            });
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onItemClick(time);
                }
            });
            txtDistance.setText(time.getDistance());
            txtTime.setText("en "+time.getTime());
        }
    }


}
