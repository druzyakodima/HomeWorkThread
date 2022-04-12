package Task1;

public class TestNotifyWait {

    private final Object monitorLetter = new Object();
    private final Object monitorLetterB = new Object();
    private volatile char currentLetter = 'A';

    public static void main(String[] args) {
        TestNotifyWait testNotifyWait = new TestNotifyWait();

        Thread t1 = new Thread(() -> {
            testNotifyWait.printA();
        });

        Thread t2 = new Thread(() -> {
            testNotifyWait.printB();
        });

        Thread t3 = new Thread(() -> {
            testNotifyWait.printC();
        });

        t1.start();
        t2.start();
        t3.start();

    }

    public void printA() {
        synchronized (monitorLetter) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'A') {
                        monitorLetter.wait();
                    }
                    System.out.print("A");
                    currentLetter = 'B';
                    monitorLetter.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void printB() {
        synchronized (monitorLetter) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'B') {
                        monitorLetter.wait();
                    }
                    System.out.print("B");
                    currentLetter = 'C';
                    monitorLetter.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void printC() {
        synchronized (monitorLetter) {
            try {
                for (int i = 0; i < 5; i++) {
                    while (currentLetter != 'C') {
                        monitorLetter.wait();
                    }
                    System.out.print("C");
                    currentLetter = 'A';
                    monitorLetter.notifyAll();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
