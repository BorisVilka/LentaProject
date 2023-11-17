package com.lenta.adminmodule

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.firedata.StoreList
import com.firedata.StoreObject
import com.google.firebase.firestore.FirebaseFirestore
import com.lenta.adminmodule.databinding.ListItemBinding
import com.squareup.picasso.Picasso

class MyAdapter(val list: MutableList<StoreObject>, val call: ()-> Unit): RecyclerView.Adapter<MyAdapter.Companion.MyHolder>() {

    companion object {
        class MyHolder(val binding: ListItemBinding): ViewHolder(binding.root) {

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyHolder {
        return MyHolder(ListItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun getItemCount(): Int {
       return list.size
    }

    override fun onBindViewHolder(holder: MyHolder, position: Int) {
        Picasso.get().load(list[position].photoUrl).into(holder.binding.imageView2)
        holder.binding.textView5.text = list[position].name
        holder.binding.textView6.text = "${String.format("%.2f",list[position].price)} руб. / ${list[position].labelPrice}"
        holder.binding.imageView5.setOnClickListener {
            list.removeAt(position)
            val tmp = StoreList()
            tmp.list = list
            FirebaseFirestore.getInstance().collection("main").document("list").set(tmp)
            notifyDataSetChanged()
        }
        holder.binding.root.setOnClickListener {
            MainActivity.obj = list[position]
            call()
        }
    }
}