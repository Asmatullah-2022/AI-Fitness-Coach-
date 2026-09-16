package com.aifitnesscoach.app.domain.util

import org.junit.Assert.assertEquals
import org.junit.Test

class BmiCalculatorTest {

    @Test
    fun `calculate returns weight over height squared in meters`() {
        val bmi = BmiCalculator.calculate(weightKg = 70f, heightCm = 175f)
        assertEquals(22.857f, bmi, 0.01f)
    }

    @Test
    fun `calculate returns zero for non-positive height`() {
        assertEquals(0f, BmiCalculator.calculate(weightKg = 70f, heightCm = 0f), 0f)
        assertEquals(0f, BmiCalculator.calculate(weightKg = 70f, heightCm = -10f), 0f)
    }

    @Test
    fun `categoryFor classifies underweight below 18point5`() {
        assertEquals(BmiCategory.UNDERWEIGHT, BmiCalculator.categoryFor(18.4f))
    }

    @Test
    fun `categoryFor classifies normal from 18point5 up to but excluding 25`() {
        assertEquals(BmiCategory.NORMAL, BmiCalculator.categoryFor(18.5f))
        assertEquals(BmiCategory.NORMAL, BmiCalculator.categoryFor(24.9f))
    }

    @Test
    fun `categoryFor classifies overweight from 25 up to but excluding 30`() {
        assertEquals(BmiCategory.OVERWEIGHT, BmiCalculator.categoryFor(25f))
        assertEquals(BmiCategory.OVERWEIGHT, BmiCalculator.categoryFor(29.9f))
    }

    @Test
    fun `categoryFor classifies obese at 30 and above`() {
        assertEquals(BmiCategory.OBESE, BmiCalculator.categoryFor(30f))
        assertEquals(BmiCategory.OBESE, BmiCalculator.categoryFor(40f))
    }

    @Test
    fun `rounded keeps one decimal place`() {
        assertEquals(22.9f, BmiCalculator.rounded(22.857f), 0.001f)
        assertEquals(18.0f, BmiCalculator.rounded(18.0f), 0.001f)
    }
}
