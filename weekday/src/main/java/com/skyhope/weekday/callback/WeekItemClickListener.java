package com.skyhope.weekday.callback;

/*
 *  ****************************************************************************
 *  * Created by : Md Tariqul Islam on 10/3/2018 at 6:55 PM.
 *  * Email : tariqul@w3engineers.com
 *  *
 *  * Purpose:
 *  *
 *  * Last edited by : Md Tariqul Islam on 10/3/2018.
 *  *
 *  * Last Reviewed by : <Reviewer Name> on <mm/dd/yy>
 *  ****************************************************************************
 */

import com.skyhope.weekday.model.WeekModel;

public interface WeekItemClickListener {

    void onGetItem(WeekModel model);
}
