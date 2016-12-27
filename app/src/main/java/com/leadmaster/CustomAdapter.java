package com.leadmaster;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;

        import java.util.ArrayList;
        import java.util.List;
        import java.util.Locale;

/**
 * Created by VARIANCEINFOTECH on 4/13/16.
 */
public class CustomAdapter extends ArrayAdapter{

    private List<Accounts_Data> list;
    private ArrayList<Accounts_Data> arraylist;
    private int resource;
    private LayoutInflater layoutInflater;
    Context context;

    public CustomAdapter(Context context, int resource, List objects) {
        super(context, resource, objects);
        list=objects;
        this.context=context;
        this.resource=resource;
        layoutInflater= (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        this.arraylist = new ArrayList<Accounts_Data>();
        this.arraylist.addAll(list);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if(convertView == null)
        {
            convertView = layoutInflater.inflate(R.layout.raw_accounts,null);
        }

//        TextView txentered=(TextView)convertView.findViewById(R.id.txentered);
//        TextView txcompany=(TextView)convertView.findViewById(R.id.txcompany);
//        TextView txleadstatus=(TextView)convertView.findViewById(R.id.txleadstatus);
//        TextView txleadsource=(TextView)convertView.findViewById(R.id.txleadsource);
   //     TextView txeventstatus=(TextView)convertView.findViewById(R.id.txeventstatus);
        //  final Button btnmenu=(Button)convertView.findViewById(R.id.btnmenu);

//        txentered.setText(list.get(position).getEntered());
//        txcompany.setText(list.get(position).getCompany());
//        txleadstatus.setText(list.get(position).getLeadStatus());
//        txleadsource.setText(list.get(position).getLeadSource());

    //    txaccount.setText(list.get(position).getAccount());
      //  txcallback.setText(list.get(position).getCallback());
       // txstarttime.setText(list.get(position).getStartTime());
       // txendtime.setText(list.get(position).getEndTime());
      //  txeventstatus.setText(list.get(position).getEventstatus());
/*
        btnmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                *//*PopupMenu popup = new PopupMenu(context,btnmenu);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        Toast.makeText(context, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                        return true;
                    }
                });

                popup.show();//showing popup menu*//*

                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custommenu);
                dialog.show();
            }
        });*/

        return convertView;
    }

    /*public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        list.clear();
        if (charText.length() == 0) {
            list.addAll(arraylist);
        }
        else
        {
            for (Accounts_Data wp : arraylist)
            {
                if (wp.getAccount().toLowerCase(Locale.getDefault()).contains(charText))
                {
                    list.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }*/
}
