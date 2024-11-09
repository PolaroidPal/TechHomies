package com.example.stepcounterapp.step_counter.data

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.telephony.SmsManager
import android.util.Log
import com.example.stepcounterapp.core.domain.models.Sessions
import com.example.stepcounterapp.step_counter.domain.StepCounterRepository
import com.example.stepcounterapp.step_counter.domain.use_cases.TrackStepsUseCase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject
import kotlin.math.sqrt

class StepCounterRepositoryImpl @Inject constructor(
    firebaseAuth: FirebaseAuth,
    private val sensorManager: SensorManager,
    private val fireStore: FirebaseFirestore,
    private val stepsUseCase: TrackStepsUseCase
) : StepCounterRepository {

    private var sensorEventListener: SensorEventListener? = null
    private var accelerometerEventListener: SensorEventListener? = null

    private val userId = firebaseAuth.currentUser?.uid

    private val _fallDetected = MutableStateFlow(false)
    override val fallDetected: StateFlow<Boolean> = _fallDetected

    private var messageSent = false

    override suspend fun saveUserSession(userSession: Sessions, id: String) {
        userId?.let { userId ->
            Log.i("id", "saveUserSession: total steps id is $id")
            fireStore.collection("users").document(userId)
                .collection("user_steps").document(id)
                .collection("sessions")
                .add(userSession)
        }
    }

    override suspend fun startTrackingSteps(
        onStepDetected: (Int) -> Unit
    ) {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        sensorEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                Log.i("sensor", "startTrackingSteps: accelerometer")
                event?.let {
                    val x = event.values[0]
                    val y = event.values[1]
                    val z = event.values[2]
                    val currentTime = System.currentTimeMillis()

                    // Pass accelerometer data to the use case and notify of a new step
                    stepsUseCase.trackStep(x, y, z, currentTime)
                    onStepDetected(stepsUseCase.getStepCount())
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        sensorManager.registerListener(
            sensorEventListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    override fun stopSensor() {
        sensorEventListener?.let {
            sensorManager.unregisterListener(it)
        }
        accelerometerEventListener?.let {
            sensorManager.unregisterListener(it)
        }
    }

    override suspend fun startProximitySensor(urgentContact: String) {
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        val fallThreshold = 4.0

        accelerometerEventListener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                val x = event!!.values[0]
                val y = event.values[1]
                val z = event.values[2]
                val gForce = sqrt((x * x + y * y + z * z).toDouble()) / SensorManager.GRAVITY_EARTH

                // Check if the g-force exceeds the fall threshold
//                if (gForce > FALL_THRESHOLD_MINOR) {
//                    _fallDetected.value = true
//                    Log.i("minor", "onSensorChanged: minor")
//                }

                Log.i("sensor", "startProximitySensor: Inside proximity $gForce")

                if (gForce > fallThreshold && !messageSent) {
                    _fallDetected.value = true
                    sendEmergencyNotification(urgentContact)
                    _fallDetected.value = false
                }
            }
            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

//        val proximityEventListener = object : SensorEventListener {
//            override fun onSensorChanged(event: SensorEvent?) {
//                // Check if the proximity sensor detects something near the phone
//                _proximityDetected.value = event!!.values[0] < (proximitySensor!!.maximumRange)
//
//                if (_fallDetected.value) {
//                    Log.i("Fall detected", "onSensorChanged: ${_fallDetected.value}")
//                    if (!_proximityDetected.value) {
//                        Log.i(
//                            "ProximitySensor",
//                            "Phone is far from body after fall. with proximity ${event.values[0]} ${proximitySensor.maximumRange}"
//                        )
//                        sendEmergencyNotification(urgentContact)
//                    } else {
//                        Log.i(
//                            "ProximitySensor",
//                            "Phone is still near the body after fall. with proximity ${event.values[0]} ${proximitySensor.maximumRange}"
//                        )
//                    }
//                    _fallDetected.value = false
//                    Log.i("fall detected", "onSensorChanged: changed boolean")
//                }
//            }
//
//            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
//        }

        // Register the sensors
//        sensorManager.registerListener(
//            proximityEventListener,
//            proximitySensor,
//            SensorManager.SENSOR_DELAY_NORMAL
//        )

        sensorManager.registerListener(
            accelerometerEventListener,
            accelerometer,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    private fun sendEmergencyNotification(urgentContact: String) {
        try {
            val smsManager = SmsManager.getDefault()
            smsManager.sendTextMessage(urgentContact, null, "Fall detected", null, null)
            messageSent = true
            Log.i("SMS", "Message sent successfully.")
        } catch (e: Exception) {
            Log.e("SMS", "Failed to send message: ${e.message}")
        }
    }
}