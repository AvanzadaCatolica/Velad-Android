package com.mac.velad.today;

/**
 * Created by ruenzuo on 27/03/16.
 */
public class TodayViewModel {

    private Record record;
    private BasicPoint basicPoint;

    public TodayViewModel(Record record, BasicPoint basicPoint) {
        this.record = record;
        this.basicPoint = basicPoint;
    }

    public Record getRecord() {
        return record;
    }

    public void setRecord(Record record) {
        this.record = record;
    }

    public BasicPoint getBasicPoint() {
        return basicPoint;
    }

    public void setBasicPoint(BasicPoint basicPoint) {
        this.basicPoint = basicPoint;
    }
}
