package at.siala.sialalex;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.dropbox.core.v2.DbxClientV2;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.Buffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import at.siala.sialalex.Adapters.ExpenseAdapter;
import at.siala.sialalex.Objects.Categorie;
import at.siala.sialalex.Objects.Expense;

public class ExpenseScreen extends AppCompatActivity
{
    private ListView expensesListView;
    private TextView amountSumTextView;
    private FloatingActionButton addExpenseButton;

    ArrayList<Expense> expenseList;
    private ExpenseAdapter expenseAdapter;
    private double amountSum;



    SharedPreferences myPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_screen);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        //Prepare Views
        expensesListView = (ListView) findViewById(R.id.expenseList);
        amountSumTextView = (TextView) findViewById(R.id.amountSum);

        //Load Preferences
        myPreferences = getPreferences(MODE_PRIVATE);

        fetchExpenseData();

        setOnClickExpenseListView();

        //region Show RefuelAddDialog
        addExpenseButton = findViewById(R.id.addExpense);
        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showExpenseAddDialog();
            }
        });
        //endregion

        calculateAmountSum();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showExpenseAddDialog()
    {
        LayoutInflater li = LayoutInflater.from(this);
        final View dialogView = li.inflate(R.layout.dialoag_add_expense, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(dialogView);

        //Set current Date
        String currentDate = new SimpleDateFormat("dd.MM.yyyy", Locale.getDefault()).format(new Date());
        ((EditText)dialogView.findViewById(R.id.dateEditText)).setText(currentDate);

        //Add Categories to Spinner
        Spinner categorieSpinner = (Spinner) dialogView.findViewById(R.id.categorieSpinner);
        categorieSpinner.setAdapter(new ArrayAdapter<Categorie>(this, R.layout.spinneritem_categorie , Categorie.values()));

        // set dialog message
        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("Hinzufügen",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Take data
                                Double amount = Double.parseDouble(String.valueOf(((EditText)dialogView.findViewById(R.id.amountEditText)).getText()));
                                String description = String.valueOf(((EditText)dialogView.findViewById(R.id.descriptionEditText)).getText());
                                String date = String.valueOf(((EditText)dialogView.findViewById(R.id.dateEditText)).getText());
                                Categorie categorie = Categorie.valueOf(((Spinner)dialogView.findViewById(R.id.categorieSpinner)).getSelectedItem().toString());

                                expenseList.add(new Expense(amount, description, date, categorie));
                                expenseAdapter.notifyDataSetChanged();
                                calculateAmountSum();

                                saveExpenseData();
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

    private void calculateAmountSum()
    {
        amountSum = 0;
        expenseList.forEach((n) -> amountSum += n.getAmount());

        amountSumTextView.setText("Gesamtbetrag " + String.format("%.2f", amountSum) + "€");
    }


    private void saveExpenseData()
    {
        //SharedPreferences.Editor preferenceEditor = myPreferences.edit();

        Gson gson = new Gson();
        String expenseJson = gson.toJson(expenseList);

        FileOutputStream fos = null;

        try
        {
            fos = openFileOutput("expenseData.txt", MODE_PRIVATE);
            fos.write(expenseJson.getBytes());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            if(fos != null)
            {
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


        //preferenceEditor.putString("ExpenseList", expenseJson);
        //preferenceEditor.commit();

    }

    private void fetchExpenseData()
    {
        Gson gson = new Gson();
        String expenseJson = "";

        //Fetch Data

        FileInputStream fis = null;

        try
        {
            fis = openFileInput("expenseData.txt");
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader br = new BufferedReader(isr);
            StringBuilder sb = new StringBuilder();
            String line;

            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            expenseJson = sb.toString();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fis != null)
            {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }



        TypeToken<ArrayList<Expense>> expenseToken = new TypeToken<ArrayList<Expense>>() {};
        if(expenseJson != "")
        {
            expenseList = gson.fromJson(expenseJson, expenseToken.getType());
        }
        else
        {
            expenseList = new ArrayList<Expense>();
        }

        expenseAdapter = new ExpenseAdapter(expenseList, this);
        expensesListView.setAdapter(expenseAdapter);
        expenseAdapter.notifyDataSetChanged();
        calculateAmountSum();
    }

    private void setOnClickExpenseListView()
    {
        expensesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id)
            {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ExpenseScreen.this, R.style.DialogStyle);


                // Prepare Dialog
                alertDialogBuilder
                        .setCancelable(false)
                        .setTitle("Löschen")
                        .setMessage("Wollen Sie diese Ausgabe löschen?")
                        .setPositiveButton("Löschen",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,int id) {
                                        expenseList.remove(position);
                                        expenseAdapter.notifyDataSetChanged();
                                        calculateAmountSum();
                                        saveExpenseData();

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

    public void startStatisticsScreen(View view)
    {
        Intent statisticsScreenIntent = new Intent(this, StatisticsScreen.class);
        statisticsScreenIntent.putExtra("Expenses", expenseList);
        this.startActivity(statisticsScreenIntent);
    }




    @Override
    protected void onDestroy()
    {
        super.onDestroy();

        saveExpenseData();
    }

    @Override
    protected void onStop() {
        super.onStop();

        saveExpenseData();
    }

    @Override
    protected void onPause() {
        super.onPause();

        saveExpenseData();
    }
}
