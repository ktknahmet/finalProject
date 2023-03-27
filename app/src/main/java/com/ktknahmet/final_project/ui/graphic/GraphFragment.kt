package com.ktknahmet.final_project.ui.graphic


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.crazylegend.kotlinextensions.views.onClick
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartModel
import com.github.aachartmodel.aainfographics.aachartcreator.AAChartType
import com.github.aachartmodel.aainfographics.aachartcreator.AASeriesElement
import com.ktknahmet.final_project.R

import com.ktknahmet.final_project.databinding.FragmentGraphBinding
import com.ktknahmet.final_project.ui.base.BaseFragment
import com.ktknahmet.final_project.utils.Constant
import java.text.SimpleDateFormat
import java.util.*


class GraphFragment : BaseFragment<FragmentGraphBinding>(FragmentGraphBinding::inflate)  {
    @SuppressLint("SimpleDateFormat")
    private var dateNow = Date()
    private val sdf2 = SimpleDateFormat("dd-MM-yyyy")
    private var faturaTip = ""
    var selectDateTime = sdf2.format(dateNow)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.selectDate.setText(selectDateTime)
        faturalar()
        graph()
        binding.selectDate.onClick {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                requireActivity(),
                R.style.MaterialCalendarCustomStyle,
                { _, y, m, d ->
                    val ay = m + 1
                    selectDateTime = ("$d-$ay-$y")
                    binding.selectDate.setText(selectDateTime)
                },
                year,
                month,
                day
            )
            dpd.show()
        }
    }
    private fun faturalar() {
        val listFatura: ArrayList<String> = ArrayList()
        listFatura.addAll(Constant.BORCTIP)

        val spinnerAdapter = activity?.let {
            ArrayAdapter(it, R.layout.view_spinner_dropdown_item, listFatura)
        }!!

        binding.spinnerFatura.setAdapter(spinnerAdapter)
        binding.spinnerFatura.onItemClickListener =
            AdapterView.OnItemClickListener { parent, _, position, _ ->
                faturaTip = parent.getItemAtPosition(position) as String

            }

    }

    private fun graph(){
        val aaChartModel : AAChartModel = AAChartModel()
            .chartType(AAChartType.Area)
            .title("title")
            .subtitle("subtitle")
            .dataLabelsEnabled(true)
            .series(arrayOf(
                AASeriesElement()
                    .name("Tokyo")
                    .data(arrayOf(7.0, 6.9, 9.5, 14.5, 18.2, 21.5, 25.2, 26.5, 23.3, 18.3, 13.9, 9.6)),
                AASeriesElement()
                    .name("NewYork")
                    .data(arrayOf(0.2, 0.8, 5.7, 11.3, 17.0, 22.0, 24.8, 24.1, 20.1, 14.1, 8.6, 2.5)),
                AASeriesElement()
                    .name("London")
                    .data(arrayOf(0.9, 0.6, 3.5, 8.4, 13.5, 17.0, 18.6, 17.9, 14.3, 9.0, 3.9, 1.0)),
                AASeriesElement()
                    .name("Berlin")
                    .data(arrayOf(3.9, 4.2, 5.7, 8.5, 11.9, 15.2, 17.0, 16.6, 14.2, 10.3, 6.6, 4.8))
            )
            )

        //The chart view object calls the instance object of AAChartModel and draws the final graphic
        binding.chartView.aa_drawChartWithChartModel(aaChartModel)
    }

}