package com.example.nush_hack21.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.nush_hack21.R
import com.example.nush_hack21.databinding.FragmentHomeBinding
import com.example.nush_hack21.model.GreenGrade
import com.example.nush_hack21.model.User
import com.example.nush_hack21.user
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_home.*
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        populateCharts()
        historyShortcut.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_historyFragment) }
        cameraShortcut.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_imageFragment) }
    }


    fun populateCharts() {
        fun populateChart1(
            chart: PieChart,
            description: String,
            typeAmountMap: MutableMap<String, Int>?
        ) {
            if( typeAmountMap == null){
                val colors: ArrayList<Int> = ArrayList(mutableListOf(Color.parseColor("#808080")))
                val pieDataset = PieDataSet(listOf(PieEntry(1f,"No data")),"")
                pieDataset.colors = colors
                val pieData = PieData(pieDataset)
                pieData.setDrawValues(false)
                pieData.setValueFormatter(null)
                chart.setDrawEntryLabels(false)
                chart.description.text = description
                chart.data = pieData
                chart.invalidate()
                return
            }
            val pieEntries: ArrayList<PieEntry> = ArrayList()
            val colors: ArrayList<Int> = ArrayList()
//            colors.add(Color.parseColor("#FFC300"))
//            colors.add(Color.parseColor("#41B883"))
//            colors.add(Color.parseColor("#ff6347"))
            colors.add(Color.parseColor("#304567"));
            colors.add(Color.parseColor("#309967"));
            colors.add(Color.parseColor("#476567"));
            for (type in typeAmountMap.keys) {
                pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
            }
            chart.description.text = description
            chart.setDrawEntryLabels(false)
            val pieDataSet = PieDataSet(pieEntries, "")
            pieDataSet.valueTextSize = 0f
            pieDataSet.colors = colors
            val pieData = PieData(pieDataSet)
            pieData.setDrawValues(false)
            pieData.setValueFormatter(null)
            chart.data = pieData
            chart.invalidate()
        }


        val lastWeek = user.history.filter {
            val date = Date(it.timestamp)
            Log.i("stats",date.toString())
            Instant.now().minus(14,ChronoUnit.DAYS) <= date.toInstant() &&
                    date.toInstant() <= Instant.now().minus(7, ChronoUnit.DAYS)
        }.map { it.product.points }.map { GreenGrade.gradeScore(it) }
        val thisWeek = user.history.filter {
            val date = Date(it.timestamp)
            Log.i("stats",date.toString())
            Instant.now().minus(7, ChronoUnit.DAYS) <= date.toInstant()
        }.map { it.product.points }.map { GreenGrade.gradeScore(it) }

        val typeAmountMap: MutableMap<String, Int> = HashMap()
        if(lastWeek.isEmpty()){
            populateChart1(chart1Left, "Last Week", null)
        } else {
            typeAmountMap["Good"] = lastWeek.filter { it == GreenGrade.GOOD }.size
            typeAmountMap["Moderate"] = lastWeek.filter { it == GreenGrade.MODERATE }.size
            typeAmountMap["questionable"] = lastWeek.filter { it == GreenGrade.QUESTIONABLE }.size
            populateChart1(chart1Left, "Last Week", typeAmountMap)
        }
        if(thisWeek.isEmpty()){
            populateChart1(chart1Right, "This Week", null)
        } else {
            typeAmountMap["Good"] = thisWeek.filter { it == GreenGrade.GOOD }.size
            typeAmountMap["Moderate"] = thisWeek.filter { it == GreenGrade.MODERATE }.size
            typeAmountMap["Questionable"] = thisWeek.filter { it == GreenGrade.QUESTIONABLE }.size
            populateChart1(chart1Right, "This Week", typeAmountMap)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}