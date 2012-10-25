package bank;

import java.util.Random;

/**
 * A transaction that adds money to an account.
 */
public class Transfer implements Runnable {
    /** The account to subtract money from. */
    private final Account from;

    /** The amounts to add. */
    private final int[] amounts;

    /** The account to add money to. */
    private final Account to;

    /**
     * Create a new add money transaction for the given account and amounts of
     * money.
     * @param account The account to add money to.
     * @param amounts The amounts to add.
     */
    public Transfer(Account from, int[] amounts, Account to) {
        this.from = from;
        this.amounts = amounts;
        this.to = to;
    }

    @Override
    public void run() {
        // TODO find and fix the bug
        for (int amount : amounts) {
            synchronized (from) {
                synchronized (to) {
                    from.addMoney(-amount);
                    to.addMoney(amount);
                }
            }
        }
    }

    /**
     * Generates 2 random int[] and feeds it to a thread that uses the AddMoney
     * runnable.
     */
    public static void main(String[] args) {
        Random rand = new Random();
        rand.setSeed(0);
        for (int run = 0; run < 1000 * 1000; run++) {
            // Generate 2 arrays of 50 amounts.
            final int NUM = 50;
            int difference = 0;
            int[] amounts1 = new int[50];
            int[] amounts2 = new int[50];
            for (int i = 0; i < NUM; i++) {
                amounts1[i] = rand.nextInt(500);
                amounts2[i] = rand.nextInt(500);
                difference -= amounts1[i];
                difference += amounts2[i];
            }

            // Make some accounts.
            Account a = new Account(0);
            Account b = new Account(1);

            // Run some transfer threads on the accounts.
            Thread[] threads = new Thread[] {
                new Thread(new Transfer(a, amounts1, b), "Thread-1"),
                new Thread(new Transfer(b, amounts2, a), "Thread-2"),
            };

            for (Thread thread : threads) {
                thread.start();
            }

            // Wait for them to finish.
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // See if we got it right.
            if (a.getBalance() != -b.getBalance() ||
                    a.getBalance() != difference ||
                    b.getBalance() != -difference) {
                System.out.printf("Balances were %d & %d, supposed to be %d & %d\n",
                        a.getBalance(), b.getBalance(),
                        difference, -difference);
            }
        }
    }
}
