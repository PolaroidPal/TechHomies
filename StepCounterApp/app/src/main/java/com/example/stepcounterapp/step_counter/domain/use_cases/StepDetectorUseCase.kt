package com.example.stepcounterapp.step_counter.domain.use_cases

import kotlin.math.sqrt

class StepDetectorUseCase {

    private var previousMagnitude = 0f
    private var lastStepTime = 0L
    private val stepThreshold = 10f // Adjust based on testing
    private val stepInterval = 300L // Minimum interval between steps

    // Low-pass filter coefficient (adjust based on testing)
    private val alpha = 0.5f
    private var smoothedMagnitude = 0f

    // Weight for the Y-axis to minimize its effect
    private val yWeight = 0.5f // Adjust this value to minimize Y-axis effect

    fun detectStep(x: Float, y: Float, z: Float, currentTime: Long): Boolean {
        // Calculate a combined magnitude with reduced Y contribution
        val adjustedMagnitude = calculateAdjustedMagnitude(x, y, z)
        smoothedMagnitude = smoothData(adjustedMagnitude)

        return if (previousMagnitude < stepThreshold && smoothedMagnitude >= stepThreshold && (currentTime - lastStepTime) > stepInterval) {
            previousMagnitude = smoothedMagnitude
            lastStepTime = currentTime
            true // Step detected
        } else {
            previousMagnitude = smoothedMagnitude
            false // No step detected
        }
    }

    // Calculate adjusted magnitude with reduced Y contribution
    private fun calculateAdjustedMagnitude(x: Float, y: Float, z: Float): Float {
        return sqrt(x * x + (y * yWeight) * (y * yWeight) + z * z)
    }

    // Simple low-pass filter for smoothing data
    private fun smoothData(magnitude: Float): Float {
        return alpha * smoothedMagnitude + (1 - alpha) * magnitude
    }
}