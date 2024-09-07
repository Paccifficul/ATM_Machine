package org.atmachine.bank;

public class BankAccountModel {
    private final long id;
    private final String accountHolderName;
    private final String accountHolderSurname;
    private double accountAmount;
    private final long accountNumber;
    private final short accountPasscode;
    private final String paymentSystem;

    public BankAccountModel(long id, String accountHolderName, String accountHolderSurname, double accountAmount,
                            long accountNumber, short accountPasscode, String paymentSystem) {
        this.id = id;
        this.accountHolderName = accountHolderName;
        this.accountHolderSurname = accountHolderSurname;
        this.accountAmount = accountAmount;
        this.accountNumber = accountNumber;
        this.accountPasscode = accountPasscode;
        this.paymentSystem = paymentSystem;
    }

    public long getId() {
        return id;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public String getAccountHolderSurname() {
        return accountHolderSurname;
    }

    public double getAccountAmount() {
        return accountAmount;
    }

    public void setAccountAmount(double accountAmount) {
        this.accountAmount = accountAmount;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public short getAccountPasscode() {
        return accountPasscode;
    }

    @Override
    public String toString() {
        return "Bank account holder: " + this.accountHolderName + " " + this.accountHolderSurname +
                "\nBank account number: " + this.accountNumber +
                "\nBalance: " + this.accountAmount;
    }

    public String getPaymentSystem() {
        return paymentSystem;
    }

}
