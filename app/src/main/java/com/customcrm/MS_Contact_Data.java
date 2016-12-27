package com.customCRM;

/**
 * Created by User on 17/10/2016.
 */
public class MS_Contact_Data
{
    private String entered;
    private String cont;
    private String acc;
    private String lead;
    private String campaign;
    private String acc_mgr;
    private String phone;
    private String lat,lang;
    private String recordId;
    private String contactId;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAcc() {
        return acc;
    }

    public void setAcc(String acc) {
        this.acc = acc;
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

    public String getCont() {
        return cont;
    }

    public void setCont(String cont) {
        this.cont = cont;
    }

    public String getEntered() {
        return entered;
    }

    public void setEntered(String entered) {
        this.entered = entered;
    }

    public String getLead() {
        return lead;
    }

    public void setLead(String lead) {
        this.lead = lead;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setRecordId(String recordId) {
        this.recordId = recordId;
    }

    public String getRecordId() {
        return recordId;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
