/**
 * The SleepingBarber class provides the main entry point for this
 * example.  It is based upon the famous Sleeping Barber problem and
 * this code is based upon Hartley's example provided on his website.
 *
 * This example should fail because there is a deadlock! -tcw
 *
 * Other properties to test are:
 * <ul>
 * <li>Only one Customer is getting a hair cut at a time.</li>
 * <li>When there are Customers waiting, the Barber should not be asleep.</li>
 * <li>When there are no Customers waiting, the Barber should be asleep.</li>
 * <li>Customers are seen in the order they arrive.</li>
 * </ul>
 *
 * @author Todd Wallentine tcw AT cis ksu edu
 * @version $Revision: 1.1 $ - $Date: 2003/12/12 17:33:59 $
 */
public class SleepingBarber {
    private static final int customerCount = 3;
    private static Customer[] customers;

    public static void main(String[] args) {
        /* create all the players in this example:
         * 1) Create a single BarberShop
         * 2) Create a single Barber in that BarberShop
         * 3) Create several Customers that will use that BarberShop
         */
        BarberShop barberShop = new BarberShop();
        Barber barber = new Barber(barberShop);

        customers = new Customer[customerCount];

        for (int i = 0; i < customerCount; i++) {
            customers[i] = new Customer(barberShop);
        }

        /* start all the players in this example. */
        barber.start();

        for (int i = 0; i < customerCount; i++) {
            customers[i].start();
        }
    }
}


class Barber extends Thread {
    private BarberShop barberShop;

    public Barber(BarberShop barberShop) {
        this.barberShop = barberShop;
    }

    public void run() {
        while (true) {
            barberShop.requestCustomer();
        }
    }
}

class Customer extends Thread {
    private BarberShop barberShop;

    public Customer(BarberShop barberShop) {
        this.barberShop = barberShop;
    }

    public void run() {
        while (true) {
            // wait some time for my hair to grow
            barberShop.getHairCut();
        }
    }
}

class BarberShop {
    private Object mutexLock;
    private Object barberReadyLock;
    private Object barberWorkingLock;
    private Object customerAvailableLock;
    private int chairs;
    private int waiting;
    private int cutting;

    public BarberShop() {
        chairs = 2;
        waiting = 0;
        cutting = 0;
        mutexLock = new Object();
        barberWorkingLock = new Object();
        barberReadyLock = new Object();
        customerAvailableLock = new Object();
    }

    public void requestCustomer() {
        synchronized (customerAvailableLock) {
            try {
                // wait until a customer is available
                customerAvailableLock.wait();
            } catch (InterruptedException ie) {
            }
        }

        synchronized (mutexLock) {
            // take the next customer from the "waiting" queue
            waiting--;
        }

        synchronized (barberReadyLock) {
            barberReadyLock.notify();
        }

        synchronized (barberWorkingLock) {
            try {
                // now wait until the barber is done cutting
                barberWorkingLock.wait();
            } catch (InterruptedException ie) {
            }
        }
    }

    public void getHairCut() {
        synchronized (mutexLock) {
            if (waiting < chairs) {
                // now wait until the barber is ready
                waiting++;

                synchronized (customerAvailableLock) {
                    customerAvailableLock.notify();
                }

                synchronized (barberReadyLock) {
                    // waiting until the barber is ready to cut my hair
                    try {
                        barberReadyLock.wait();
                    } catch (InterruptedException ie) {
                    }
                }

                cutting++;

                // cut hair for some random period of time
                cutting--;

                synchronized (barberWorkingLock) {
                    // tell the barber he is done cutting
                    barberWorkingLock.notify();
                }
            }
        }
    }
}
