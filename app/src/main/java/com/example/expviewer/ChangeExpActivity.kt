package com.example.expviewer

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import io.reactivex.Observer
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_change_exp.*
import retrofit2.Response

class ChangeExpActivity : AppCompatActivity () {

    private lateinit var expDB: ExpDB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_exp)

        val intent = intent
        val id = intent.getIntExtra(getString(R.string.key_id), 0)

        expDB = ExpDB(this)
        val exp = expDB.getSingleExp(id)
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

        val desc = graph_view_ed.description
        desc.text = ""
        graph_view_ed.setDrawBorders(true)
        val lineDataSet = LineDataSet(result, "Результат эксперимента (Y - imp, X - nm)")
        lineDataSet.setDrawCircles(false)
        lineDataSet.color = Color.RED
        graph_view_ed.data = LineData(lineDataSet)

        if (exp.name != null)
            et_expname_edit.setText(exp.name)
        if (exp.measDate != null)
            et_expmeasdate_edit.setText(exp.measDate)
        if (exp.comment != null)
            et_expcomment_edit.setText(exp.comment)
        if (exp.setName != null)
            tv_setname_var_change.text = exp.setName
        if (exp.setDescription != null)
            tv_setdesc_var_change.text = exp.setDescription
        if (exp.typeName != null)
            tv_typedesc_var_change.text = exp.typeName

        btn_save_change.setOnClickListener{
            if (et_expname_edit.text.toString() != null)
                exp.name = et_expname_edit.text.toString()
            if (et_expmeasdate_edit.text.toString() != null)
                exp.measDate = et_expmeasdate_edit.text.toString()
            if (et_expcomment_edit.text.toString() != null)
                exp.comment = et_expcomment_edit.text.toString()
            postData(exp)
        }
    }

    private fun postData (exp: ExpData) {
        RetrofitClientInstance.getInstance()
            .postAllExp(exp)
            .subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe(object: Observer<Response<ExpData>>{
                override fun onComplete() {}

                override fun onSubscribe(d: Disposable) {}

                override fun onNext(t: Response<ExpData>) {
                    if (t.isSuccessful){
                        Toast.makeText(this@ChangeExpActivity, getString(R.string.post_ok), Toast.LENGTH_SHORT).show()
                        expDB.addExp(exp)
                    } else {
                        Toast.makeText(this@ChangeExpActivity, getString(R.string.post_error), Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onError(e: Throwable) {
                    e.printStackTrace()
                }

            })
    }
}