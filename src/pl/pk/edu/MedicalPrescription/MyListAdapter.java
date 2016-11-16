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
            v = vi.inflate(R.layout.drugrowlistview, parent,false);
            holder = new Holder();
            holder.textName = (TextView) v.findViewById(R.id.list_item_name);
            holder.textDate = (TextView) v.findViewById(R.id.list_item_date);
            holder.btnCheck = (Button)   v.findViewById(R.id.check_btn);
            //holder.btnDelete = (Button)   v.findViewById(R.id.delete_btn);
            v.setTag(holder);


        }else{
            holder = (Holder) convertView.getTag();
        }
        //drug = list.get(position);
        drug = getItem(position); // the same as ArrayList<drugs> drugs.get(index)

        if (drug != null) {

                holder.textName.setText(drug.getName());
                if(drug.getRemainingDoses()>0){
                holder.textDate.setText(DateConverter.dateToString( drug.getDateOfNextDose() ));

                }else{
                    holder.textDate.setText("Finished");
                    holder.btnCheck.setVisibility(View.INVISIBLE);
                }

                
            
            if(holder.btnCheck != null){
                holder.btnCheck.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View w){
                        Drug drugs = getItem(position);
                        if(drugs.takeDose()==false)
                        {
                            Log.i("PMP", drugs.getRemainingDoses()+"w");
                            notifyDataSetChanged();

                        }else{
                            
                            Log.i("PMP", drugs.getRemainingDoses()+"poza ifem");
                            notifyDataSetChanged();
                            
                            /*Date beginTime = drugs.getDateOfNextDose();
                            int remain = drugs.getRemainingDoses();
                            String description = null;
                            if(remain>1){
                                description = remain + " remaining doses to finish drug";
                            }else{
                                description = remain + " remaining dose to finish drug";
                            }
                            /*Intent intent = new Intent(Intent.ACTION_INSERT)
                                    .setData(Events.CONTENT_URI)
                                    .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.getTime())
                                    .putExtra(CalendarContract.EXTRA_EVENT_END_TIME, beginTime.getTime())
                                    .putExtra(Events.TITLE, drugs.getName())
                                    .putExtra(Events.DESCRIPTION, description)
                                    .putExtra(Events.AVAILABILITY, Events.AVAILABILITY_FREE);
                            context.startActivity(intent);*/

                        }
                        

                    }
                });
            }

           /* if(holder.btnDelete != null){
                holder.btnDelete.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View w)
                    {

                    }

                });
            }*/

        }

        return v;
    }

    private static class Holder {

        public TextView textName;
        public TextView textDate;
        public Button btnCheck;
        //public Button btnDelete;
    }

}