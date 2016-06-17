package com.scholars.doctor.ui;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.scholars.doctor.R;
import com.scholars.doctor.model.Prescription;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by I311846 on 11-Jun-16.
 */
public class PrescriptionAdapter extends RecyclerView.Adapter<PrescriptionAdapter.ViewHolder> {

    List<Prescription> mItems;
    Context mContext;

    public PrescriptionAdapter(Context ctx) {
        this.mItems = new ArrayList<>();
        this.mContext = ctx;
    }

    public PrescriptionAdapter(Context ctx, List<Prescription> mItems) {
        this.mItems = mItems;
        this.mContext = ctx;
    }

    public void addItem(Prescription prescription) {
        this.mItems.add(prescription);
        notifyDataSetChanged();
    }

    public Prescription getItem(int position) {
        if (mItems != null) {
            return mItems.get(position);
        }
        return null;
    }

    public void setItems(List<Prescription> items) {
        this.mItems = items;
        notifyDataSetChanged();
    }

    public void setItem(int position, Prescription prescription) {
        if (mItems != null) {
            mItems.set(position, prescription);
            notifyItemChanged(position);
        }
    }

    public void updateItem(Prescription prescription) {
        if (mItems != null) {
            int i = 0;
            for (; i < mItems.size(); i++) {
                if (mItems.get(i).getId().equals(prescription.getId())) {
                    mItems.set(i, prescription);
                    break;
                }
            }
            notifyItemChanged(i);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recycler_view_prescription, parent, false);
        return new ViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Prescription p = mItems.get(position);
        holder.title.setText(p.getTitle());
        holder.text.setText(p.getMedicines());
        holder.status.setText(p.getStatus());
        int color;
        switch (p.getStatus()) {
            case "PENDING":
                color = android.R.color.holo_red_dark;
                break;
            case "SHIPPED":
                color = android.R.color.holo_orange_dark;
                break;
            case "DELIVERED":
                color = android.R.color.holo_green_dark;
                break;
            default:
                color = android.R.color.black;
                break;
        }
        holder.status.setTextColor(mContext.getColor(color));
    }

    @Override
    public int getItemCount() {
        if (mItems != null) {
            return mItems.size();
        }
        return 0;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public TextView text;
        public TextView status;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.presc_title);
            text = (TextView) itemView.findViewById(R.id.presc_text);
            status = (TextView) itemView.findViewById(R.id.presc_status);
        }
    }
}
