package com.secureidltd.belemaogan.sqliteproject;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder>{

    private OnItemClickListener onItemClickListener;
    private Cursor cursor;
    private Context context;

    public MyAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(Cursor cursor);
    }

    public void addCursor(Cursor cursor){
        this.cursor = cursor;
        this.notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_view_layout, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        if (!cursor.moveToPosition(i)){
            return;
        }

        String name = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_2));
        String surname = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_3));
        String mark = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_4));
        long id = cursor.getInt(cursor.getColumnIndex(DatabaseHelper.COLUMN_1));

        myViewHolder.nameTextView.setText(context.getString(R.string.full_name, name, surname));
        myViewHolder.markTextView.setText(context.getString(R.string.mark_text, mark));
        myViewHolder.itemView.setTag(id);
        myViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(cursor);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (cursor != null){
            return cursor.getCount();
        }
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        TextView nameTextView;
        TextView markTextView;

        MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.name_text_view);
            markTextView = itemView.findViewById(R.id.mark_tv);
        }
    }
}
