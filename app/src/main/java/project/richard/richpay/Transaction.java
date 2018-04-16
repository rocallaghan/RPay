package project.richard.richpay;

import java.util.Date;

public class Transaction {
    public String transInfo;
    public String transId;
    public double amount;
    public String transDate;



    public Transaction(String transId, double amount, String transInfo, String transDate) {


        this.transId = transId;
        this.transInfo = transInfo;
        this.amount = amount;
        this.transDate = transDate;


    }


    public String gettransId() {
        return transId;
    }

    public void settansId(String transId) {
        this.transId = transId;
    }

    public String getTransInfo(){
        return transInfo;
    }

    public void setTransInfo(String transInfo){
        this.transInfo = transInfo;
    }

    public double getAmount(){
        return amount;
    }

    public void setAmount(double amount) {this.amount = amount;}


    public String getTransDate() {
        return transDate;
    }

    public void setTransId(String transDate) {
        this.transDate = transDate;
    }


}