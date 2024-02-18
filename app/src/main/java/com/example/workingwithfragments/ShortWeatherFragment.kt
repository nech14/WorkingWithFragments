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


class ShortWeatherFragment : Fragment() {

    private lateinit var imageView: ImageView
    private lateinit var descriptionWeather: TextView
    private lateinit var temp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_short_weather, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        imageView = view.findViewById(R.id.imageView)
        descriptionWeather = view.findViewById(R.id.descriptionWeather)
        temp = view.findViewById(R.id.temp)

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
                    Picasso.get().load(iconWeatherURL).into(imageView)
                    descriptionWeather.text = weatherData?.weather?.getOrNull(0)?.description ?: "miss"
                    temp.text = weatherData?.main?.temp?.toString() ?: "miss"
                }
            }
        }
    }

}