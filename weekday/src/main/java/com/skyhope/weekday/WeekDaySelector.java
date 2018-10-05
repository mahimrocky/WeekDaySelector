package com.skyhope.weekday;

/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 10/3/2018 at 5:09 PM.
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
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.skyhope.weekday.callback.WeekItemClickListener;
import com.skyhope.weekday.data.CommonUtils;
import com.skyhope.weekday.model.WeekModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class WeekDaySelector extends LinearLayout implements WeekDaySelectorAdapter.OnItemClickListener {

    private String TAG = "MyWeekDaySelector";
    private RecyclerView recyclerView;
    private TextView textViewCurrentMonth;

    private WeekDaySelectorAdapter mAdapter;

    // User listener
    private WeekItemClickListener mWeekItemClickListener;

    // App property
    private String currentDayMonth;
    private boolean isHoliday;
    private Set<Integer> mHolidayList = new HashSet<>();

    private List<WeekModel> weekList = new ArrayList<>();


    /*
     *  User choice property
     * */

    // Selected date color
    private static final int DEFAULT_SELECTED_DATE_CIRCLE_COLOR = Color.rgb(72, 179, 255);
    private int selectedDateCircleColor;

    // Holiday color
    private static final int DEFAULT_HOLIDAY_CIRCLE_COLOR = Color.rgb(255, 0, 0);
    private int holidayCircleColor;

    public WeekDaySelector(Context context) {
        super(context);
    }

    public WeekDaySelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        initAttrs(attrs);
        initView(context);
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        recyclerView = findViewById(R.id.recycler_view);
        textViewCurrentMonth = findViewById(R.id.current_date_month);

        recyclerView.setHasFixedSize(true);
        mAdapter = new WeekDaySelectorAdapter(getContext(), this);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        recyclerView.setAdapter(mAdapter);

        getAllDate();

        setSelectedDateCircleColor(getContext());
        setHolidayColorCircle(getContext());
    }

    @Override
    public void getOnItemClick(WeekModel model) {
        textViewCurrentMonth.setText(model.getCurrentDateName());
        if (mWeekItemClickListener != null) {
            mWeekItemClickListener.onGetItem(model);
        }
    }

    private void initView(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View view = inflater.inflate(R.layout.item_calender, this);

        }
    }

    private void initAttrs(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.WeekDaySelector);

        selectedDateCircleColor = typedArray.getColor(R.styleable.WeekDaySelector_selected_date_circle_color, DEFAULT_SELECTED_DATE_CIRCLE_COLOR);

        holidayCircleColor = typedArray.getColor(R.styleable.WeekDaySelector_holiday_circle_color, DEFAULT_HOLIDAY_CIRCLE_COLOR);

        typedArray.recycle();

    }

    private void getAllDate() {
        SimpleDateFormat fmt = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);
        SimpleDateFormat monthFormatter = new SimpleDateFormat("MMMM", Locale.ENGLISH);
        SimpleDateFormat selectedDateFormatter = new SimpleDateFormat("MMMM d, yyyy", Locale.ENGLISH);
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        String toDay = fmt.format(cal.getTime());
        int position = -1;
        int currentDay = 0;
        cal.clear();
        cal.set(year, month - 1, 1);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        weekList.clear();
        for (int i = 0; i < 60; i++) {
            int day = cal.get(Calendar.DAY_OF_WEEK);
            String dayName = "";
            String date = fmt.format(cal.getTime());

            switch (day) {
                case Calendar.SUNDAY:
                    dayName = CommonUtils.SUNDAY;
                    currentDay = Calendar.SUNDAY;
                    break;
                case Calendar.MONDAY:
                    dayName = CommonUtils.MONDAY;
                    currentDay = Calendar.MONDAY;
                    break;
                case Calendar.TUESDAY:
                    dayName = CommonUtils.TUESDAY;
                    currentDay = Calendar.TUESDAY;
                    break;
                case Calendar.WEDNESDAY:
                    dayName = CommonUtils.WEDNESDAY;
                    currentDay = Calendar.WEDNESDAY;
                    break;
                case Calendar.THURSDAY:
                    dayName = CommonUtils.THURSDAY;
                    currentDay = Calendar.THURSDAY;
                    break;
                case Calendar.FRIDAY:
                    dayName = CommonUtils.FRIDAY;
                    currentDay = Calendar.FRIDAY;
                    break;
                case Calendar.SATURDAY:
                    dayName = CommonUtils.SATURDAY;
                    currentDay = Calendar.SATURDAY;
                    break;
            }
            WeekModel model = new WeekModel(dayName);
            model.setDate(date);

            if (toDay.equals(date) && position == -1) {
                position = i;
                model.setToday(true);

                currentDayMonth = selectedDateFormatter.format(cal.getTime());
                textViewCurrentMonth.setText(currentDayMonth);
            }

            model.setHoliday(isHoliday);

            model.setCurrentDateName(selectedDateFormatter.format(cal.getTime()));

            model.setMonth(monthFormatter.format(cal.getTime()));

            model.setCurrentDay(currentDay);

            weekList.add(model);
            cal.add(Calendar.DAY_OF_MONTH, 1);
        }
        mAdapter.addItems(weekList);
        recyclerView.scrollToPosition(position);
    }

    /*
     * Set color on Selected Date circle color
     * */
    private void setSelectedDateCircleColor(Context context) {
        GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.drawble_circle_deep);

        if (mDrawable != null) {
            mDrawable.setColor(selectedDateCircleColor);
        }

      /*  GradientDrawable drawable = (GradientDrawable)tex.getBackground();
        drawable.setStroke(3, Color.RED);*/
    }

    /*
     * Set color on holiday circle
     * */

    private void setHolidayColorCircle(Context context) {
        GradientDrawable mDrawable = (GradientDrawable) ContextCompat.getDrawable(context, R.drawable.drawable_circle_red);

        if (mDrawable != null) {
            mDrawable.setColor(holidayCircleColor);
        }
    }



    /*
     *
     * ******************** User Edit section ***************
     * */


    /*
     * User can set holiday list.
     * From Holiday interface
     * */
    public void setHoliday(Set<Integer> holidayList) {
        if (holidayList.size() > 6) {
            try {
                throw new Exception("You cannot add all day as Holiday");
            } catch (Exception e) {
                e.printStackTrace();
            }
            return;
        }

        mHolidayList.clear();
        mHolidayList = holidayList;

        if (mHolidayList.size() == 0) {
            // No holiday
            isHoliday = false;
        } else {
            mAdapter.updateHoliday(mHolidayList);
        }
    }

    /*
     * User can get Holiday item selected listener
     * */
    public void setWeekItemClickListener(WeekItemClickListener weekItemClickListener) {
        this.mWeekItemClickListener = weekItemClickListener;
    }

}
