package com.example.nush_hack21.ui.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.nush_hack21.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import kotlinx.android.synthetic.main.fragment_home.*
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
  ): View? {
    homeViewModel =
            ViewModelProvider(this).get(HomeViewModel::class.java)

    _binding = FragmentHomeBinding.inflate(inflater, container, false)
    val root: View = binding.root

    return root
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

//    populateCharts()

  }


  fun populateCharts() {
    fun populateChart1(chart: PieChart,description: String, typeAmountMap: MutableMap<String,Int>) {
      val pieEntries: ArrayList<PieEntry> = ArrayList()
      val colors: ArrayList<Int> = ArrayList()
      colors.add(Color.parseColor("#FFC300"))
      colors.add(Color.parseColor("#41B883"))
      colors.add(Color.parseColor("#ff6347"))
      for (type in typeAmountMap.keys) {
        pieEntries.add(PieEntry(typeAmountMap[type]!!.toFloat(), type))
      }
      chart.description.text = description
        chart.setDrawEntryLabels(false)
      val pieDataSet = PieDataSet(pieEntries,"")
      pieDataSet.valueTextSize = 0f;
      pieDataSet.colors = colors
      val pieData = PieData(pieDataSet)
      pieData.setDrawValues(false)
      pieData.setValueFormatter(null)
      chart.data = pieData
      chart.invalidate()
    }


//    val lastWeek = user?.history?.filter{
//      val date = Date(it.timestamp)
//        Date().before <=
//        true
//    }


    val typeAmountMap: MutableMap<String, Int> = HashMap()
    typeAmountMap["Good"] = 200
    typeAmountMap["Moderate"] = 230
    typeAmountMap["Nefarious"] = 100
    populateChart1(chart1Left,"Last Week",typeAmountMap)
    typeAmountMap["Good"] = 200
    typeAmountMap["Moderate"] = 230
    typeAmountMap["Nefarious"] = 100
    populateChart1(chart1Right,"This Week",typeAmountMap)


  }


  override fun onDestroyView() {
    super.onDestroyView()
        _binding = null
    }
}