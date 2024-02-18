package com.example.workingwithfragments

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.net.URL
import java.util.*


class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        val select_button = findViewById<Button>(R.id.select_button)

        select_button.setOnClickListener{
            val myDialog = MyDialogFragment()
            myDialog.show(supportFragmentManager, "Погода")
        }

    }


    suspend fun loadWeather() {
        val API_KEY = "afd3f31c472731bed0074b6a14cbf7f1"
        val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=Irkutsk&appid=$API_KEY&units=metric"
        try {
            val stream = URL(weatherURL).openStream()

            // JSON отдаётся одной строкой,
            val data = stream.bufferedReader().use { it.readText() }

            val gson = Gson()
            val weatherData = gson.fromJson(data, WeatherData::class.java)

            // TODO: предусмотреть обработку ошибок (нет сети, пустой ответ)
            Log.d("GGGGGGGGGGGGGGGGGGGGGGGGG", data)
            Log.d("HHHHHHHHHHHHHHHHHHHHHHH", weatherData.toString())

        } catch (e: IOException) {
            // Обработка ошибок при работе с сетью
            e.printStackTrace()
        }


    }



    public fun onClick(v: View) {

        // Используем IO-диспетчер вместо Main (основного потока)
        CoroutineScope(Dispatchers.IO).launch {
            loadWeather()
        }
    }
}