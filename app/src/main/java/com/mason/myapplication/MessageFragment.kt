package com.mason.myapplication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.mason.myapplication.databinding.FragmentMessageBinding

/**
 * A simple [Fragment] subclass as the second destination in the navigation.
 */
class MessageFragment : Fragment() {
    private lateinit var smsAdapter: SmsAdapter
    private val smsList: MutableList<Sms> = mutableListOf()
    private var _binding: FragmentMessageBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val profileViewModel: ProfileViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentMessageBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onResume() {
        super.onResume()
        Log.d(Companion.TAG, "XXXXX> onResume: profileViewModel = $profileViewModel")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.buttonSecond.setOnClickListener {
//            findNavController().navigate(R.id.action_Second2Fragment_to_First2Fragment)
        }
//        var defaultSmsPackage = Telephony.Sms.getDefaultSmsPackage(requireContext())
        // check smsAdapter initialized

        smsAdapter = SmsAdapter()
        binding.recyclerViewMessage.adapter = smsAdapter
        binding.recyclerViewMessage.layoutManager = LinearLayoutManager(requireContext())


        grantSmsPermission().also {
            Log.d(TAG, "XXXXX> onViewCreated: grantSmsPermission = $it") }
            .takeIf { it }?.let {
                readAllSms()
            }
    }

     fun readAllSms() {
//         val tv = TypedValue()
//         if (requireActivity().theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
//             val actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
//             // log displaymetrics
//                Log.d(TAG, "XXXXX> readAllSms: displaymetrics = ${resources.displayMetrics}")
//             Log.d(TAG, "XXXXX> readAllSms: actionbarsize = $actionBarHeight")
//         }

//        smsList.removeAll(smsList)
        val cursor = requireActivity().contentResolver.query(
            Uri.parse("content://sms/"),
            null,
            null,
            null,
            null
        )
        cursor?.let {
            smsList.removeAll(smsList)
            // 依序讀取所有簡訊
            while (it.moveToNext()) {
                // 從 cursor 中讀取相關欄位資料
                val id = it.getLong(it.getColumnIndexOrThrow("_id"))
                val address = it.getString(it.getColumnIndexOrThrow("address")).toString()
                val body = it.getString(it.getColumnIndexOrThrow("body")).toString()
                val date = it.getString(it.getColumnIndexOrThrow("date")).toString()
                val type = it.getInt(it.getColumnIndexOrThrow("type"))
                for (i in 0..it.columnCount - 1) {
//                    Log.d(TAG, "XXXXX> onViewCreated: ${it.getColumnName(i)}: ${it.getString(i)}")
                }
                // 將資料封裝成 Sms 物件並加入至列表中
                val sms = Sms(id, address, body, date, type)
                smsList.add(sms)
                Log.d(TAG, "XXXXX> onViewCreated: sms = $sms")
            }
            // 關閉 cursor
            it.close()

            // 更新 RecyclerView 的顯示
            smsAdapter.setSmsList(smsList)
            smsAdapter.notifyDataSetChanged()
            Log.d(TAG, "XXXXX> readAllSms: smsList = $smsList, smsAdapter.itemCound = ${smsAdapter.itemCount}")
        }
    }

    private fun grantSmsPermission(): Boolean {
        // check sms permission
        val smsPermission = android.Manifest.permission.READ_SMS
        val grant = requireActivity().checkCallingOrSelfPermission(smsPermission)
        if (grant != android.content.pm.PackageManager.PERMISSION_GRANTED) {
            // request permission
            requestPermissions(arrayOf(smsPermission), 0)
        }
        return grant == android.content.pm.PackageManager.PERMISSION_GRANTED
//        return true
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val TAG = "MessageFragment"
    }
}