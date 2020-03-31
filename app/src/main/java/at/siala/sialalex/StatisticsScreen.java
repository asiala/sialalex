package at.siala.sialalex;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ProgressBar;

import com.anychart.anychart.AnyChart;
import com.anychart.anychart.AnyChartView;
import com.anychart.anychart.Cartesian;
import com.anychart.anychart.CartesianSeriesColumn;
import com.anychart.anychart.DataEntry;
import com.anychart.anychart.Position;
import com.anychart.anychart.ValueDataEntry;

import java.util.ArrayList;
import java.util.List;

import at.siala.sialalex.Objects.Expense;

public class StatisticsScreen extends AppCompatActivity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_statistics_screen);

        double tankstelle = 0, party = 0, restaurants = 0, kleidung = 0, sonstiges = 0;

        ArrayList<Expense> expenses = (ArrayList<Expense>) getIntent().getSerializableExtra("Expenses");

        for(int i = 0; i < expenses.size(); i++)
        {
            switch (expenses.get(i).getCategorie())
            {
                case Tankstelle: tankstelle += expenses.get(i).getAmount(); break;
                case Party: party += expenses.get(i).getAmount(); break;
                case Sonstiges: sonstiges += expenses.get(i).getAmount(); break;
                case Kleidung: kleidung += expenses.get(i).getAmount(); break;
                case Restaurant: restaurants += expenses.get(i).getAmount(); break;
            }
        }


        AnyChartView anyChartView = findViewById(R.id.chartView);

        Cartesian cartesian = AnyChart.column();

        List<DataEntry> data = new ArrayList<>();
        data.add(new ValueDataEntry("Tankstelle", tankstelle));
        data.add(new ValueDataEntry("Party", party));
        data.add(new ValueDataEntry("Restaurants", restaurants));
        data.add(new ValueDataEntry("Kleidung", kleidung));
        data.add(new ValueDataEntry("Sonstiges", sonstiges));


        CartesianSeriesColumn column = cartesian.column(data);

        cartesian.getGetSeries(0.0).getTooltip().setFormat("{%value}{decimalsCount:2} €");

        column.getTooltip()
                .setTitleFormat("{%X}")
                .setPosition(Position.CENTER_BOTTOM)
                .setOffsetX(0d)
                .setOffsetY(5d)
                .setFontColor("#099CB3");

        //cartesian.getXAxis().getLabels().setFormat("{%Value}{decimalsCount:2}");

        anyChartView.setChart(cartesian);

    }

}
