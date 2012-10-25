package bank;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

public class ATM implements Runnable {
    /** Endpoint to connect socket to. */
    private final SocketAddress endpoint;

    /** number of accounts in the bank. */
    private final int numAccounts;

    /**
     * Create a new ATM that knows a bit about a bank.
     * @param numAccounts The number of accounts the bank has.
     * @param port The port to connect on.
     */
    public ATM(int numAccounts, int port) {
        this.numAccounts = numAccounts;
        endpoint = new InetSocketAddress("localhost", port);
    }

    @Override
    public void run() {
        Socket socket = new Socket();
        PrintWriter out = null;
        try {
            socket.connect(endpoint);
            out = new PrintWriter(socket.getOutputStream(), true);
            sendMessages(out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            out.close();
        }
    }

    /**
     * Send messages to the bank.  Must be of form:
     * <ID> <$>
     * @param out The place to print messages.
     */
    private void sendMessages(PrintWriter out) {
        // TODO write some messages.
    }

    public static void main(String[] args) {
        // Arguments.  Change them for different behavior!
        final int PORT = 1235;
        final int NUM_ACCOUNTS = 1000 * 1000;
        final int NUM_THREADS = 8;
        final int TO_SPAWN = 10 * 1000;

        // Server needs to be cleaned up, so declare here for access in finally.
        BankServer server = null;
        Thread serverThread = null;

        try {
            Bank bank = new Bank(NUM_ACCOUNTS);
            server = new BankServer(bank, PORT);
            serverThread = new Thread(server, "Server");
            serverThread.start();

            // Create NUM_THREADS threads and keep using them to spawn several
            // transactions.
            Thread[] clients = new Thread[NUM_THREADS];
            int spawned = 0;
            while (spawned < TO_SPAWN) {
                for (int i = 0; i < clients.length; i++) {
                    if (clients[i] == null || !clients[i].isAlive()) {
                        if (clients[i] != null) {
                            clients[i].join();
                        }
                        clients[i] = new Thread(
                                new ATM(NUM_ACCOUNTS, PORT), "Client-" + i);
                        clients[i].start();
                        spawned++;
                    }
                }
            }

            // Stop everything.
            for (Thread client : clients) {
                client.join();
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            System.out.println("Stopping server.");
            server.stop();
            try {
                serverThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
