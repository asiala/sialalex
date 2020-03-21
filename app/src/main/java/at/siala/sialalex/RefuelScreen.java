package at.siala.sialalex;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

import at.siala.sialalex.Adapters.RefuelAdapter;
import at.siala.sialalex.Objects.Refuel;

public class RefuelScreen extends AppCompatActivity
{
    private ListView refuelListView;
    private TextView invoiceSumTextView;
    private FloatingActionButton addRefuelButton;

    ArrayList<Refuel> refuelList;
    private RefuelAdapter refuelAdapter;
    private double invoiceSum;

    SharedPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refuel_screen);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Prepare Views
        refuelListView = (ListView) findViewById(R.id.refuelList);
        invoiceSumTextView = (TextView) findViewById(R.id.invoiceSum);

        //Load Preferences
        myPreferences = getPreferences(MODE_PRIVATE);

        fetchRefuelData();

        setOnClickRefuelListView();

        //region Show RefuelAddDialog
        addRefuelButton = findViewById(R.id.addRefuel);
        addRefuelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showRefuelAddDialog();
            }
        });
        //endregion

        calculateInvoiceSum();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showRefuelAddDialog()
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

                                saveRefuelData();
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

        invoiceSumTextView.setText("Gesamtbetrag " + String.format("%.2f", invoiceSum) + "€");
    }


    private void saveRefuelData()
    {
        SharedPreferences.Editor preferenceEditor = myPreferences.edit();

        Gson gson = new Gson();
        String refuelJson = gson.toJson(refuelList);

        preferenceEditor.putString("RefuelList", refuelJson);
        preferenceEditor.commit();

    }

    private void fetchRefuelData()
    {
        Gson gson = new Gson();
        String refuelJson = myPreferences.getString("RefuelList", "");

        TypeToken<ArrayList<Refuel>> refuelToken = new TypeToken<ArrayList<Refuel>>() {};
        if(refuelJson != "")
        {
            refuelList = gson.fromJson(refuelJson, refuelToken.getType());
        }
        else
        {
            refuelList = new ArrayList<Refuel>();
        }

        refuelAdapter = new RefuelAdapter(refuelList, this);
        refuelListView.setAdapter(refuelAdapter);
        refuelAdapter.notifyDataSetChanged();
        calculateInvoiceSum();
    }

    private void setOnClickRefuelListView()
    {
        refuelListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(RefuelScreen.this, R.style.DialogStyle);


                // Prepare Dialog
                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle("Löschen")
                        .setMessage("Wollen Sie den Tankvorgang löschen?")
                        .setPositiveButton("Löschen",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        refuelList.remove(position);
                                        refuelAdapter.notifyDataSetChanged();
                                        calculateInvoiceSum();
                                        saveRefuelData();

                                    }
                                })
                        .setNegativeButton("Abbrechen",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();


                return true;
            }
        });
    }


    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        saveRefuelData();
    }

    @Override
    protected void onStop() {
        super.onStop();

        saveRefuelData();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveRefuelData();
    }
}
