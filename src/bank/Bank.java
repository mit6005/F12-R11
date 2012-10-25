package bank;

import java.util.LinkedList;
import java.util.List;

/**
 * A bank holds many accounts.
 */
public class Bank {
    /** Accounts in this bank. */
    private List<Account> accounts;

    /**
     * Creates a new banks with the given number of empty accounts.
     * @param numAccounts The number of empty accounts.
     */
    public Bank(final int numAccounts) {
        accounts = new LinkedList<Account>();

        // Init NUM_INIT_THREADS threads
        final int NUM_INIT_THREADS = 1;
        Thread[] threads = new Thread[NUM_INIT_THREADS];
        for (int i = 0; i < threads.length; i++) {
            threads[i] = new Thread(String.format("AddAccount-%d", i)) {
                @Override
                public void run() {
                    // Continue adding until we have enough accounts.
                    while (accounts.size() < numAccounts) {
	                	final int id = accounts.size();
	                	Account account = new Account(id);
                        accounts.add(account);
                    }
                }
            };
	        threads[i].start();
        }

        // Wait for all threads to stop.
        for (Thread thread : threads) {
	        try {
	            thread.join();
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
        }

        System.out.printf("Successfully made %d accounts.\n", accounts.size());
    }

    /**
     * Verifies all accounts are not null and have the correct ID.
     */
    public void verify() {
        for (int i = 0; i < accounts.size(); i++) {
            Account account = accounts.get(i);
            if (account == null) {
                System.out.println("Account " + i + " is null!");
            } else if (account.getId() != i) {
                System.out.println("Account " + i +
                        " has ID: " + account.getId());
            }
        }
    }

    /**
     * Updates the bank account with the specified amount.
     * @param fromId The ID of the account.
     * @param amount The amount.
     */
    public void update(int fromId, int amount, int toId) {
        // Subtract from the "from" account.
    	accounts.get(fromId).addMoney(-amount);

    	// Add to the "to" account.
    	accounts.get(toId).addMoney(amount);
    }

    public static void main(String[] args) {
    	// Number of accounts.
    	final int NUM_ACCOUNTS = 1000 * 1000;

    	// Make a bank with NUM_ACCOUNTS accounts.
    	Bank bank = new Bank(NUM_ACCOUNTS);

    	// Verify everything went well.
    	bank.verify();
    }
}
