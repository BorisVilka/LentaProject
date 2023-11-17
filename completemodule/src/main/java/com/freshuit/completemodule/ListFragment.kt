package com.completemodule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.completemodule.databinding.FragmentListBinding
import com.firedata.OrderList
import com.google.firebase.firestore.FirebaseFirestore


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater,container,false)
        binding.list.addItemDecoration(
            DividerItemDecoration(requireContext(),
                DividerItemDecoration.VERTICAL)
        )
        binding.button.setOnClickListener {
            binding.list.visibility = View.INVISIBLE
            binding.progressBar2.visibility = View.VISIBLE
            binding.button.visibility = View.INVISIBLE
            FirebaseFirestore.getInstance().collection("main").document("orders").set(OrderList().apply {
                list = (binding.list.adapter as MyAdapter).data
            }).addOnCompleteListener {
                binding.list.visibility = View.VISIBLE
                binding.progressBar2.visibility = View.INVISIBLE
            }
        }
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        FirebaseFirestore.getInstance().collection("main").document("orders").get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val res = it.result.toObject(OrderList::class.java)
                    binding.progressBar2.visibility = View.INVISIBLE
                    if(res!=null && res.list!=null) {
                        res.list = res!!.list!!.filter { it.status != "Доставлен" }.toMutableList()
                        val adapter = MyAdapter(res.list!!,requireContext()) {
                            binding.button.visibility = View.VISIBLE
                        }
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
    }

}