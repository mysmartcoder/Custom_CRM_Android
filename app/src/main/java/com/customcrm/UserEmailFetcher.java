package com.customCRM;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.util.Log;

/**
 * Created by User on 17/11/2016.
 */
public class UserEmailFetcher
{

    static String getEmail(Context context)
    {
        AccountManager accountManager = AccountManager.get(context);
        Account account = getAccount(accountManager);

        //For all registered accounts;
		/*try
        {
			Account[] accounts = AccountManager.get(context).getAccounts();
			for (Account account : accounts) {
				Item item = new Item( account.type, account.name);
				accountsList.add(item);
			}
		}
        catch (Exception e)
        {
			Log.i("Exception", "Exception:" + e);
		}*/

        if (account == null)
        {
            return null;
        }
        else
        {
            return account.name;
        }
    }

    private static Account getAccount(AccountManager accountManager)
    {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0)
        {
            account = accounts[0];
        }
        else
        {
            accounts=accountManager.getAccounts();
            if(accounts.length>0)
            {
                account=accounts[0];
            }
            else
            {
                account = null;
            }
        }
        return account;
    }
}