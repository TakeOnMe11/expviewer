package com.example.expviewer

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.Entry
import kotlinx.android.synthetic.main.activity_single_exp.*
import kotlin.collections.ArrayList
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet


class SingleExpActivity : AppCompatActivity () {

    private lateinit var expDB: ExpDB
    var id: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_exp)

        val intent = intent
        id = intent.getIntExtra(getString(R.string.key_id), 0)

        expDB = ExpDB(this)
        val exp = expDB.getSingleExp(id)
        Log.d("exp", exp.toString())
        Log.d("exp", exp.setDescription)
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

        val xAxis: ArrayList<Float> = ArrayList()
        val yAxis: ArrayList<Float> = ArrayList()
        val result: ArrayList<Entry> = ArrayList()
        for (i in 0 until words.size step 2){
            xAxis.add(doubleList[i])
        }
        for (i in 1 until words.size step 2){
            yAxis.add(doubleList[i])
        }
        for (i in 0 until yAxis.size){
            result.add(Entry(xAxis[i], yAxis[i]))
        }

        val desc = graph_view.description
        desc.text = ""
        graph_view.setDrawBorders(true)
        val lineDataSet = LineDataSet(result, "Результат эксперимента (Y - imp, X - nm)")
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

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.exp_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_edit){
            val intent = Intent(this, ChangeExpActivity::class.java)
            intent.putExtra(this.getString(R.string.key_id), id)
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }
}