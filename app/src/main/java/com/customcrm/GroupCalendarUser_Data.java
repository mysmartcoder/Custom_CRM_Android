package com.customCRM;

/**
 * Created by User on 10/11/2016.
 */
public class GroupCalendarUser_Data
{
    private String LogonId;
    private String FullName;
    private String BusinessType;

    public void setLogonId(String logonId) {
        LogonId = logonId;
    }

    public String getLogonId() {
        return LogonId;
    }

    public void setFullName(String fullName) {
        FullName = fullName;
    }

    public String getFullName() {
        return FullName;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public String getBusinessType() {
        return BusinessType;
    }


    @Override
    public String toString() {
        return FullName;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof GroupCalendarUser_Data)
        {
            GroupCalendarUser_Data c = (GroupCalendarUser_Data)obj;
            if(c.getFullName().equals(FullName) && c.getLogonId().equals(LogonId) && c.getBusinessType().equals(BusinessType))
                return true;
        }

        return false;
    }
}
