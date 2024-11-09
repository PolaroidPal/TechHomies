package com.example.stepcounterapp.step_counter.domain.use_cases

class TrackStepsUseCase(private val stepDetector: StepDetectorUseCase) {

    private var stepCount = 0

    fun trackStep(x: Float, y: Float, z: Float, currentTime: Long) {
        if (stepDetector.detectStep(x, y, z, currentTime)) {
            stepCount++
        }
    }

    fun getStepCount(): Int {
        return stepCount
    }
}