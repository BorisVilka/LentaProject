package com.lentaproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.firedata.OrderList
import com.firedata.StoreList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lentaproject.databinding.FragmentOrdersBinding


class OrdersFragment : Fragment() {

    private lateinit var binding: FragmentOrdersBinding
    lateinit var adapter: OrderAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOrdersBinding.inflate(inflater,container,false)
        FirebaseFirestore.getInstance().collection("main").document("orders").get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val res = it.result.toObject(OrderList::class.java)
                    binding.progressBar2.visibility = View.INVISIBLE
                    val user = FirebaseAuth.getInstance().currentUser
                    if(res!=null && res.list!=null) {
                        res.list = res!!.list!!.filter { it.phone == user!!.phoneNumber!!}.toMutableList()
                        adapter = OrderAdapter(res.list!!)
                        binding.list.adapter = adapter
                    } else {
                        binding.textView.text = "Нет заказов"
                        binding.textView.visibility = View.VISIBLE
                    }
                } else {
                    binding.progressBar2.visibility = View.INVISIBLE
                    binding.textView.text = "Произошла ошибка. Попробуйте позже"
                    binding.textView.visibility = View.VISIBLE
                    Log.d("TAG",it.exception.toString())
                }
            }
        return binding.root
    }


}