package com.completemodule

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.RecyclerView
import com.completemodule.databinding.ListItemBinding
import com.firedata.Order


class MyAdapter(public val data: MutableList<Order>, val ctx: Context,val callback: ()->Unit): RecyclerView.Adapter<MyAdapter.Companion.OrderHolder>() {

    companion object {
        class OrderHolder(val binding: ListItemBinding): RecyclerView.ViewHolder(binding.root) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        return OrderHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        var sb = "Товары:\n"
        for(i in data[position].products!!) sb = sb+"${i.name} ${i.count} ${i.labelPrice}\n"
        holder.binding.textView6.text = sb
        //holder.binding.textView10.text = data[position].status
        var sum = 0.0
        for(i in data[position].products!!) sum+=i.price!!*i.count!!
        holder.binding.textView12.text = "${sum} руб."
        holder.binding.textView2.text = data[position].phone

        val adapter: ArrayAdapter<*> = ArrayAdapter.createFromResource(
            ctx, R.array.status,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        holder.binding.spin.adapter = adapter
        holder.binding.spin.setSelection((holder.binding.spin.adapter as ArrayAdapter<String>).getPosition(data[position].status))
        var first = true
        holder.binding.spin.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(!first) {
                    data[position].status = ctx.resources.getStringArray(R.array.status)[p2]
                    callback()
                    notifyItemChanged(position)
                } else first = false
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }
}