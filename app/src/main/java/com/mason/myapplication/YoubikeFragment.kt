package com.mason.myapplication

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.mason.myapplication.data.Youbike2RealtimeItem
import com.mason.myapplication.databinding.FragmentYoubikeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.Call
//import okhttp3.*
import java.io.IOException
import java.net.URL
import kotlin.system.measureTimeMillis
import kotlin.time.ExperimentalTime
import okhttp3.logging.HttpLoggingInterceptor

import okhttp3.OkHttpClient
import okhttp3.Request


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [YoubikeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YoubikeFragment : Fragment() {
    private lateinit var youbikeRVAdapter: YoubikeRecyclerViewAdapter
    private lateinit var rvYoubike: RecyclerView
    private lateinit var binding: FragmentYoubikeBinding
    val youbike2RealtimeURL =
        "https://data.ntpc.gov.tw/api/datasets/010e5b15-3823-4b20-b401-b1cf000550c5/json?size=1000000"


    var client: OkHttpClient = OkHttpClient()
//        .newBuilder()
//    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
//    .build()

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    // create a list to store Youbike2RealtimeItem

    var bikeList = ArrayList<Youbike2RealtimeItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentYoubikeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @OptIn(ExperimentalTime::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rvYoubike = binding.recyclerviewYoubike
        youbikeRVAdapter = YoubikeRecyclerViewAdapter()
        rvYoubike.adapter = youbikeRVAdapter
        rvYoubike.layoutManager = LinearLayoutManager(requireContext())
        bikeList.clear()

        loadYoubikeInfo()

    }

    private fun loadYoubikeInfo() {
        CoroutineScope(Dispatchers.IO).launch {

            // 建立Request，設置連線資訊
            val request: Request = Request.Builder()
                .url(youbike2RealtimeURL)
                .build()

            // 建立Call
            val call: Call = client.newCall(request)

            // 執行Call連線到網址
            val result: String
            // 執行Call連線到網址
            call.execute().use { response ->
                // 取得連線結果
                result = response.body?.string() ?: ""
                Log.d(TAG, "XXXXX> okHttp excute:  result :$result")

            }
            var fromJson =
                Gson().fromJson(result, Array<Youbike2RealtimeItem>::class.java).asList()
            fromJson.forEach { bikeStop ->
                bikeStop.sarea.takeIf { bikeStop.sarea == "板橋區" }?.run {
//                                    Log.d(TAG, "XXXXX> Gson fromJson: bikeStop: $bikeStop")
                    bikeList.add(bikeStop)
                }
            }
            Log.d(TAG, "XXXXX> Gson youBike in 板橋: ${bikeList.size} ")
//            Log.d(TAG, "XXXXX> Gson YouBike in 新北: ${bikeList.size} ")
            /*
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
// 連線成功，自response取得連線結果
                    val result: String = response.body?.string() ?: ""
                    Log.d("OkHttp result", result)
                }
            })
            */

            // update ui view
            CoroutineScope(Dispatchers.Main).launch {
                youbikeRVAdapter.updateData(bikeList)
            }
        }.let { Log.d(TAG, "XXXXX> okHttp. measureTimeMillis: $it") }


/*
        Thread(Runnable {
            measureTimeMillis {
                val readText =
                    URL(youbike2RealtimeURL).readText()
                /*
                //            Log.d(TAG, "XXXXX> onViewCreated: $readText")
                                    var fromJson =
                                        Gson().fromJson(readText, Array<Youbike2RealtimeItem>::class.java).asList()

                //            Log.d(TAG, "XXXXX> Gson youBike data: $fromJson")
                                    fromJson.forEach { bikeStop ->
                //                bikeStop.sarea.takeIf { bikeStop.sarea=="板橋區" }?.run {
                //                    Log.d(TAG, "XXXXX> Gson fromJson: bikeStop: $bikeStop")
                                        bikeList.add(bikeStop)
                //                }
                                    }
                //            Log.d(TAG, "XXXXX> Gson youBike in 板橋: ${bikeList.size} ")
                                    Log.d(TAG, "XXXXX> Gson YouBike in 新北: ${bikeList.size} ")
                */
            }.apply { Log.d(TAG, "XXXXX>Native. measureTimeMillis: $this") }

            measureTimeMillis {

                // 建立Request，設置連線資訊
                val request: Request = Request.Builder()
                    .url(youbike2RealtimeURL)
                    .build()

                // 建立Call

                // 建立Call
                val call: Call = client.newCall(request)

                // 執行Call連線到網址

                // 執行Call連線到網址
                call.execute().use { response ->
                    // 取得連線結果
                    val result: String = response.body?.string() ?: ""
                    Log.d(TAG, "XXXXX> okHttp excute:  result :$result")
                }
                /*
                call.enqueue(object : Callback {
                    override fun onFailure(call: Call, e: IOException) {
                    }

                    override fun onResponse(call: Call, response: Response) {
// 連線成功，自response取得連線結果
                        val result: String = response.body?.string() ?: ""
                        Log.d("OkHttp result", result)
                    }
                })
                */
            }.let { Log.d(TAG, "XXXXX> okHttp. measureTimeMillis: $it") }

            //            var jsonArray = JSONArray(readText)
            //            for (i in 0 until jsonArray.length()-1) {
            //                var bikeInfo = jsonArray.getJSONObject(i)
            ////                Log.d(TAG, "XXXXX> onViewCreated: $bikeInfo")
            //                var stopName = bikeInfo.getString("sna")
            //                var stopTotal = bikeInfo.getInt("tot")
            //                var stopSbi = bikeInfo.getInt("sbi")
            ////                bikeList.add(Youbike2RealtimeItem(stopName, stopTotal, stopSbi))
            //
            //            }
            //            val bikeResult = Gson().fromJson(readText, Youbike2RealtimeItem::class.java)
            //             log all bikeResult
            //            bikeResult.forEach {
            //                Log.d(Companion.TAG, "XXXXX> bike: $bikeResult")
            //            }


            activity?.runOnUiThread {
                youbikeRVAdapter.bikeInfo = bikeList
                youbikeRVAdapter.notifyDataSetChanged()

            }
        }).start()

 */
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment YoubikeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            YoubikeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }

        private const val TAG = "YoubikeFragment"
    }
}