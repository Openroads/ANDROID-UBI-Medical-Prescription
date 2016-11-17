package pl.pk.edu.MedicalPrescription;

import java.util.ArrayList;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Button;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.util.Log;
import android.widget.Toast;
import android.content.Intent;
import android.provider.CalendarContract.Events;
import android.provider.CalendarContract;
import java.util.Date;
import android.widget.LinearLayout;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.widget.AdapterView;

public class MyListAdapter extends ArrayAdapter<Drug> {
    private int layout_resource;
    Context context;
    private Drug drug;
    public MyListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
        this.context=context;
      
    }

    public MyListAdapter(Context context, int resource, ArrayList<Drug> drugs) {
        super(context, resource, drugs);
        this.layout_resource=resource;
        this.context=context;
      
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        
        Holder holder = null;
        Log.i("PMP", "aaa5"+position);
        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(this.getContext());
            v = vi.inflate(R.layout.drugrowlistview, parent,false);
            holder = new Holder();
            //holder.linerv = (LinearLayout) v.findViewById(R.id.linearrow);
            holder.textName = (TextView) v.findViewById(R.id.list_item_name);
            holder.textDate = (TextView) v.findViewById(R.id.list_item_date);
            holder.btnCheck = (Button)   v.findViewById(R.id.check_btn);
            v.setTag(holder);


        }else{
            holder = (Holder) convertView.getTag();
        }
        drug = getItem(position); // the same as ArrayList<drugs> drugs.get(index)

        if (drug != null) {

                holder.textName.setText(drug.getName());
                int remains = drug.getRemainingDoses();
                if(remains>0){
                holder.textDate.setText(DateConverter.dateToString( drug.getDateOfNextDose() ));

                }else if(remains == -1){
                    holder.textDate.setText("Deleted");
                    holder.btnCheck.setVisibility(View.INVISIBLE);
                }
                else{
                    holder.textDate.setText("Finished");
                    holder.btnCheck.setVisibility(View.INVISIBLE);
                }

                
            
            if(holder.btnCheck != null){
                holder.btnCheck.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View w){
                        Drug drug = getItem(position);
                        if(drug.takeDose()==false)
                        {
                            Log.i("PMP", drug.getRemainingDoses()+"w");
                            notifyDataSetChanged();

                        }else{
                            
                            Log.i("PMP", "aaa");
                            notifyDataSetChanged();
                            /*
                            if(without id)
                            SenderToCalendar send = SenderToCalendar();
                            send.setEventInCalendar(this.context,drug);
                            else{
                                edit

                            }*/

                        }
                        

                    }
                });
            }

            /*if(holder.linerv != null){
            holder.linerv.setOnLongClickListener(new View.OnLongClickListener(){
                    public boolean onLongClick(View w)
                    {
                        return true;
                    }

                });
            }*/

        }



        return v;
}

    private static class Holder {
        //public LinearLayout linerv;
        public TextView textName;
        public TextView textDate;
        public Button btnCheck;
    }

}