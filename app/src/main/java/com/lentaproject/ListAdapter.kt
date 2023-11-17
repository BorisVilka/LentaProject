package com.lentaproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.firedata.StoreObject
import com.lentaproject.databinding.ListItemBinding
import com.squareup.picasso.Picasso
import kotlin.math.max

class ListAdapter(val all: List<StoreObject>): RecyclerView.Adapter<ListAdapter.Companion.MyHolder>() {

    var data = mutableListOf<StoreObject>()

    init {
        data.addAll(all)
    }
    companion object {
        class MyHolder(val binding: ListItemBinding): ViewHolder(binding.root) {
            var count = 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return data.size
    }


    public fun setFilter(filter: String) {
        data.clear()
        data.addAll(all.filter { it.name!!.contains(filter) })
        notifyDataSetChanged()
    }

    public fun closeSearch() {
        data.clear()
        data.addAll(all)
        notifyDataSetChanged()
    }
    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        holder.binding.imageView5.setImageResource(if(MyApp.list.contains(data[position])) R.drawable.baseline_check_24 else R.drawable.outline_shopping_cart_checkout_24)
        holder.binding.imageView5.setOnClickListener {
            if(!MyApp.list.contains(data[position])) {
                MyApp.list.add(data[position])
                holder.binding.imageView5.setImageResource(R.drawable.baseline_check_24)
            } else {
                MyApp.list.remove(data[position])
                holder.binding.imageView5.setImageResource(R.drawable.outline_shopping_cart_checkout_24)
            }
        }
        data[position].count = 1
        holder.binding.textView7.text = data[position].count.toString()
        holder.binding.imageView4.setOnClickListener {
            data[position].count = data[position].count!! + 1
            holder.binding.textView7.text = data[position].count.toString()
        }
        holder.binding.imageView3.setOnClickListener {
            data[position].count = data[position].count!! - 1
            holder.count = max(1,holder.count)
            holder.binding.textView7.text = data[position].count.toString()
        }
        Picasso.get().load(data[position].photoUrl).into(holder.binding.imageView2)
        holder.binding.textView5.text = data[position].name
        holder.binding.textView6.text = "${String.format("%.2f",data[position].price)} руб. / ${data[position].labelPrice}"
    }

    override fun onViewRecycled(holder: MyHolder) {
        super.onViewRecycled(holder)
    }
}