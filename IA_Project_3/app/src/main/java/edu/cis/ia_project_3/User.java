package edu.cis.ia_project_3;

import java.util.ArrayList;

public class User
{
    String uid;
    String email;
    ArrayList<Payments> payments;

    public User(String uid, String email, ArrayList<Payments> payments)
    {
        this.uid = uid;
        this.email = email;
        this.payments = payments;
    }

    public User(String uid, String email)
    {
        this.uid = uid;
        this.email = email;
    }

    public User()
    {
    }

    public String getUid()
    {
        return uid;
    }

    public void setUid(String uid)
    {
        this.uid = uid;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public ArrayList<Payments> getPayments()
    {
        return payments;
    }

    public void setPayments(ArrayList<Payments> payments)
    {
        this.payments = payments;
    }
}
