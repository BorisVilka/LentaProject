package com.lentaproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.firedata.Order
import com.lentaproject.databinding.OrderItemBinding

class OrderAdapter(val data: MutableList<Order>): RecyclerView.Adapter<OrderAdapter.Companion.OrderHolder>() {

    companion object {
        class OrderHolder(val binding: OrderItemBinding): ViewHolder(binding.root) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrderHolder {
        return OrderHolder(OrderItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
      return data.size
    }

    override fun onBindViewHolder(holder: OrderHolder, position: Int) {
        var sb = "Товары:\n"
        for(i in data[position].products!!) sb = sb+"${i.name} ${i.count} ${i.labelPrice}\n"
        holder.binding.textView6.text = sb
        holder.binding.textView10.text = data[position].status
        var sum = 0.0
        for(i in data[position].products!!) sum+=i.price!!*i.count!!
        holder.binding.textView12.text = "${sum} руб."
    }
}