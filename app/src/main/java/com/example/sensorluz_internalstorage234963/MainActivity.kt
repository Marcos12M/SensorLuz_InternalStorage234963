package com.example.sensorluz_internalstorage234963

import android.content.Context
import android.content.SharedPreferences
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var txtLuzGuardada1: TextView
    private lateinit var txtLuzGuardada2: TextView
    private lateinit var txtLuzActual: TextView
    private lateinit var btnGuardar1: Button
    private lateinit var btnGuardar2: Button
    private lateinit var sensorManager: SensorManager
    private var lightSensor: Sensor? = null
    private var currentLight = 0f
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        txtLuzGuardada1 = findViewById(R.id.txtLuzGuardada1)
        txtLuzGuardada2 = findViewById(R.id.txtLuzGuardada2)
        txtLuzActual = findViewById(R.id.txtLuzActual)
        btnGuardar1 = findViewById(R.id.btnGuardar1)
        btnGuardar2 = findViewById(R.id.btnGuardar2)

        sharedPreferences = getSharedPreferences("LuzGuardada", Context.MODE_PRIVATE)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        lightSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT)

        btnGuardar1.setOnClickListener {
            guardarLuz(1)
        }

        btnGuardar2.setOnClickListener {
            guardarLuz(2)
        }
        //La neta no creo que sea el correcto pero tenia ganas de hacerlo con shared
        cargarLucesGuardadas()
    }

    override fun onResume() {
        super.onResume()
        if (lightSensor != null) {
            sensorManager.registerListener(this, lightSensor, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        if (sensorEvent.sensor.type == Sensor.TYPE_LIGHT) {
            currentLight = sensorEvent.values[0]
            txtLuzActual.text = "Luz actual: $currentLight"
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        //Tengo el consenmiento del compañero para usar su foto jeje
    }

    private fun guardarLuz(numeroGuardado: Int) {
        val editor = sharedPreferences.edit()
        if (numeroGuardado == 1) {
            editor.putFloat("luzGuardada1", currentLight)
            txtLuzGuardada1.text = "Luz guardada [1]: $currentLight"
        } else if (numeroGuardado == 2) {
            editor.putFloat("luzGuardada2", currentLight)
            txtLuzGuardada2.text = "Luz guardada [2]: $currentLight"
        }
        editor.apply()
        Toast.makeText(this, "Luz guardada en $numeroGuardado", Toast.LENGTH_SHORT).show()
    }

    private fun cargarLucesGuardadas() {
        val luzGuardada1 = sharedPreferences.getFloat("luzGuardada1", 0f)
        val luzGuardada2 = sharedPreferences.getFloat("luzGuardada2", 0f)
        txtLuzGuardada1.text = "Luz guardada [1]: $luzGuardada1"
        txtLuzGuardada2.text = "Luz guardada [2]: $luzGuardada2"
    }
}