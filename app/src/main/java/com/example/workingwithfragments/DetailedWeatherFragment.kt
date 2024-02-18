package com.example.workingwithfragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.URL


class DetailedWeatherFragment : Fragment() {

    private lateinit var icon: ImageView
    private lateinit var descriptionWeather: TextView

    private lateinit var temp: TextView
    private lateinit var pressure: TextView
    private lateinit var humidity: TextView

    private lateinit var speed: TextView
    private lateinit var deg: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_detailed_weather, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        icon = view.findViewById(R.id.icon)
        descriptionWeather = view.findViewById(R.id.descriptionWeather1)

        temp = view.findViewById(R.id.tempData)
        pressure = view.findViewById(R.id.pressureData)
        humidity = view.findViewById(R.id.humidityData)

        speed = view.findViewById(R.id.speedData)
        deg = view.findViewById(R.id.degData)

        updateWeatheer()
    }


    private suspend fun loadWeather(): WeatherData? {
        val API_KEY = "afd3f31c472731bed0074b6a14cbf7f1"
        val weatherURL = "https://api.openweathermap.org/data/2.5/weather?q=Irkutsk&appid=$API_KEY&units=metric"
        return try {
            val stream = URL(weatherURL).openStream()

            // JSON отдаётся одной строкой,
            val data = stream.bufferedReader().use { it.readText() }

            val gson = Gson()

            gson.fromJson(data, WeatherData::class.java)

        } catch (e: IOException) {
            // Обработка ошибок при работе с сетью
            e.printStackTrace()
            null
        }


    }

    private fun updateWeatheer(){
        var weatherData: WeatherData?
        CoroutineScope(Dispatchers.IO).launch {
            weatherData = loadWeather()
            withContext(Dispatchers.Main) {
                if (weatherData != null) {
                    val iconWeatherURL = "https://openweathermap.org/img/wn/${weatherData!!.weather[0].icon}@4x.png"
                    Picasso.get().load(iconWeatherURL).into(icon)

                    descriptionWeather.text = weatherData!!.weather[0].description

                    temp.text = weatherData!!.main.temp.toString()
                    pressure.text = weatherData!!.main.pressure.toString()
                    humidity.text = weatherData!!.main.humidity.toString()

                    speed.text = weatherData!!.wind.speed.toString()
                    deg.text = weatherData!!.wind.deg.toString()

                }
            }
        }
    }


}