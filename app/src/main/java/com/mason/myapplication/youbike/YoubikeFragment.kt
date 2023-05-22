package com.mason.myapplication.youbike

//import okhttp3.*

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Filter.FilterListener
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mason.myapplication.R
import com.mason.myapplication.data.RetrofitManager
import com.mason.myapplication.data.Youbike2RealtimeItem
import com.mason.myapplication.databinding.FragmentYoubikeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Response
import kotlin.time.ExperimentalTime


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

/**
 * A simple [Fragment] subclass.
 * Use the [YoubikeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class YoubikeFragment : Fragment(), FilterListener {
    private lateinit var searchViewYoubike: SearchView
    private lateinit var youbikeRVAdapter: YoubikeRecyclerViewAdapter
    private lateinit var rvYoubike: RecyclerView
    private lateinit var binding: FragmentYoubikeBinding
    val youbikeViewModel by viewModels<YoubikeViewModel>()
    val youbike2RealtimeURL =
        "https://data.ntpc.gov.tw/api/datasets/010e5b15-3823-4b20-b401-b1cf000550c5/json?size=1000000"


    var client: OkHttpClient = OkHttpClient()
//        .newBuilder()
//    .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC))
//    .build()


    var bikeList = ArrayList<Youbike2RealtimeItem>()
//    var bikeList = List<Youbike2RealtimeItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentYoubikeBinding.inflate(inflater, container, false)
            .apply {
                viewModel = youbikeViewModel
                lifecycleOwner = viewLifecycleOwner
            }
        return binding.root
    }

    @OptIn(ExperimentalTime::class)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        youbikeViewModel = YoubikeViewModel()
        rvYoubike = binding.recyclerviewYoubike
        youbikeRVAdapter = YoubikeRecyclerViewAdapter()
        rvYoubike.adapter = youbikeRVAdapter
        youbikeRVAdapter.setFilterListener(this)
        rvYoubike.layoutManager = LinearLayoutManager(requireContext())
        bikeList.clear()

        loadYoubikeInfo()
        youbikeRVAdapter.updateFilter("板橋區")

//        youbikeViewModel.filterListSize.observe(viewLifecycleOwner) {
//            Log.d(TAG, "XXXXX> filterListSize: $it")
//            binding.textViewYoubikeTitle.text = getString(R.string.youbike_realtime_data)+" $it"
//        }


        searchViewYoubike = binding.searchViewYoubike
//        searchViewYoubike.isIconifiedByDefault = false

        searchViewYoubike.setOnQueryTextListener(object :
            androidx.appcompat.widget.SearchView.OnQueryTextListener,
            SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.d(TAG, "XXXXX> onQueryTextSubmit: $query")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                Log.d(TAG, "XXXXX> onQueryTextChange: $newText")
                youbikeRVAdapter.updateFilter(newText ?: "")
                return false
            }
        })
    }

    private fun loadYoubikeInfo() {
        /*
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
            *//*
            call.enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                }

                override fun onResponse(call: Call, response: Response) {
// 連線成功，自response取得連線結果
                    val result: String = response.body?.string() ?: ""
                    Log.d("OkHttp result", result)
                }
            })
            *//*

            // update ui view
            CoroutineScope(Dispatchers.Main).launch {
                youbikeRVAdapter.updateData(bikeList)
            }
        }.let { Log.d(TAG, "XXXXX> okHttp. measureTimeMillis: $it") }
*/
        bikeList.clear()
        Log.d(TAG, "XXXXX>  YouBike size: ${bikeList.size} ")

        CoroutineScope(Dispatchers.IO).launch {
            val myAPIService = RetrofitManager.instance.myApi

            // 3. 建立連線的Call，此處設置call為myAPIService中的getAlbums()連線

            // 3. 建立連線的Call，此處設置call為myAPIService中的getAlbums()連線
            val call = myAPIService.allBikeStop

            // 4. 執行call

            // 4. 執行call
            call.enqueue(object : Callback<List<Youbike2RealtimeItem?>> {
                override fun onResponse(
                    call: retrofit2.Call<List<Youbike2RealtimeItem?>>,
                    response: Response<List<Youbike2RealtimeItem?>>
                ) {
                    // 連線成功
                    // 回傳的資料已轉成Youbike2RealtimeItem物件，可直接用get方法取得特定欄位
                    response.body()?.forEach { bikeStop ->
                        bikeStop!!.sarea.takeIf { it == "板橋區" }?.let {
                            bikeList.add(bikeStop)
                        }

//                        Log.d(TAG,"Youbike2RealtimeItem bikeStop: bikeStop")
//                        bikeList.add(bikeStop!!)
                    }
                    Log.d(TAG, "XXXXX> Gson YouBike in 板橋: ${bikeList.size} ")
                    CoroutineScope(Dispatchers.Main).launch {
                        youbikeRVAdapter.updateData(bikeList)
                        searchViewYoubike.setQuery("四維", true)
                    }
                }

                override fun onFailure(call: retrofit2.Call<List<Youbike2RealtimeItem?>>, t: Throwable?) {
                    // 連線失敗
                    Log.d(TAG, "XXXXX> onFailure: 連線失敗")
                }
            })

        }

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
        private const val TAG = "YoubikeFragment"
    }

    override fun onFilterComplete(p0: Int) {
        Log.d(TAG, "XXXXX> onFilterComplete: list.size: $p0")
        youbikeViewModel.filterListSize.value = p0
    }
}