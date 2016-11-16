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

public class MyListAdapter extends ArrayAdapter<Drug> {

    private int layout_resource;
    Context context;
    private TextView tt2;
    private Drug drug;
    private Button chbt;
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
    public View getView(int position, View convertView, ViewGroup parent) {

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
            v.setTag(holder);

        }else{
            holder = (Holder) convertView.getTag();
        }

        drug = this.getItem(position); // the same as ArrayList<drugs> drugs.get(index)

        if (drug != null) {

                holder.textName.setText(drug.getName());
                holder.textDate.setText(DateConverter.dateToString( drug.getDateOfNextDose() ));
            
            if(holder.btnCheck != null){
                holder.btnCheck.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View w){
                       // Log.i("PMP", position+"position");
                        // ja pierdole 
                        if(drug.takeDose()==false)
                        {
                            Toast.makeText(getContext(), "Doses finished.",Toast.LENGTH_LONG).show();
                            Log.i("PMP", drug.getRemainingDoses()+"w");
                            notifyDataSetChanged();
                            //holder.textDate.setText("Finished");
                           // holder.btnCheck.setVisibility(View.INVISIBLE);

                        }else{
                            Log.i("PMP", drug.getRemainingDoses()+"poza ifem");
                             //holder.textDate.setText(DateConverter.dateToString( drug.getDateOfNextDose() ));
                            notifyDataSetChanged();
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
        public Button btnCheck;
    }

}