package com.example.mike.sdcardscanner.ui;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.mike.sdcardscanner.R;

import java.io.File;
import java.util.List;

public class FileRecyclerAdapter extends RecyclerView.Adapter<FileRecyclerAdapter.ViewHolder> {

    List<File> files;

    public FileRecyclerAdapter(List<File> files) {
        this.files = files;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from( viewGroup.getContext() ).inflate(R.layout.file_item, viewGroup, false );
        return new ViewHolder( view );
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        viewHolder.fiName.setText( files.get(i).getName() );
        viewHolder.fiSize.setText( ""+files.get(i).length()+"B" );
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView fiName;
        TextView fiSize;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            fiName = itemView.findViewById( R.id.fiName );
            fiSize = itemView.findViewById( R.id.fiSize );
        }
    }
}
