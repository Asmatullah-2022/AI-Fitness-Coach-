package com.aifitnesscoach.app.domain.util

import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDate

class StreakCalculatorTest {

    private val today = LocalDate.of(2026, 9, 16)

    @Test
    fun `no completed dates yields zero streaks`() {
        val result = StreakCalculator.compute(emptyList(), today)
        assertEquals(0, result.currentStreak)
        assertEquals(0, result.longestStreak)
    }

    @Test
    fun `consecutive days ending today count as current streak`() {
        val dates = listOf(today.minusDays(2), today.minusDays(1), today)
        val result = StreakCalculator.compute(dates, today)
        assertEquals(3, result.currentStreak)
        assertEquals(3, result.longestStreak)
    }

    @Test
    fun `streak survives when today has no session yet but yesterday does`() {
        val dates = listOf(today.minusDays(2), today.minusDays(1))
        val result = StreakCalculator.compute(dates, today)
        assertEquals(2, result.currentStreak)
    }

    @Test
    fun `streak resets to zero when both today and yesterday are missing`() {
        val dates = listOf(today.minusDays(5), today.minusDays(4))
        val result = StreakCalculator.compute(dates, today)
        assertEquals(0, result.currentStreak)
        assertEquals(2, result.longestStreak)
    }

    @Test
    fun `longest streak can exceed current streak`() {
        val dates = listOf(
            today.minusDays(10), today.minusDays(9), today.minusDays(8), today.minusDays(7), // longest run of 4
            today.minusDays(1), today // current run of 2
        )
        val result = StreakCalculator.compute(dates, today)
        assertEquals(2, result.currentStreak)
        assertEquals(4, result.longestStreak)
    }

    @Test
    fun `duplicate and unsorted dates are treated as a single distinct day each`() {
        val dates = listOf(today, today.minusDays(1), today, today.minusDays(1))
        val result = StreakCalculator.compute(dates, today)
        assertEquals(2, result.currentStreak)
        assertEquals(2, result.longestStreak)
    }
}
