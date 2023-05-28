package com.mason.myapplication.youbike

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Filter.FilterListener
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mason.myapplication.R
import com.mason.myapplication.data.RetrofitManager
import com.mason.myapplication.data.Youbike2RealtimeItem
import com.mason.myapplication.databinding.FragmentYoubikeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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


    var bikeList = mutableListOf<Youbike2RealtimeItem>()

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

        rvYoubike = binding.recyclerviewYoubike
        youbikeRVAdapter = YoubikeRecyclerViewAdapter()
        rvYoubike.adapter = youbikeRVAdapter
        youbikeRVAdapter.setFilterListener(this)
        rvYoubike.layoutManager = LinearLayoutManager(requireContext())
        bikeList.clear()

        loadYoubikeStops()

        searchViewYoubike = binding.searchViewYoubike

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
        searchViewYoubike.setQuery("公園", true)
    }

    fun loadYoubikeStops() = CoroutineScope(Dispatchers.IO).launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            val myAPIService = RetrofitManager.instance.myApi
            val bikeList = mutableListOf<Youbike2RealtimeItem>()

            val interval = 15_000L // 10 秒

            while (isActive) {
                val response = myAPIService.allBikeStop
                val bikeStops = response.execute().body()

                bikeStops.let {
                    bikeList.clear()
                    it?.forEach { bikeStop ->
                        bikeStop.sarea.takeIf { area -> area == "板橋區" }?.let {
                            bikeList.add(bikeStop)
                        }
                    }

                    Log.d(TAG, "XXXXX> Gson YouBike in 板橋: ${bikeList.size} ")

                    withContext(Dispatchers.Main) {
                        youbikeRVAdapter.updateData(bikeList)
                    }
                }

                delay(interval)
            }
        }
    }

    companion object {
        private const val TAG = "YoubikeFragment"
    }

    override fun onFilterComplete(p0: Int) {
        Log.d(TAG, "XXXXX> onFilterComplete: list.size: $p0")
        youbikeViewModel.filterListSize.value = p0
    }
}