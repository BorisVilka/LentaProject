package com.lentaproject

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.firedata.StoreObject
import com.lentaproject.databinding.BasketItemBinding
import com.lentaproject.databinding.ListItemBinding
import com.squareup.picasso.Picasso
import kotlin.math.max

class BasketAdapter(val data: List<StoreObject>): RecyclerView.Adapter<BasketAdapter.Companion.MyHolder1>() {

    companion object {
        class MyHolder1(val binding: BasketItemBinding): RecyclerView.ViewHolder(binding.root) {
            var count = 0
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder1 {
        return MyHolder1(BasketItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
        return MyApp.list.size
    }

    override fun onBindViewHolder(holder: MyHolder1, position: Int) {
        holder.binding.imageView5.setOnClickListener {
            MyApp.list.remove(data[position])
            notifyDataSetChanged()
        }
        holder.binding.textView7.text = data[position].count.toString()
        holder.binding.textView9.text = "${data[position].count} * ${data[position].price} = ${data[position].count!! * data[position].price!!}"
        holder.binding.imageView4.setOnClickListener {
            data[position].count = data[position].count?.plus(1)
            holder.binding.textView7.text = data[position].count.toString()
            holder.binding.textView9.text = "${data[position].count} * ${data[position].price} = ${data[position].count!! * data[position].price!!}"
        }
        holder.binding.imageView3.setOnClickListener {
            data[position].count = data[position].count?.minus(1)
            data[position].count = max(1,data[position].count!!)
            holder.binding.textView7.text = data[position].count.toString()
            holder.binding.textView9.text = "${data[position].count} * ${data[position].price} = ${data[position].count!! * data[position].price!!}"
        }
        Picasso.get().load(data[position].photoUrl).into(holder.binding.imageView2)
        holder.binding.textView5.text = data[position].name
        holder.binding.textView6.text = "${String.format("%.2f",data[position].price)} руб. / ${data[position].labelPrice}"
    }

    override fun onViewRecycled(holder: MyHolder1) {
        super.onViewRecycled(holder)
    }
}