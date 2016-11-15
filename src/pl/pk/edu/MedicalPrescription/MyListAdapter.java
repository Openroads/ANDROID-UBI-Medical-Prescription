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

public class MyListAdapter extends ArrayAdapter<Drug> {

    private int layout_resource;

    private TextView tt2;
    private Drug drug;
    private Button chbt;
    public MyListAdapter(Context context, int textViewResourceId) {
        super(context, textViewResourceId);
    }

    public MyListAdapter(Context context, int resource, ArrayList<Drug> drugs) {
        super(context, resource, drugs);
        this.layout_resource=resource;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View v = convertView;
        Holder holder = null;


        if (v == null) {

            LayoutInflater vi;
            vi = LayoutInflater.from(this.getContext());
            v = vi.inflate(layout_resource, null);
            holder.textName = (TextView) v.findViewById(R.id.list_item_name);
            holder.textDate = (TextView) v.findViewById(R.id.list_item_date);
            holder.btnCheck = (Button)   v.findViewById(R.id.check_btn);
            v.setTag(holder);

        }else{
            holder = (Holder) v.getTag();
        }

        drug = this.getItem(position); // the same as ArrayList<drugs> drugs.get(index)

        if (drug != null) {

                holder.textName.setText(drug.getName());
                holder.textDate.setText(DateConverter.dateToString( drug.getDateOfNextDose() ));
            
            if(holder.btnCheck != null){
                holder.btnCheck.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        Log.i("PMP", position+"position");
                        if(drug.takeDose()==false)
                        {
                            Log.i("PMP", drug.getRemainingDoses()+"w");
                            holder.textDate.setText("Finished");
                            holder.btnCheck.setVisibility(View.INVISIBLE);

                        }else{
                            Log.i("PMP", drug.getRemainingDoses()+"poza ifem");
                            holder.textDate.setText(DateConverter.dateToString( drug.getDateOfNextDose() ));
                        }
                        

                    }
                });
            }

        }

        return v;
    }
    static class Holder {

            TextView textName;
            TextView textDate;
            Button btnCheck;
    }

}