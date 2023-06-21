package com.example.fitnessapp

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.example.fitnessapp.databinding.ActivityDashboardBinding

class DashboardActivity : AppCompatActivity(), SensorEventListener {

    private lateinit var binding: ActivityDashboardBinding
    private var sensorManager: SensorManager? = null
    private var totalSteps: Float = 0.0f
    private var previousTotalSteps: Float = 0.0f
    private var running = false
    private var stepLength: Float = 0.0f
    private var distanceTraveled: Float = 0.0f
    private var caloriesBurned: Float = 0.0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        loadData()
        resetSteps()

        supportActionBar?.hide()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?
        calculateStepLength()
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(stepSensor == null) {
            Toast.makeText(this, "No Sensor found", Toast.LENGTH_SHORT).show()
        } else {
            sensorManager?.registerListener(this, stepSensor, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(event: SensorEvent?) {
        if (running) {
            totalSteps = event?.values?.get(0) ?: 0f
            val currentSteps = (totalSteps - previousTotalSteps).toInt()
            distanceTraveled += calculateDistance(currentSteps)
            caloriesBurned += calculateCaloriesBurned(currentSteps)

            binding.stepsTaken.text = "$currentSteps steps"
            binding.distanceTravelled.text = "%.2f meters".format(distanceTraveled)
            binding.caloriesBurned.text = "%.2f calories".format(caloriesBurned)
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Empty implementation
    }

    private fun calculateDistance(steps: Int): Float {
        return steps * stepLength
    }

    private fun calculateCaloriesBurned(steps: Int): Float {
        // Calculate the calories burned based on the number of steps
        // Replace the calculation logic with your own algorithm
        val calories = steps * 0.05f // Assuming 1 step burns 0.05 calories
        binding.caloriesBurned.text = calories.toString()
        return calories
    }

    private fun calculateStepLength() {
        // Calculate the average step length based on user's height or use a default value
        // Replace the calculation logic with your own algorithm
        val userHeight = 170 // Replace with user's height in centimeters
        stepLength = userHeight * 0.415f
        val distance = (stepLength * totalSteps).toString()// Assuming average step length is 41.5% of user's height
        binding.distanceTravelled.text = distance
    }

    private fun resetSteps() {
        binding.stepsTaken.setOnClickListener {
            Toast.makeText(this, "Long Press to Reset", Toast.LENGTH_SHORT).show()
        }
        binding.stepsTaken.setOnLongClickListener {
            previousTotalSteps = totalSteps
            binding.stepsTaken.text = 0.toString()
            binding.distanceTravelled.text = "0 meters"
            binding.caloriesBurned.text = "0 calories"

            saveData()

            true
        }
    }

    private fun saveData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = sharedPreferences.edit()
        editor?.putFloat("key1", previousTotalSteps)
        editor?.apply()
    }

    private fun loadData() {
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber: Float = sharedPreferences.getFloat("key1", 0f)
        Log.d("MainActivity", "$savedNumber")
        previousTotalSteps = savedNumber
    }
}
