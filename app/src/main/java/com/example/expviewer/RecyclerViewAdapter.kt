package com.example.expviewer

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.exp_data_list.view.*
import java.util.*


class RecyclerViewAdapter(private val data: ArrayList<ExpData>, val context: Context):
    RecyclerView.Adapter<RecyclerViewAdapter.Holder>(), Filterable {

    private var dataFull = ArrayList<ExpData>(data)

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(data.get(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.exp_data_list, parent, false) as View
        return Holder(view)
    }

    override fun getItemCount(): Int = data.size

    inner class Holder(v: View): RecyclerView.ViewHolder(v), View.OnClickListener {

        var expId = 0
        var expData = ExpData()

        fun bind(expDataDTO: ExpData) = with(itemView){
            expId = expDataDTO.id
            tv_expname_list.text = expDataDTO.name
            tv_expdate_list.text = expDataDTO.measDate
            tv_expcomment_list.text = expDataDTO.comment
            itemView.setOnClickListener(this@Holder)
        }


        override fun onClick(p0: View?) {
            val intent = Intent(context, SingleExpActivity::class.java)
            intent.putExtra(context.getString(R.string.key_id), expId)
            context.startActivity(intent)
        }

    }

    override fun getFilter(): Filter {
        return exampleFilter
    }

    private val exampleFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence): FilterResults {
            val filteredList: MutableList<ExpData> = ArrayList<ExpData>()
            if (constraint == null || constraint.length == 0) {
                filteredList.addAll(dataFull)
            } else {
                val filterPattern = constraint.toString().toLowerCase().trim { it <= ' ' }
                for (item in dataFull) {
                    if (item.name.toLowerCase().contains(filterPattern) || item.comment.toLowerCase().contains(filterPattern)){
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(constraint: CharSequence, results: FilterResults) {
            data.clear()
            data.addAll(results.values as ArrayList<ExpData>)
            notifyDataSetChanged()
        }
    }
}