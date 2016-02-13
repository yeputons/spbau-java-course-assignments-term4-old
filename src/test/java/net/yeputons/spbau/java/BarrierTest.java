package net.yeputons.spbau.java;

import org.junit.Test;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.*;

public class BarrierTest {
    @Test(timeout = 100)
    public void testZeroSingleThread() throws Exception {
        final Barrier b = new Barrier(0);
        b.await();
        b.await();
    }

    @Test(timeout = 100)
    public void testOneSingleThread() throws Exception {
        final Barrier b = new Barrier(1);
        b.await();
        b.await();
    }

    public Integer[] testSeveral(int parties, int threads) throws InterruptedException {
        assertTrue(parties <= threads);

        final Barrier b = new Barrier(parties);
        final List<Integer> events = new LinkedList<Integer>();
        TestThread[] ths = new TestThread[threads];
        for (int i = 0; i < threads; i++) {
            final int id = i + 1;
            ths[i] = new TestThread() {
                @Override
                public void run() throws Exception {
                    synchronized (events) {
                        events.add(id);
                    }
                    b.await();
                    synchronized (events) {
                        events.add(-id);
                    }
                }
            };
        }
        for (TestThread t : ths) {
            t.join();
        }
        assertEquals(2 * threads, events.size());
        for (int id = 1; id <= threads; id++) {
            int start = events.indexOf(id);
            int end = events.indexOf(-id);
            assertNotEquals(-1, start);
            assertNotEquals(-1, end);
            assertTrue(start < end);
        }
        for (int i = 0; i < parties; i++) {
            assertTrue(events.get(i) > 0);
        }
        return events.toArray(new Integer[events.size()]);
    }

    @Test
    public void testTwo() throws Exception {
        testSeveral(2, 2);
    }

    @Test
    public void testTwoAndPost() throws Exception {
        testSeveral(2, 10);
    }

    @Test
    public void testBig() throws Exception {
        testSeveral(100, 200);
    }
}
