package com.trueearning.pro;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

@Keep
public class VPAdapter extends RecyclerView.Adapter<VPAdapter.ViewHolder> {

    ArrayList<ViewPagerItem> viewPagerItemArrayList;

    public VPAdapter(ArrayList<ViewPagerItem> viewPagerItemArrayList) {
        this.viewPagerItemArrayList = viewPagerItemArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewpager_item,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        ViewPagerItem viewPagerItem = viewPagerItemArrayList.get(position);

        holder.mainHeadingA.setText(viewPagerItem.mainHeading);
        holder.tvHeading1A.setText(viewPagerItem.heading1);
        holder.tvHeading2A.setText(viewPagerItem.heading2);
        holder.tvHeading3A.setText(viewPagerItem.heading3);
        holder.tvHeading4A.setText(viewPagerItem.heading4);

    }

    @Override
    public int getItemCount() {
        return viewPagerItemArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{


        TextView mainHeadingA,tvHeading1A,tvHeading2A,tvHeading3A,tvHeading4A;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mainHeadingA = itemView.findViewById(R.id.main_heading);
            tvHeading1A = itemView.findViewById(R.id.tvHeading1);
            tvHeading2A = itemView.findViewById(R.id.tvHeading2);
            tvHeading3A = itemView.findViewById(R.id.tvHeading3);
            tvHeading4A = itemView.findViewById(R.id.tvHeading4);

        }
    }

}
