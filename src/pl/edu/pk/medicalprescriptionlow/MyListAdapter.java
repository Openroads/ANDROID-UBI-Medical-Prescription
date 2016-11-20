package pl.edu.pk.medicalprescriptionlow;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;

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

        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(this.getContext());
            v = vi.inflate(layout_resource, parent,false);
            holder = new Holder();
            holder.textName = (TextView) v.findViewById(R.id.list_item_name);
            holder.textDate = (TextView) v.findViewById(R.id.list_item_date);
            holder.btnCheck = (Button)   v.findViewById(R.id.check_btn);
            holder.textHurry= (TextView) v.findViewById(R.id.texthurry);
            holder.textRemain=(TextView) v.findViewById(R.id.remain);
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
                    Date currdate = new Date();
                    if((currdate.compareTo(drug.getDateOfNextDose())) >=0){
                        holder.btnCheck.setText("Take it!");
                        holder.textHurry.setText("You missed !");
                        holder.textDate.setTextColor(Color.RED);
                    }else{
                        holder.btnCheck.setText("Take");
                        holder.textHurry.setText("Next dose:");
                        holder.textDate.setText(DateConverter.dateToString( drug.getDateOfNextDose() ));
                        holder.textDate.setTextColor(Color.GREEN);
                    }
                    if(remains>1)
                    {
                        holder.textRemain.setText(remains+" doses remaining");
                    }else{
                        holder.textRemain.setText(remains+" dose remaining");
                    }
                }else if(remains == -1){
                    holder.textDate.setText("Deleted");
                    holder.btnCheck.setVisibility(View.INVISIBLE);
                    SenderToCalendar delete = new SenderToCalendar();
                    delete.deleteEventFromCalendar(MyListAdapter.this.context,drug);
                }
                else{
                    holder.textDate.setText("Finished");
                    holder.btnCheck.setVisibility(View.INVISIBLE);
                    SenderToCalendar delete = new SenderToCalendar();
                    delete.deleteEventFromCalendar(MyListAdapter.this.context,drug);
                    holder.textRemain.setText(remains+" dose remaining");
                }
                
                
            
            if(holder.btnCheck != null){
                holder.btnCheck.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View w){
                        Drug drug = getItem(position);
                        if(drug.takeDose()==true)
                        {
                            notifyDataSetChanged();

                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyListAdapter.this.context);
                            String notif = preferences.getString("notificationsType", "1");

                            int kind = Integer.valueOf(notif);
                            if (kind != 0){
                                SenderToCalendar sender = new SenderToCalendar();
                                if(kind == 1 || drug.getEventId()==0) {
                                    sender.setEventInCalendar(MyListAdapter.this.context, drug,kind);
                                }
                                else {
                                    sender.updateEventInCalendar(MyListAdapter.this.context,drug);

                                }

                            }

                        }

                    }
                });
            }

        }
        return v;
    }

    private static class Holder {
        public TextView textName;
        public TextView textDate;
        public TextView textHurry;
        public Button btnCheck;
        public TextView textRemain;
    }

}