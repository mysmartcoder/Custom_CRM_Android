package com.leadmaster;

/**
 * Created by User on 11/11/2016.
 */
public class Month_Data
{
    String monthName;
    String monthId;

    public void setMonthId(String monthId) {
        this.monthId = monthId;
    }

    public String getMonthId() {
        return monthId;
    }

    public void setMonthName(String monthName) {
        this.monthName = monthName;
    }

    public String getMonthName() {
        return monthName;
    }

    @Override
    public String toString() {
        return monthName;
    }

    @Override
    public boolean equals(Object obj)
    {
        if(obj instanceof Month_Data)
        {
            Month_Data c = (Month_Data)obj;
            if(c.getMonthName().equals(monthName) && c.getMonthId().equals(monthId))
                return true;
        }

        return false;
    }
}
