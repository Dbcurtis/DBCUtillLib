/*
 * Copyright (c) 2015, Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package net.dbcrd.DBCUtilLib;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * <strong>Class to help track how much elapsed time passed from a start to end, or start to laps.</strong>
 *
 * @author Daniel B. Curtis {@code <dbcurtis@dbcrd.net>}
 */
public class StopWatch
{

    /** THE LOGGER */
    private static final Logger THE_LOGGER = Logger.getLogger(StopWatch.class.getName());

    /**
     *
     * @param startLDT a LocalDateTime of the start
     * @param endLDT   a LocalDateTime of the end
     *
     * @return a long with milliseconds between start and end
     *
     * @throws IllegalArgumentException if either argument is null or end is before start.
     */
    public static long getTotalMiliSeconds(LocalDateTime startLDT, LocalDateTime endLDT)
    {
        if (startLDT == null || endLDT == null || startLDT.isAfter(endLDT))
        {
            throw new IllegalArgumentException("null Argument or end is before start");
        }
        final long el = endLDT.toInstant(ZoneOffset.UTC).toEpochMilli();
        final long sl = startLDT.toInstant(ZoneOffset.UTC).toEpochMilli();
        return el - sl;

    }

    /**
     *
     * @param startLDTin a LocalDateTime of the start
     * @param endLDTin   a LocalDateTime of the end
     *
     * @return a long with seconds between start and end
     *
     * @throws IllegalArgumentException if either argument is null or end is before start.
     */
    public static long getTotalSeconds(final LocalDateTime startLDTin, final LocalDateTime endLDTin)
    {
        final LocalDateTime startLDT = Objects.requireNonNull(startLDTin);
        final LocalDateTime endLDT = Objects.requireNonNull(endLDTin);
        if (startLDT == null || endLDT == null || startLDT.isAfter(endLDT))
        {
            throw new IllegalArgumentException("null Argument or end is before start");
        }
        Instant ei =endLDT.toInstant(ZoneOffset.UTC);
        Instant si =startLDT.toInstant(ZoneOffset.UTC);
        Instant di = ei.minusMillis(si.toEpochMilli());
        return di .getEpochSecond();

//
//
//        final long el = endLDT.toInstant(ZoneOffset.UTC).getEpochSecond();
//        final long sl = startLDT.toInstant(ZoneOffset.UTC).getEpochSecond();
//        return el - sl;

    }
    /** a list of LocalDateTeim that show laps */
    private final List<LocalDateTime> lapTimes = new ArrayList<>(100);
    /** an Optional that is present if the stop watch has ended */
    private Optional<LocalDateTime> oEndTime = Optional.empty();
    /** the start time of the timer */
    private final LocalDateTime startTime;

    /**
     * Start the timer
     */
    public StopWatch()
    {
        super();
        startTime = LocalDateTime.now();
    }

    /**
     * End the timer. You can end the timer multiple times
     */
    public void end()
    {
        synchronized (this)
        {
           // final LocalDateTime n = LocalDateTime.now();
            oEndTime = Optional.of(LocalDateTime.now());
        }
    }

    /**
     *
     * @return a Stream of LocalDateTime that shows the lap times and ends with the end time if the timer has ended
     */
    public Stream<LocalDateTime> getLapTimes()
    {
        List<LocalDateTime> laps;
        List<LocalDateTime> result;
        synchronized (this)
        {
            laps   = new ArrayList<>(lapTimes.size() + 2);
            result = new ArrayList<>(lapTimes.size() + 2);
            laps.add(startTime);
            laps.addAll(lapTimes);
            oEndTime.map(et->{
                laps.add(et);
                return true;
            });

        }
        if (laps.size() < 2)
        {
            return new ArrayList<LocalDateTime>(1).stream();
        }

        final Iterator<LocalDateTime> ldit = laps.iterator();
        LocalDateTime priorldt = ldit.next();

        while (ldit.hasNext())
        {
            final LocalDateTime lapLDT = ldit.next();
            //final LocalDateTime jjd = LocalDateTime.MIN.withYear(2000).plusSeconds(getTotalSeconds(priorldt, lapLDT));
            result.add(LocalDateTime.MIN.withYear(2000).plusSeconds(getTotalSeconds(priorldt, lapLDT)));
            priorldt = lapLDT;
        }

        return result.stream();
    }

    /**
     *
     * @return return a long that is the milliseconds from the start to end
     */
    public long getTotalMilliSeconds()
    {
        return oEndTime.map(et->getTotalMiliSeconds(startTime, oEndTime.get()))
                .orElse(-1L);
    }

    /**
     *
     * @return a long that is the seconds from the start to end
     */
    public long getTotalSeconds()
    {
       return oEndTime.map(et->getTotalSeconds(startTime, et))
               .orElse(-1L);
    }

    /**
     *
     * @return a LocalTime that represents the time between the start and the end time
     */
    public Optional<LocalDateTime> getTotalTime()
    {
        return oEndTime
                .map(ignore -> Optional.of(LocalDateTime.MIN.withYear(2000).plusSeconds(getTotalSeconds())))
                .orElse( Optional.empty());
    }

    /**
     * mark the current time as a lap
     */
    public void lap()
    {
        synchronized (this)
        {
            oEndTime.map(ignore-> true)
                    .orElseGet(()->{
                        lapTimes.add(LocalDateTime.now());
                        return true;
                    });
        }
    }

    /** {@inheritDoc } */
    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder(256);
        synchronized (this)
        {
            oEndTime.map(ignore->{
                sb.append("complete at  ").append(getTotalSeconds()).append(" sec. Laps: ").append(lapTimes.size());
                return true;
            }).orElseGet(()->{
                sb.append("running, Laps:   ").append(lapTimes.size());
                return true;
            });

        }
        return sb.toString();
    }

}
