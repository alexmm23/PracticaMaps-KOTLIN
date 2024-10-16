package com.example.localizacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    private lateinit var boton: Button
    private lateinit var botonGoogleApiTrazado: Button
    private lateinit var botonTrazadoNormal: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        botonGoogleApiTrazado = findViewById(R.id.btnGoogleApi)
        botonGoogleApiTrazado.setOnClickListener {
            val intent = Intent(this, RequestMapsActivity::class.java)
            intent.putExtra("tipo", "google")
            startActivity(intent)
        }
        botonTrazadoNormal = findViewById(R.id.btnTrazado)
        botonTrazadoNormal.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)
            intent.putExtra("tipo", "normal")
            startActivity(intent)
        }
        boton = findViewById(R.id.btnMaps)
        boton.setOnClickListener() { onClick() }
    }


    fun onClick() {
        val intent: Intent?
        intent = Intent(this, MapsActivity::class.java)
        startActivity(intent)
    }
}
