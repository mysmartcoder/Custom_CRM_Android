package com.customCRM;

/**
 * Created by User on 03/08/2016.
 */
public class PluralConversion
{
    public static String getPlural(String strWordIn)
    {
        String strWord, strWordOut;

        strWordOut = strWordIn;

        if (strWordIn.length() > 0)
        {
            strWord = strWordIn.toLowerCase();

            if (strWord.endsWith("y"))
            {
                strWordOut = strWordIn.substring(0, strWordIn.length() - 1);
                strWordOut = strWordOut + "ies";
            }
            else if (strWord.endsWith("s"))
            {

            }
            else
            {
                strWordOut = strWordOut + "s";
            }
        }
        return strWordOut;
    }
}
