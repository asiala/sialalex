package at.siala.sialalex.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import at.siala.sialalex.Objects.Refuel;
import at.siala.sialalex.R;

public class RefuelAdapter extends ArrayAdapter<Refuel> implements View.OnClickListener
{
    private ArrayList<Refuel> refuelData;
    Context refuelContext;

    public RefuelAdapter(ArrayList<Refuel> data, Context context)
    {
        super(context, R.layout.listitem_refuel ,data);
        this.refuelData = data;
        this.refuelContext = context;
    }

    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Refuel refuel = (Refuel) object;

        switch (v.getId())
        {
            case R.id.invoice:
                //Auswählen welches Item angeklickt wurde und anschließend was passiert
                break;
        }
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View refuelItem = convertView;
        if(refuelItem == null)
        {
            refuelItem = LayoutInflater.from(refuelContext).inflate(R.layout.listitem_refuel, parent, false);
        }

        Refuel currentRefuel = refuelData.get(position);

        TextView invoice = (TextView) refuelItem.findViewById(R.id.invoice);
        invoice.setText(currentRefuel.getInvoice_amount()+"€");

        TextView date = (TextView) refuelItem.findViewById(R.id.date);
        date.setText(currentRefuel.getDate());


        TextView location = (TextView) refuelItem.findViewById(R.id.location);
        location.setText(currentRefuel.getLocation());

        return refuelItem;


    }
}
