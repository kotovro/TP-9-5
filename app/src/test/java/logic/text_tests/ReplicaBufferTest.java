package logic.text_tests;

import logic.general.Replica;
import logic.general.Speaker;
import logic.text_edit.ReplicaBuffer;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static ui.EditController.getImage;

@Execution(ExecutionMode.CONCURRENT)
class ReplicaBufferTest {
    private Replica testReplica;
    private Speaker testSpeaker;

    @BeforeEach
    void setUp() {
        testSpeaker = new Speaker("TestSpeaker", getImage("/images/default_users/undefined.png"), 0);
        testReplica = new Replica("Test message", testSpeaker);
        ReplicaBuffer.clear();
    }

    @AfterEach
    void tearDown() {
        ReplicaBuffer.clear();
    }

    @Test
    void isEmpty_shouldReturnTrueWhenEmpty() {
        assertTrue(ReplicaBuffer.isEmpty());
    }

    @Test
    void setAndGetReplica_shouldWorkCorrectly() {
        ReplicaBuffer.setReplica(testReplica);
        assertSame(testReplica, ReplicaBuffer.getReplica());
    }

    @RepeatedTest(5)
    void concurrentAccess_shouldNotCorruptState() throws InterruptedException {
        final int THREAD_COUNT = 10;
        final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        final CountDownLatch latch = new CountDownLatch(THREAD_COUNT);
        final AtomicInteger successCount = new AtomicInteger();

        for (int i = 0; i < THREAD_COUNT; i++) {
            final Replica replica = new Replica("Thread-" + i, testSpeaker);
            executor.execute(() -> {
                try {
                    latch.countDown();
                    latch.await();

                    ReplicaBuffer.setReplica(replica);
                    if (replica.equals(ReplicaBuffer.getReplica())) {
                        successCount.incrementAndGet();
                    }
                    ReplicaBuffer.clear();
                } catch (Exception e) {
                    fail("Concurrent access failed: " + e.getMessage());
                }
            });
        }

        executor.shutdown();

        long startTime = System.currentTimeMillis();
        while (!executor.isTerminated()) {
            if (System.currentTimeMillis() - startTime > 3000) {
                executor.shutdownNow();
                fail("Test timeout exceeded 3 seconds");
            }
            Thread.sleep(100);
        }

        assertEquals(THREAD_COUNT, successCount.get());
    }

    @Test
    void highLoadPerformance_shouldCompleteQuickly() {
        final int ITERATIONS = 10000;
        long startNanos = System.nanoTime();

        for (int i = 0; i < ITERATIONS; i++) {
            ReplicaBuffer.setReplica(testReplica);
            ReplicaBuffer.getReplica();
            ReplicaBuffer.clear();
        }

        long durationNanos = System.nanoTime() - startNanos;
        long durationMillis = durationNanos / 1_000_000;
        System.out.printf("Completed %d iterations in %d ms%n", ITERATIONS, durationMillis);
        assertTrue(durationMillis < 500, "Performance degraded: " + durationMillis + " ms");
    }

    @Test
    void memoryLeakTest() {
        WeakReference<Replica> replicaRef = new WeakReference<>(new Replica("Leak test", testSpeaker));

        ReplicaBuffer.setReplica(replicaRef.get());
        assertNotNull(replicaRef.get(), "Replica should exist before GC");

        ReplicaBuffer.clear();
        System.gc();

        assertNull(replicaRef.get(), "Replica should be collected by GC");
    }

    @Test
    void bufferStressTest_withHighVolumeData() throws InterruptedException {
        final int MESSAGE_COUNT = 99;   //максимум до 500 реплик на все потоки (для 1 потока был тест до 100000000)
        final int THREAD_COUNT = 5;
        final ExecutorService executor = Executors.newFixedThreadPool(THREAD_COUNT);
        final AtomicInteger successCount = new AtomicInteger();
        final AtomicInteger failureCount = new AtomicInteger();
        final List<AssertionError> errors = Collections.synchronizedList(new ArrayList<>());

        for (int i = 0; i < MESSAGE_COUNT; i++) {
            final Replica replica = new Replica("Msg-" + i, testSpeaker);
            executor.execute(() -> {
                try {
                    ReplicaBuffer.setReplica(replica);
                    Replica retrieved = ReplicaBuffer.getReplica();
                    if (!replica.equals(retrieved)) {
                        failureCount.incrementAndGet();
                        errors.add(new AssertionError(
                                String.format("Ожидалось: %s, получено: %s", replica, retrieved)
                        ));
                    } else {
                        successCount.incrementAndGet();
                    }
                    ReplicaBuffer.clear();
                } catch (AssertionError e) {
                    failureCount.incrementAndGet();
                    errors.add(e);
                }
            });
        }

        executor.shutdown();
        long start = System.currentTimeMillis();
        while (!executor.isTerminated()) {
            if (System.currentTimeMillis() - start > 10_000) { // 10 секунд таймаут
                executor.shutdownNow();
                break;
            }
            Thread.sleep(100);
        }

        if (!errors.isEmpty()) {
            System.err.println("\n=== ОШИБКИ ===");
            errors.forEach(e -> {
                System.err.println(e.getClass().getSimpleName() + ": " + e.getMessage());
                if (e.getCause() != null) {
                    System.err.println("Причина: " + e.getCause().getMessage());
                }
            });
        }

        assertAll(
                () -> assertEquals(0, failureCount.get(),
                        String.format("Обнаружено %d ошибок. Первые 3: %s",
                                failureCount.get(),
                                errors.stream().limit(3).map(e -> e.getMessage()).collect(Collectors.toList())
                        )),
                () -> assertTrue(ReplicaBuffer.isEmpty(), "Буфер не пуст после теста"),
                () -> assertNull(ReplicaBuffer.getReplica(), "Финальное состояние не null")
        );
    }

    @Test
    void nullSafety_shouldHandleNullValues() {
        assertAll(
                () -> assertDoesNotThrow(() -> ReplicaBuffer.setReplica(null)),
                () -> assertNull(ReplicaBuffer.getReplica()),
                () -> assertDoesNotThrow(ReplicaBuffer::clear)
        );
    }
}