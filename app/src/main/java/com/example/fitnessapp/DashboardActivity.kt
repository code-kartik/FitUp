package com.example.fitnessapp

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast

class DashboardActivity : AppCompatActivity(), SensorEventListener {

    var sensorManager: SensorManager? = null
    var totalSteps = 0f
    var previousTotalSteps = 0f
    private var running = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

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

    override fun onSensorChanged(p0: SensorEvent?) {

    }

    override fun onAccuracyChanged(p0: Sensor?, p1: Int) {
        TODO("Not yet implemented")
    }
}