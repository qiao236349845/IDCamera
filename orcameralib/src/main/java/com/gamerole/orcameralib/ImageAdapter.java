package com.gamerole.orcameralib;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * create by kevinqiao
 * 2020/4/24
 */
public class ImageAdapter extends RecyclerView.Adapter<ImageViewHolder>{


    private LayoutInflater inflater;
    private List dataList;
    private int currentIndex = 0;
    private OnViewClickListener listener;

    public ImageAdapter(Context context){
        dataList = new ArrayList();
        inflater = LayoutInflater.from(context);
        for(int i = 0; i< 8;i++){
            dataList.add("");
        }
    }

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        this.getL
        View view = inflater.inflate(R.layout.item_image,null);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder holder, final int position) {
        Object o = dataList.get(position);
        if(o instanceof String && !TextUtils.isEmpty((String)o)){
            Glide.with(holder.imageView).load(o).into(holder.imageView);
        }else if(o instanceof File){
            Glide.with(holder.imageView).load(o).into(holder.imageView);
        }

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onViewClick(v.getId(),position);
                setIndex(position);
            }
        });
        if(currentIndex == position){
            holder.parent.setBackgroundResource(R.drawable.bg_radius2_line);
        }else {
            holder.parent.setBackgroundResource(R.color.transparent);
        }

    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setFresh(int index, Object o){
        dataList.set(index,o);
        notifyDataSetChanged();
    }
    public void setFresh(Object o){
        dataList.set(currentIndex,o);
        if(currentIndex < dataList.size() - 1){
            currentIndex ++;
        }
        notifyDataSetChanged();
    }

    public void addData(Object o){
        dataList.add(o);
    }

    public void setIndex(int index){
        this.currentIndex = index;
        notifyDataSetChanged();
    }


    public void setListener(OnViewClickListener l){
        listener = l;
    }

    interface OnViewClickListener{
        void onViewClick(int id, int position);
    }

}

class ImageViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    View parent;
    public ImageViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
        parent = itemView.findViewById(R.id.parent);
    }

}

