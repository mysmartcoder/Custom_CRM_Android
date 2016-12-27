package com.leadmaster;

/**
 * Created by User on 05/07/2016.
 */
public class Callbacktask_Data
{
    private String rcd_Id;
    private String callback_Id;

    private String companyName;
    private String eventName;
    private String startTime;
    private String endTime;
    private String eventStatus;

    public String getCompanyName() {
        return companyName;
    }

    public String getEventName() {
        return eventName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getCallback_Id() {
        return callback_Id;
    }

    public void setCallback_Id(String callback_Id) {
        this.callback_Id = callback_Id;
    }

    public String getRcd_Id() {
        return rcd_Id;
    }

    public void setRcd_Id(String rcd_Id) {
        this.rcd_Id = rcd_Id;
    }
}
