package com.lentaproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DividerItemDecoration
import com.firedata.StoreList
import com.firedata.StoreObject
import com.google.firebase.firestore.FirebaseFirestore
import com.lentaproject.databinding.FragmentListBinding


class ListFragment : Fragment() {

    private lateinit var binding: FragmentListBinding
    private lateinit var adapter: ListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentListBinding.inflate(inflater,container,false)
        binding.list.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.HORIZONTAL))
        binding.list.addItemDecoration(DividerItemDecoration(requireContext(),DividerItemDecoration.VERTICAL))
        FirebaseFirestore.getInstance().collection("main").document("list").get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val res = it.result.toObject(StoreList::class.java)
                    binding.progressBar2.visibility = View.INVISIBLE
                    if(res!=null && res.list!=null) {
                        adapter = ListAdapter(res.list!!)
                        binding.list.adapter = adapter
                    } else {
                        binding.textView.text = "Товаров нет в наличии"
                        binding.textView.visibility = View.VISIBLE
                    }
                } else {
                    it.exception!!.printStackTrace()
                    binding.progressBar2.visibility = View.INVISIBLE
                    binding.textView.text = "Произошла ошибка. Попробуйте позже"
                    binding.textView.visibility = View.VISIBLE
                    Log.d("TAG",it.exception.toString())
                }
            }
        return binding.root
    }


}