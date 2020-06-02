package com.example.expviewer

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.activity_single_exp.*
import java.util.*
import kotlin.collections.ArrayList
import java.util.Arrays.asList
import android.R.attr.x
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class SingleExpActivity : AppCompatActivity () {

    private lateinit var expDB: ExpDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_exp)

        val intent = intent
        val id = intent.getIntExtra(getString(R.string.key_id), 0)

        expDB = ExpDB(this)
        val exp = expDB.getSingleExp(id)
        Log.d("exp", exp.setDescription)
        Log.d("exp", exp.setName)
        Log.d("exp", exp.typeName)
        val data = exp.data
        val words:Array<String>  = data.split("\\s+".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
        for (i in words.indices) {
            words[i] = words[i].replace("[^\\w.]".toRegex(), "")
        }
        val doubleList = FloatArray(words.size)
        for (i in 0 until words.size) {
            doubleList[i] = java.lang.Float.parseFloat(words[i])
        }

        val nums: ArrayList<Float> = ArrayList()
        val xAxis: ArrayList<String> = ArrayList()
        val yAxis: ArrayList<Entry> = ArrayList()
        for (i in 0 until words.size step 2){
            xAxis.add(words[i])
        }
        for (i in 1 until words.size step 2){
            nums.add(doubleList[i])
        }
        for (i in 0 until nums.size){
            yAxis.add(Entry(xAxis[i].toFloat(), nums[i]))
        }

        val lineDataSet = LineDataSet(yAxis, "result")
        lineDataSet.setDrawCircles(false)
        lineDataSet.color = Color.RED
        graph_view.data = LineData(lineDataSet)

        if (exp.name != null)
            tv_exp_name_var.text = exp.name
        if (exp.measDate != null)
            tv_exp_measdate_var.text = exp.measDate
        if (exp.comment != null)
            tv_exp_comment_var.text = exp.comment
        if (exp.setName != null)
            tv_exp_setname_var.text = exp.setName
        if (exp.setDescription != null)
            tv_exp_setdesc_var.text = exp.setDescription
        if (exp.typeName != null)
            tv_exp_typedesc_var.text = exp.typeName
    }
}