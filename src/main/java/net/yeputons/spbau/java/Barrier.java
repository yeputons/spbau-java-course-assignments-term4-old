package net.yeputons.spbau.java;

import java.util.concurrent.atomic.AtomicInteger;

public class Barrier {
    private final AtomicInteger partiesLeft;

    public Barrier(int parties) {
        partiesLeft = new AtomicInteger(parties);
    }

    public void await() throws InterruptedException {
        int partiesAfterMe = partiesLeft.decrementAndGet();
        if (partiesAfterMe < 0) {
            return;
        }
        synchronized (partiesLeft) {
            if (partiesAfterMe == 0) {
                partiesLeft.notifyAll();
            } else {
                while (partiesLeft.get() > 0) {
                    //partiesLeft.wait();
                }
            }
        }
    }
}
