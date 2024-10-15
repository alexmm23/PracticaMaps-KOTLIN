package com.example.localizacion

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.PolylineOptions
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL
import com.google.maps.android.PolyUtil


class RequestMapsActivity : AppCompatActivity() {
    private lateinit var btnPeticion: Button
    private lateinit var edtOrigen: EditText
    private lateinit var edtDestino: EditText
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_maps)
        btnPeticion = findViewById(R.id.btnTrazarRuta)
        edtOrigen = findViewById(R.id.edtOrigen)
        edtDestino = findViewById(R.id.edtDestino)

        btnPeticion.setOnClickListener {
            val origen = edtOrigen.text.toString()
            val destino = edtDestino.text.toString()
            val API_KEY ="AIzaSyAiyCoVwKbUbb4aosASpW25Ir0Cqf5Y0Ds"

            if(origen.isNotEmpty() && destino.isNotEmpty()){
                getRoute(origen, destino, API_KEY){response ->
                    Log.d("Response", response)
                    val jsonResponse = JSONObject(response)
                    val routes = jsonResponse.getJSONArray("routes")
                    val firstRoute = routes.getJSONObject(0)
                    val overViewPolyline = firstRoute.getJSONObject("overview_polyline")
                    val points = overViewPolyline.getString("points")
                    println("Ruta $points")
                    val intent = Intent(this, MapsActivity::class.java)
                    intent.putExtra("points", points)
                    startActivity(intent)
                }
            }
        }

    }
    fun getRoute(origin: String, destination: String, apiKey:String, callback: (String) -> Unit){
        val urlStr = "https://maps.googleapis.com/maps/api/directions/json?origin=$origin&destination=$destination&key=$apiKey"
        Log.d("REQUEST", urlStr)
        CoroutineScope(Dispatchers.IO).launch {
            try{
                val url = URL(urlStr)
                val connection = url.openConnection() as HttpURLConnection
                connection.requestMethod = "GET"
                connection.connect()

                val inputStream = connection.inputStream
                val response = inputStream.bufferedReader().use{it.readText()}
                withContext(Dispatchers.Main){
                    callback(response)
                }

            }catch (e: Exception){
                e.printStackTrace()
                withContext(Dispatchers.Main){
                    callback("Error: ${e.message}")
                }
            }
        }
    }
}