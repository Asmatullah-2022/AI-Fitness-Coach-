package com.aifitnesscoach.app.domain.util

import kotlin.math.roundToInt

enum class BmiCategory(val label: String) {
    UNDERWEIGHT("Underweight"),
    NORMAL("Normal"),
    OVERWEIGHT("Overweight"),
    OBESE("Obese")
}

object BmiCalculator {

    fun calculate(weightKg: Float, heightCm: Float): Float {
        if (heightCm <= 0f) return 0f
        val heightM = heightCm / 100f
        return weightKg / (heightM * heightM)
    }

    fun categoryFor(bmi: Float): BmiCategory = when {
        bmi < 18.5f -> BmiCategory.UNDERWEIGHT
        bmi < 25f -> BmiCategory.NORMAL
        bmi < 30f -> BmiCategory.OVERWEIGHT
        else -> BmiCategory.OBESE
    }

    fun rounded(bmi: Float): Float = (bmi * 10).roundToInt() / 10f
}
