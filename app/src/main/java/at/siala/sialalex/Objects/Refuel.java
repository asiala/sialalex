package at.siala.sialalex.Objects;

import java.util.Date;

public class Refuel
{
    String date;
    String location;
    Double invoice_amount;

    public Refuel(String date, String location, Double invoice_amount)
    {
        this.date = date;
        this.location = location;
        this.invoice_amount = invoice_amount;
    }

    public String getDate()
    {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Double getInvoice_amount() {
        return invoice_amount;
    }

    public void setInvoice_amount(Double invoice_amount) {
        this.invoice_amount = invoice_amount;
    }
}
