# WeekDaySelector
This library help to pick date of per week base date.

# Root Gradle
```sh
    allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```

# App Gradle:

```sh
dependencies {
	         implementation 'com.github.mahimrocky:WeekDaySelector:1.0.0'
	}
```

# XML Section

```sh
    <com.skyhope.weekday.WeekDaySelector
        android:id="@+id/weekDaySelector"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        app:holiday_circle_color="@color/colorAccent"
        app:layout_constraintLeft_toLeftOf="parent"
        app:layout_constraintRight_toRightOf="parent"
        app:layout_constraintTop_toTopOf="parent"
        app:selected_date_circle_color="@color/colorPrimary" />
```

### By below options you can change or customize the view

| Attributes | Purpose |
| ------ | ------ |
| ```app:holiday_circle_color```|  To change holiday circle color|
| ```app:selected_date_circle_color```|  To change selected date circle color|

### How to set listener and holiday:

You can set holiday and set date selected listenr by using 
```sh
  weekDaySelector = findViewById(R.id.weekDaySelector);

        Set<Integer> holiday = new HashSet<>();
        holiday.add(Holiday.SUNDAY);
        weekDaySelector.setHoliday(holiday);

        weekDaySelector.setWeekItemClickListener(this);
    }

    @Override
    public void onGetItem(WeekModel model) {
        Toast.makeText(this, model.getMonth(), Toast.LENGTH_SHORT).show();
    }
```

please implement ``` WeekItemClickListener ``` in your activity
