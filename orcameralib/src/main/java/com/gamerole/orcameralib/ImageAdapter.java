package com.gamerole.orcameralib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private int[] imageRes = {R.drawable.ic_icon_main_left,R.drawable.ic_icon_no_main_left,R.drawable.ic_icon_main_back};
    private String[] strRes = {"主驾侧身","副驾侧身","正后方"};
    private int picNum;

    public ImageAdapter(Context context,int num){
        dataList = new ArrayList();
        inflater = LayoutInflater.from(context);
        for(int i = 0; i< num;i++){
            dataList.add("");
        }
        picNum = num;
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

        if(position < 3){
            holder.tv1.setVisibility(View.GONE);
            holder.tv2.setVisibility(View.VISIBLE);
            holder.tv2.setText(strRes[position]);
            Drawable drawable = holder.tv1.getResources().getDrawable(imageRes[position]);
            drawable.setBounds(0,0,drawable.getMinimumWidth(),drawable.getMinimumHeight());
            holder.tv2.setCompoundDrawables(null,null,null,drawable);
        }else {
            holder.tv2.setVisibility(View.GONE);
            holder.tv1.setVisibility(View.VISIBLE);
        }
        Log.i("infos",position + "  ");


        if(picNum == 1){
            holder.tv2.setVisibility(View.GONE);
            holder.tv1.setVisibility(View.VISIBLE);
            holder.tv1.setText("拍照");
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

    public ArrayList<String> getData(){
        ArrayList<String> files = new ArrayList();
        for(Object o : dataList){
            if(o instanceof File){
                files.add(((File) o).getAbsolutePath());
            }
        }
        return files;
    }

    public boolean isReady(){
        boolean b = false;
        int count = 0;
        int num;
        if(picNum < 3){
            num = picNum;
        }else {
            num = 3;
        }
        for(int i = 0; i < num;i++){
            Object o = dataList.get(i);
            if(o instanceof File){
                count ++;
            }
        }
        if(count == num){
            b = true;
        }
        return b;
    }

}

class ImageViewHolder extends RecyclerView.ViewHolder{

    ImageView imageView;
    View parent;
    TextView tv1;
    TextView tv2;
    public ImageViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
        parent = itemView.findViewById(R.id.parent);
        tv1 = itemView.findViewById(R.id.tv1);
        tv2 = itemView.findViewById(R.id.tv2);
    }

}

