package at.siala.sialalex.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import at.siala.sialalex.Objects.Expense;
import at.siala.sialalex.R;

public class ExpenseAdapter extends ArrayAdapter<Expense> //implements View.OnClickListener
{
    private ArrayList<Expense> expenseData;
    Context context;

    public ExpenseAdapter(ArrayList<Expense> data, Context context)
    {
        super(context, R.layout.listitem_expense,data);
        this.expenseData = data;
        this.context = context;
    }

    /*@Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        Expense refuel = (Expense) object;

        switch (v.getId())
        {
            case R.id.invoice:
                //Auswählen welches Item angeklickt wurde und anschließend was passiert
                break;
        }
    }*/


    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        View expenseItem = convertView;
        if(expenseItem == null)
        {
            expenseItem = LayoutInflater.from(context).inflate(R.layout.listitem_expense, parent, false);
        }

        Expense currentExpense = expenseData.get(position);

        TextView amount = (TextView) expenseItem.findViewById(R.id.invoice);
        amount.setText(String.format("%.2f", currentExpense.getAmount())+"€");

        TextView date = (TextView) expenseItem.findViewById(R.id.date);
        date.setText(currentExpense.getDate());

        TextView description = (TextView) expenseItem.findViewById(R.id.description);
        description.setText(currentExpense.getDescription());

        TextView categorie = (TextView) expenseItem.findViewById(R.id.categorie);
        categorie.setText(currentExpense.getCategorie().toString());

        return expenseItem;


    }
}
