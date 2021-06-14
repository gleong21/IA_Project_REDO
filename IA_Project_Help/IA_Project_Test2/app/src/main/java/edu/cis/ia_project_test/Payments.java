package edu.cis.ia_project_test;

public class Payments
{
    String id;
    String name;
    String amount;
    String year;
    String date;
    String month;
    boolean isPaid;
    Double longitude;
    Double latitude;
    int days;
    int timesPaid;
    int average;

    public Payments(String id, String name, String amount, String year, String date, String month, boolean isPaid,
                    Double longitude, Double latitude, int days, int times, int avg)
    {
        this.id = id;
        this.name = name;
        this.amount = amount;
        this.year = year;
        this.date = date;
        this.month = month;
        this.isPaid = isPaid;
        this.longitude = longitude;
        this.latitude = latitude;
        this.days = days;
        this.timesPaid = times;
        this.average = avg;
    }

    public Payments()
    {
    }


    public Payments(String id, String name, String amount)
    {
        this.id = id;
        this.name = name;
        this.amount = amount;
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

    public boolean isPaid()
    {
        return isPaid;
    }

    public void setPaid(boolean paid)
    {
        isPaid = paid;
    }


    public Double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(Double longitude)
    {
        this.longitude = longitude;
    }

    public Double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(Double latitude)
    {
        this.latitude = latitude;
    }

    public int getDays()
    {
        return days;
    }

    public void setDays(int days)
    {
        this.days = days;
    }

    public int getTimesPaid()
    {
        return timesPaid;
    }

    public void setTimesPaid(int timesPaid)
    {
        this.timesPaid = timesPaid;
    }

    public int getAverage()
    {
        return average;
    }

    public void setAverage(int average)
    {
        this.average = average;
    }
}
