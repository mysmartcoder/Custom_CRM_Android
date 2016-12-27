package com.leadmaster;

/**
 * Created by User on 08/07/2016.
 */
public class Opportunity_Data
{
    private String record_id;
    private String opp_id;
    private String url;

    private String policy_name;
    private String account;
    private String opportunity;
    private String opportunity_stage;
    private String acct_mgr;
    private String phone;

    public String getAcct_mgr() {
        return acct_mgr;
    }

    public void setAcct_mgr(String acct_mgr) {
        this.acct_mgr = acct_mgr;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public String getOpportunity() {
        return opportunity;
    }

    public void setOpportunity(String opportunity) {
        this.opportunity = opportunity;
    }

    public String getOpportunity_stage() {
        return opportunity_stage;
    }

    public void setOpportunity_stage(String opportunity_stage) {
        this.opportunity_stage = opportunity_stage;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPolicy_name() {
        return policy_name;
    }

    public void setPolicy_name(String policy_name) {
        this.policy_name = policy_name;
    }

    public void setOpp_id(String opp_id) {
        this.opp_id = opp_id;
    }

    public String getOpp_id() {
        return opp_id;
    }

    public void setRecord_id(String record_id) {
        this.record_id = record_id;
    }

    public String getRecord_id() {
        return record_id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
