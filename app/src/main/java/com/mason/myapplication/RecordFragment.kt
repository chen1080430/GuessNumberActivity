package com.mason.myapplication

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.mason.myapplication.databinding.FragmentRecordBinding
import kotlinx.coroutines.*

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class RecordFragment : Fragment() {
    private var _binding: FragmentRecordBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    val recordAdapter = RecordAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonFrag2.setOnClickListener {
            findNavController().navigate(R.id.action_SecondFragment_to_FirstFragment)
        }

        binding.buttonGuessAgain.setOnClickListener {
            findNavController().navigate(R.id.go_guess_number_page)
        }

        // create recycler view to print out all records

        //create coroutine scope
        var coroutineScope = CoroutineScope(Dispatchers.IO)
        coroutineScope.launch {
//        AsyncTask.execute() {
            var db = Room.databaseBuilder(
                requireContext(),
                GameDatabases::class.java,
                "games.db"
            ).build()
            var records = db.recordDao().getAll()
            Log.d(TAG, "onViewCreated: $records")

            recordAdapter.recordList = records
//            recordAdapter.notifyDataSetChanged()
            // run on main thread
            withContext(Dispatchers.Main) {
                binding.recyclerviewRecord.layoutManager = LinearLayoutManager(requireContext())
                binding.recyclerviewRecord.adapter = recordAdapter
            }
//            binding.recyclerviewRecord.post {
//                binding.recyclerviewRecord.layoutManager = LinearLayoutManager(requireContext())
//                binding.recyclerviewRecord.adapter = recordAdapter
//            }
//            binding.recyclerviewRecord.layoutManager = LinearLayoutManager(requireContext())
//            binding.recyclerviewRecord.adapter = recordAdapter
//        }
        }

//        binding.recyclerviewRecord.layoutManager = LinearLayoutManager(requireContext())
//        binding.recyclerviewRecord.adapter = recordAdapter

    }

    inner class RecordAdapter() : RecyclerView.Adapter<RecordViewHolder>() {
        lateinit var recordList: List<Record>

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
            var inflate = LayoutInflater.from(parent.context)
            var view = inflate.inflate(R.layout.record_layout, parent, false)
            return RecordViewHolder(view)
        }

        override fun getItemCount(): Int {
            return recordList.size
        }

        override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
//            holder.name.text = "Mason"
//            holder.counter.text = "100"
            recordList.let {
                holder.name.text = recordList[position].name
                holder.counter.text = recordList[position].counter.toString()
            }
        }

    }

    inner class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var name: TextView = itemView.findViewById(R.id.text_name)
        var counter: TextView = itemView.findViewById(R.id.text_count)
//        var counter : TextView = itemView.text_counter

    }

    override fun onStart() {
        super.onStart()
        Thread() {

            var recordList =
                Room.databaseBuilder(requireContext(), GameDatabases::class.java, "games.db")
                    .build()
                    .recordDao()
                    .getAll()
            recordList.forEach {
                Log.d(
                    Companion.TAG,
                    "onStart: id: ${it.id}, name: ${it.name}, counter: ${it.counter}"
                )

            }
        }.start()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "RecordFragment"
    }
}