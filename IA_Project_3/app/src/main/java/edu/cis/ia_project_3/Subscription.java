package edu.cis.ia_project_3;

public class Subscription extends Payments
{
    boolean isFree;
    int paymentPeriod;

    public Subscription(String id, String name, String amount, boolean isFree, int paymentPeriod)
    {
        super(id, name, amount);
        this.isFree = isFree;
        this.paymentPeriod = paymentPeriod;
    }
}
