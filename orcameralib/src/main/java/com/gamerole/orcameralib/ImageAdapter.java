package com.gamerole.orcameralib;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.gamerole.orcameralib.util.DimensionUtil;

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
    private int offset_22 = DimensionUtil.dpToPx(22);
    private int offset_28 = DimensionUtil.dpToPx(28);

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
            setVisiable(holder,true);
            Glide.with(holder.imageView).load(o).into(holder.imageView);
        }else if(o instanceof File){
            setVisiable(holder,true);
            Glide.with(holder.imageView).load(o).into(holder.imageView);
        }else {
            setVisiable(holder,false);
            if(position < 3 && picNum != 1){
                Drawable srcdrawable = ResourcesCompat.getDrawable(holder.imageViewBg.getResources(), imageRes[position], null);
                Glide.with(holder.imageViewBg).load(srcdrawable).transform(new RotateTransformation(90f)).into(holder.imageViewBg);
                holder.tv1.setVisibility(View.GONE);
                holder.tv2.setVisibility(View.VISIBLE);
                holder.tv2.setText(strRes[position]);
                int left;
                if(strRes[position].length() == 3){
                    left = offset_28;
                }else {
                    left = offset_22;
                }
                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.tv2.getLayoutParams();
                params.leftMargin = left;
                holder.tv2.setLayoutParams(params);
            }else {
                holder.imageViewBg.setImageResource(R.color.color_8B8F91);
                holder.tv2.setVisibility(View.GONE);
                holder.tv1.setVisibility(View.VISIBLE);
                if(picNum == 1){
                    holder.tv1.setText("拍照");
                }else {
                    holder.tv1.setText("其他");
                }
            }
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

    private void setVisiable(ImageViewHolder holder,boolean visiable){
        int visiable1;
        int visiable2;
        if(visiable){
            visiable1 = View.VISIBLE;
            visiable2 = View.GONE;
        }else {
            visiable1 = View.GONE;
            visiable2 = View.VISIBLE;
        }
        holder.imageView.setVisibility(visiable1);
        holder.imageViewBg.setVisibility(visiable2);
        holder.tv2.setVisibility(View.GONE);
        holder.tv1.setVisibility(View.GONE);
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
        Log.i("infos","setFresh -- currentIndex " + currentIndex);
        dataList.set(currentIndex,o);
        if(currentIndex < dataList.size() - 1){
            currentIndex ++;
        }

        Log.i("infos","setFresh -3- currentIndex " + currentIndex);
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
    ImageView imageViewBg;
    public ImageViewHolder(View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.image);
        parent = itemView.findViewById(R.id.parent);
        tv1 = itemView.findViewById(R.id.tv1);
        tv2 = itemView.findViewById(R.id.tv2);
        imageViewBg = itemView.findViewById(R.id.image_bg);
    }

}

