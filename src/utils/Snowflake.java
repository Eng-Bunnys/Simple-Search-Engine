package org.bunnys.utils;

import java.time.Instant;
import java.util.concurrent.atomic.AtomicLong;

// Snowflake logic taken from twitters (X) algo
public class Snowflake {
    private static final long EPOCH = 1700000000000L;
    private static final long MACHINE_ID = 1L;
    private static final long MACHINE_BITS = 5L;
    private static final long SEQUENCE_BITS = 12L;
    private static final long MAX_SEQUENCE = (1L << SEQUENCE_BITS) - 1;

    private static final AtomicLong lastTimestamp = new AtomicLong(-1L);
    private static final AtomicLong sequence = new AtomicLong(0L);

    private final long id;

    private Snowflake(long id) {
        this.id = id;
    }

    public static Snowflake generateSnowflake() {
        long timestamp = Instant.now().toEpochMilli() - EPOCH;

        if (timestamp == lastTimestamp.get()) {
            long seq = sequence.incrementAndGet() & MAX_SEQUENCE;
            if (seq == 0) {
                while (timestamp <= lastTimestamp.get())
                    timestamp = Instant.now().toEpochMilli() - EPOCH;
            }
        } else
            sequence.set(0L);

        lastTimestamp.set(timestamp);

        long id = (timestamp << (MACHINE_BITS + SEQUENCE_BITS))
                | (MACHINE_ID << SEQUENCE_BITS)
                | sequence.get();

        return new Snowflake(id);
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.valueOf(id);
    }
}
