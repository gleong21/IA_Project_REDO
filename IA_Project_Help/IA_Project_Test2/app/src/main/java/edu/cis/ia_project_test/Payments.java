package edu.cis.ia_project_test;

public class Payments
{
    String id;
    String name;
    String amount;
    String year;
    String date;
    String month;

    public Payments(String id, String name, String amount)
    {
        this.id = id;
        this.name = name;
        this.amount = amount;
    }

    public Payments()
    {
    }

    public Payments(String id, String name, String amount, String year, String month, String date)
    {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.year = year;
        this.date = date;
        this.month = month;
    }


    public String getId()
    {
        return id;
    }

    public void setId(String id)
    {
        this.id = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAmount()
    {
        return amount;
    }

    public void setAmount(String amount)
    {
        this.amount = amount;
    }

    public String getYear()
    {
        return year;
    }

    public void setYear(String year)
    {
        this.year = year;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }

    public String getMonth()
    {
        return month;
    }

    public void setMonth(String month)
    {
        this.month = month;
    }
}
