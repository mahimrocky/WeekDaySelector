package com.skyhope.weekday;

/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 10/3/2018 at 5:45 PM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 10/3/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.skyhope.weekday.model.WeekModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class WeekDaySelectorAdapter extends RecyclerView.Adapter<WeekDaySelectorAdapter.WeekDayViewHolder> {
    private Context mContext;
    private List<WeekModel> weekModels;
    private int mSelectedPosition = -1;
    private WeekDayViewHolder mHolder;
    private OnItemClickListener mOnItemClickListener;

    public WeekDaySelectorAdapter(Context mContext, OnItemClickListener onItemClickListener) {
        this.mContext = mContext;
        this.weekModels = new ArrayList<>();
        this.mOnItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public WeekDayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new WeekDayViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_week, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull final WeekDayViewHolder holder, final int position) {
        final WeekModel model = weekModels.get(position);
        holder.textViewDay.setText(model.getDayName());

        if (model.isToday()) {
            holder.textViewDay.setBackgroundResource(R.drawable.drawble_circle_deep);
            mSelectedPosition = position;
            mHolder = holder;
        } else if (model.isHoliday()) {
            holder.textViewDay.setBackgroundResource(R.drawable.drawable_circle_red);
        } else {
            holder.textViewDay.setBackgroundResource(R.drawable.drawable_circle_border);
        }

        holder.textViewDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mSelectedPosition != position && !model.isHoliday()) {
                    WeekModel m = weekModels.get(mSelectedPosition);
                    m.setToday(false);
                    model.setToday(true);
                    mSelectedPosition = position;

                    if (mHolder != null) {
                        mHolder.textViewDay.setBackgroundResource(R.drawable.drawable_circle_border);
                    }
                    holder.textViewDay.setBackgroundResource(R.drawable.drawble_circle_deep);
                    mHolder = holder;
                }

                mOnItemClickListener.getOnItemClick(model);
            }
        });

    }

    @Override
    public int getItemCount() {
        return weekModels.size();
    }

    void addItems(List<WeekModel> list) {
        this.weekModels.clear();
        this.weekModels = list;
        notifyDataSetChanged();
    }

    void updateHoliday(Set<Integer> mHolidayList) {
        for (WeekModel model : weekModels) {
            if (mHolidayList.contains(model.getCurrentDay())) {
                model.setHoliday(true);
            }
        }
        notifyDataSetChanged();
    }

    class WeekDayViewHolder extends RecyclerView.ViewHolder {
        TextView textViewDay;

        WeekDayViewHolder(View itemView) {
            super(itemView);
            textViewDay = itemView.findViewById(R.id.text_view_week);
        }
    }

    interface OnItemClickListener {
        void getOnItemClick(WeekModel model);
    }
}
