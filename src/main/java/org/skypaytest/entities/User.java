package org.skypaytest.entities;

public class User {
    private int userId;
    private int balance;

    public User(int userId, int balance) {
        if (balance < 0){
            throw new IllegalArgumentException("Le solde ne peut pas être négatif.");
        }
        this.userId = userId;
        this.balance = balance;
    }

    public int getUserId() {return userId;}
    public int getBalance() {return balance;}

    public void deductBalance(int amount) throws IllegalArgumentException {
        if (amount > balance){
            throw new IllegalArgumentException("Solde insuffisant");
        }

        balance -= amount;
    }

    @Override
    public String toString() {
        return String.format("User #%d - Balance: %d)", userId, balance);
    }

}
