package com.many.calendarperevernu

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.jjoe64.graphview.GraphView
import com.jjoe64.graphview.series.DataPoint
import com.jjoe64.graphview.series.LineGraphSeries
import com.jjoe64.graphview.series.Series
import com.many.calendarperevernu.databinding.ActivityMainBinding
import com.many.calendarperevernu.db.Model
import com.many.calendarperevernu.db.UsersDBHelper
import com.many.calendarperevernu.mycalendar.MyCalendur
import okhttp3.*
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException
import java.time.LocalDate
import java.time.YearMonth
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt


private const val NUM_PAGES = 5

class MainActivity : AppCompatActivity(), LocationListener {
    private lateinit var viewPager: ViewPager2
    private lateinit var binding: ActivityMainBinding
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2
    lateinit var state: String;
    private var ll: Pair<Double, Double> = Pair(0.0, 0.0)
    var client: OkHttpClient = OkHttpClient()
//    lateinit var usersDBHelper : UsersDBHelper

    companion object {
        private var eventsCardIsReady = false
        private var requestText = ""
        private const val oldEventsCardText = ""
        lateinit var calendar: MyCalendur
        lateinit var startDate: LocalDate
        lateinit var endDate: LocalDate
        var firstDate = true
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun setDate(date: LocalDate) {
        if (firstDate) {
            startDate = date
            endDate = date
            firstDate = false
        } else {
            endDate = date
            Log.d("MY", "Echo " + startDate.toEpochDay() + " " + endDate.toEpochDay())

            if (startDate.toEpochDay().toLong() > endDate.toEpochDay().toLong()) {
                endDate = startDate
                startDate = date
            }
            firstDate = true
        }
        var formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
        findViewById<TextView>(R.id.startDate).text = formatter.format(startDate)
        findViewById<TextView>(R.id.endDate).text = formatter.format(endDate)
    }

    @SuppressLint("ResourceAsColor", "RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        usersDBHelper = UsersDBHelper(this)
        val permissions = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        ActivityCompat.requestPermissions(this, permissions, 0)

        calendar = MyCalendur()
        startDate = LocalDate.now()
        endDate = LocalDate.now()
        binding = ActivityMainBinding.inflate(layoutInflater);
        viewPager = findViewById(R.id.pager)
//        viewPager.setPageTransformer(ZoomOutPageTransformer())

        val pagerAdapter = ScreenSlidePagerAdapter(this)
        viewPager.adapter = pagerAdapter
        var ym = MyCalendur.difDate(YearMonth.now())
//        val graph = findViewById<View>(R.id.graph) as GraphView
//        val series: LineGraphSeries<DataPoint> = LineGraphSeries(
//            arrayOf(
//                DataPoint(0.0, 1.0),
//                DataPoint(1.0, 5.0),
//                DataPoint(2.0, 3.0),
//                DataPoint(3.0, 2.0),
//                DataPoint(4.0, 6.0)
//            )
//        )
//        graph.addSeries(series)
        viewPager.currentItem = ym.year * 12 + ym.monthValue
        getLocation()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun runSelectedTemp(localDate: LocalDate, list: Array<Int>) {
        var a = LineGraphSeries<DataPoint>()
        var all = false
        runOnUiThread {
            for (l in list) {
                var date = LocalDate.of(localDate.year, localDate.month, l)
                var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
                var stringDate = formatter.format(date)
                val request: Request = Request.Builder()
                    .url("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/${ll.second},${ll.first}/$stringDate?key=ZDN8H3KC4Y4Q2TFRMUXKGCPBQ")
                    .build()
                val call = client.newCall(request)
                call.enqueue(object : Callback {
                    @Throws(IOException::class)
                    override fun onResponse(call: Call, response: Response) {
                        try {
                            val testV = JSONObject(response.body!!.string())
                            var temp =
                                ((((testV["days"] as JSONArray)[0] as JSONObject)["temp"].toString()
                                    .toFloat() - 32.0) / 1.8).toFloat().roundToInt()
                            Log.d("MY", l.toString())
                            if (l == 1) {
                                a.appendData(DataPoint(l.toDouble(), 25.0), false, 3)
                            } else
                                if (l == list[list.size - 1]) {
                                    a.appendData(
                                        DataPoint(l.toDouble(), 30.0),
                                        false,
                                        3
                                    )
                                }
                        } catch (e: IOException) {
                            e.printStackTrace()
                        } catch (e: JSONException) {
                            e.printStackTrace()
                        }
                        all = true
                    }

                    override fun onFailure(call: Call, e: IOException) {}
                })
            }
        }
        val handler = Handler()
        val delay = 10 // 1000 milliseconds == 1 second
        handler.postDelayed(object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                if (all) {
                    var graph = findViewById<GraphView>(R.id.graph)
                    graph.addSeries(a);
                }
            }
        }, delay.toLong())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun runLastEvents(localDate: LocalDate) {
        var ii = LocalDate.of(localDate.year, localDate.month, 1)
        var formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        var stringDate = formatter.format(localDate)
        var startTime = ii.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli()
        runOnUiThread {
            Log.d("MYHTTP", "In $startTime")
            Log.d("MY", localDate.toEpochDay().toString() + " " + LocalDate.now().toEpochDay())
            val request: Request = Request.Builder()
                .url("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/${ll.second},${ll.first}/$stringDate?key=ZDN8H3KC4Y4Q2TFRMUXKGCPBQ")
                .build()
            val call = client.newCall(request)
            call.enqueue(object : Callback {
                @Throws(IOException::class)
                override fun onResponse(call: Call, response: Response) {
                    try {
                        UpdateEvents(response.body!!.string())
                    } catch (e: IOException) {
                        e.printStackTrace()
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call, e: IOException) {}
            })
        }
        val handler = Handler()
        val delay = 10 // 1000 milliseconds == 1 second
        handler.postDelayed(object : Runnable {
            @SuppressLint("SetTextI18n")
            override fun run() {
                if (eventsCardIsReady) {
                    eventsCardIsReady = false
                    try {
                        val testV = JSONObject(requestText)
                        var city = testV["timezone"].toString()
                        var temp =
                            ((((testV["days"] as JSONArray)[0] as JSONObject)["temp"].toString()
                                .toFloat() - 32.0) / 1.8).toFloat().roundToInt().toString()
                        var main =
                            ((testV["days"] as JSONArray)[0] as JSONObject)["conditions"].toString()
                        var wind =
                            ((testV["days"] as JSONArray)[0] as JSONObject)["windspeed"].toString()
                        findViewById<TextView>(R.id.cityName).text = city
                        findViewById<TextView>(R.id.tempText).text =
                            temp + getString(R.string.gradus)
                        findViewById<TextView>(R.id.mainText).text = main
                        findViewById<TextView>(R.id.windText).text = "Wind " + wind + "m/s"
                    } catch (e: JSONException) {
                        e.printStackTrace()
                    }
                    Thread.currentThread().interrupt()
                }
                handler.postDelayed(this, delay.toLong())
            }
        }, delay.toLong())
    }

    @Throws(JSONException::class)
    fun UpdateEvents(jsonText: String) {
        Log.d("MY", jsonText)
        if (jsonText == oldEventsCardText) {
            return
        }
        requestText = jsonText
        eventsCardIsReady = true
    }

    private inner class ScreenSlidePagerAdapter(fa: FragmentActivity) :
        FragmentStateAdapter(fa) {
        override fun getItemCount(): Int = Int.MAX_VALUE

        @RequiresApi(Build.VERSION_CODES.O)
        override fun createFragment(position: Int): Fragment {
            val pair = MyCalendur.yearMonth(position);

            return FragmentCalendar(pair.first, pair.second)
        }
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                locationPermissionCode
            )
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onLocationChanged(location: Location) {
//        tvGpsLocation = findViewById(R.id.textView)
        ll = Pair(location.longitude, location.latitude)
        Log.d("MY", ll.toString())
        runLastEvents(LocalDate.now())

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == locationPermissionCode) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

}