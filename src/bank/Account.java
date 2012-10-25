package bank;

/**
 * An account keeps track of a balance (amount of money in the account), and an ID.
 */
public class Account {
    /** Balance of this account. */
    private int balance;

    /** ID of this account. */
    private int id;

    /**
     * Create a new account with $0 balance.
     * @param id Account ID.
     */
    public Account(int id) {
    	this(0, id);
    }

    /**
     * Create a new account.
     * @param balance Starting balance.
     * @param id Account ID.
     */
    public Account(int balance, int id) {
        this.balance = balance;
        this.id = id;
    }

    /**
     * Returns the balance in the account.
     * @return The balance in the account.
     */
    public int getBalance() {
        return balance;
    }

    /**
     * Returns the account's ID.
     * @Return The ID.
     */
    public int getId() {
        return id;
    }

    /**
     * Adds a (possibly negative) amount of money to the balance.
     * @param money Amount to add to the balance.
     */
    public void addMoney(int money) {
        balance += money;
    }
}
