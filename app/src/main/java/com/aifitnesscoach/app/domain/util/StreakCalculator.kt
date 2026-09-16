package com.aifitnesscoach.app.domain.util

import java.time.LocalDate

data class StreakInfo(
    val currentStreak: Int,
    val longestStreak: Int
)

object StreakCalculator {

    /**
     * [completedDates] need not be sorted or de-duplicated.
     * A streak counts consecutive calendar days that contain at least one
     * completed workout session, anchored at "today" (or "yesterday" if
     * today has no session yet, so the streak isn't lost before the user
     * has a chance to work out).
     */
    fun compute(completedDates: List<LocalDate>, today: LocalDate = LocalDate.now()): StreakInfo {
        val distinctDays = completedDates.toSortedSet()
        if (distinctDays.isEmpty()) return StreakInfo(0, 0)

        var longest = 0
        var running = 0
        var previous: LocalDate? = null
        for (day in distinctDays) {
            running = if (previous != null && previous.plusDays(1) == day) running + 1 else 1
            longest = maxOf(longest, running)
            previous = day
        }

        var anchor = today
        if (!distinctDays.contains(anchor)) {
            anchor = today.minusDays(1)
        }
        if (!distinctDays.contains(anchor)) {
            return StreakInfo(0, longest)
        }

        var current = 1
        var cursor = anchor
        while (distinctDays.contains(cursor.minusDays(1))) {
            cursor = cursor.minusDays(1)
            current++
        }

        return StreakInfo(current, longest)
    }
}
