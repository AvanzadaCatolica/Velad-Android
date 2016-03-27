package com.mac.velad.week;

import com.mac.velad.today.BasicPoint;

/**
 * Created by ruenzuo on 27/03/16.
 */
public class WeekViewModel {

    private BasicPoint basicPoint;
    private int weekCount;

    public WeekViewModel(BasicPoint basicPoint, int weekCount) {
        this.basicPoint = basicPoint;
        this.weekCount = weekCount;
    }

    public BasicPoint getBasicPoint() {
        return basicPoint;
    }

    public void setBasicPoint(BasicPoint basicPoint) {
        this.basicPoint = basicPoint;
    }

    public int getWeekCount() {
        return weekCount;
    }

    public void setWeekCount(int weekCount) {
        this.weekCount = weekCount;
    }
}
