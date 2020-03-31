package at.siala.sialalex.Objects;

import java.io.Serializable;

public class Expense implements Serializable
{
    String date;
    String description;
    Double amount;
    Categorie categorie;

    public Expense(Double amount, String description, String date, Categorie categorie)
    {
        this.date = date;
        this.description = description;
        this.amount = amount;
        this.categorie = categorie;
    }

    public String getDate()
    {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Categorie getCategorie() {
        return categorie;
    }

    public void setCategorie(Categorie categorie) {
        this.categorie = categorie;
    }
}
