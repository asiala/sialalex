package at.siala.sialalex;

import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.getbase.floatingactionbutton.FloatingActionButton;

import java.io.Console;
import java.util.ArrayList;
import java.util.Calendar;

import at.siala.sialalex.Adapters.RefuelAdapter;
import at.siala.sialalex.Objects.Refuel;

public class StartScreen extends AppCompatActivity
{
    private ListView refuelListView;
    private TextView invoiceSumTextView;
    private FloatingActionButton addRefuelButton;

    ArrayList<Refuel> refuelList;
    private RefuelAdapter refuelAdapter;
    private double invoiceSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_screen);

        //region Add Refuel
        addRefuelButton = findViewById(R.id.addRefuel);
        addRefuelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),"Add Clicked",Toast.LENGTH_SHORT).show();

                showRefuelDialog();
            }
        });
        //endregion


        refuelListView = (ListView) findViewById(R.id.refuelList);
        refuelList = new ArrayList<>();
        refuelAdapter = new RefuelAdapter(refuelList, this);
        refuelListView.setAdapter(refuelAdapter);

        invoiceSumTextView = (TextView) findViewById(R.id.invoiceSum);
        calculateInvoiceSum();
    }

    private void showRefuelDialog()
    {
        LayoutInflater li = LayoutInflater.from(this);
        final View dialogView = li.inflate(R.layout.dialog_add_refuel, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(dialogView);

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Hinzufügen",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Take data
                                Double invoice = Double.parseDouble(String.valueOf(((EditText)dialogView.findViewById(R.id.invoiceEditText)).getText()));
                                String location = String.valueOf(((EditText)dialogView.findViewById(R.id.locationEditText)).getText());
                                String date = String.valueOf(((EditText)dialogView.findViewById(R.id.dateEditText)).getText());


                                refuelList.add(new Refuel(date, location, invoice));
                                refuelAdapter.notifyDataSetChanged();
                                calculateInvoiceSum();
                            }
                        })
                .setNegativeButton("Abbrechen",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();
    }

    private void calculateInvoiceSum()
    {
        invoiceSum = 0;
        refuelList.forEach((n) -> invoiceSum += n.getInvoice_amount());

        invoiceSumTextView.setText("Gesamtbetrag " + invoiceSum + "€");
    }


}
