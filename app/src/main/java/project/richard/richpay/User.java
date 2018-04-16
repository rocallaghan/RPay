package project.richard.richpay;

/**
 * Created by richard on 11/04/2018.
 */

public class User {
    public String email;
    public String userID;
    public double balance;
    public Transaction transaction;




    //required default constructor
    public User() {
    }

    public User(String userID, String email, double balance, Transaction transaction) {
        this.email = email;
        this.userID = userID;
        this.balance = balance;
        this.transaction = transaction;


    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserID(){
        return userID;
    }

    public void setUserID(String userID){
        this.userID = userID;
    }

    public double getBalance(){
        return balance;
    }

    public void setUserBalance(double balance) {this.balance = balance;}

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public Transaction getTransaction(){
        return transaction;
    }

}
