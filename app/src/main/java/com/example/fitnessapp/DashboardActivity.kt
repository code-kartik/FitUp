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

class DashboardActivity : AppCompatActivity(), SensorEventListener {

    var sensorManager: SensorManager? = null
    var totalSteps = 0f
    var previousTotalSteps = 0f
    private var running = false

    val stepsTaken:TextView = findViewById(R.id.stepsTaken)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        loadData()
        resetSteps()

        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager?
    }

    override fun onResume() {
        super.onResume()
        running = true
        val stepSensor = sensorManager?.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)

        if(stepSensor == null){
            Toast.makeText(this,"No Sensor found",Toast.LENGTH_SHORT).show()
        }
        else{
            sensorManager?.registerListener(this,stepSensor,SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager?.unregisterListener(this)
    }

    override fun onSensorChanged(p0: SensorEvent?) {

        if(running){
            totalSteps = p0!!.values[0]
            val currentSteps = totalSteps.toInt() - previousTotalSteps.toInt()
            stepsTaken.text = ("$currentSteps steps")
        }
    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }

    fun resetSteps(){
        stepsTaken.setOnClickListener{
            Toast.makeText(this,"Long Press to Reset", Toast.LENGTH_SHORT)
        }
        stepsTaken.setOnLongClickListener {
            previousTotalSteps = totalSteps
            stepsTaken.text = 0.toString()

            saveData()

            true
        }
    }

    private fun saveData() {
        val sharedPreferences: SharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor? = sharedPreferences.edit()
        editor?.putFloat("key1", previousTotalSteps)
        editor?.apply()
    }

    private fun loadData(){
        val sharedPreferences: SharedPreferences = getSharedPreferences("myPrefs", Context.MODE_PRIVATE)
        val savedNumber: Float = sharedPreferences.getFloat("key1",0f)
        Log.d("MainActivity","$savedNumber")
        previousTotalSteps = savedNumber
    }
}