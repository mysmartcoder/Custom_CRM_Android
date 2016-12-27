package com.leadmaster;

/**
 * Created by User on 02/09/2016.
 */
public class MS_Records_Data
{
    private String entered;
    private String company;
    private String leadStatus;
    private String leadSource;
    private String phone;
    private String recordId;
    private String lat,lang;
    private String campaign;
    private String acc_mgr;

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public String getEntered() {
        return entered;
    }

    public void setEntered(String entered) {
        this.entered = entered;
    }

    public String getLeadStatus() {
        return leadStatus;
    }

    public void setLeadStatus(String leadStatus) {
        this.leadStatus = leadStatus;
    }

    public String getLeadSource() {
        return leadSource;
    }

    public void setLeadSource(String leadSource) {
        this.leadSource = leadSource;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRecordId() {
        return recordId;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getLang() {
        return lang;
    }

    public String getLat() {
        return lat;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getAcc_mgr() {
        return acc_mgr;
    }

    public void setAcc_mgr(String acc_mgr) {
        this.acc_mgr = acc_mgr;
    }

    public String getCampaign() {
        return campaign;
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }
}

