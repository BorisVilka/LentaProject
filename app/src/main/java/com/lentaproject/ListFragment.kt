package com.lentaproject

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView.OnQueryTextListener
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
        binding.searchView.setOnQueryTextListener(object : OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                adapter.setFilter(p0!!)
                return true
            }

            override fun onQueryTextChange(p0: String?): Boolean {
               return true
            }

        })
        binding.searchView.setOnCloseListener {
            adapter.closeSearch()
            return@setOnCloseListener true
        }
        FirebaseFirestore.getInstance().collection("main").document("list").get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val res = it.result.toObject(StoreList::class.java)
                    binding.progressBar2.visibility = View.INVISIBLE
                    if(res!=null && res.list!=null) {
                        MyApp.all = res.list!!
                        adapter = ListAdapter(res.list!!)
                        binding.list.adapter = adapter
                    } else {
                        binding.textView.text = "Товаров нет в наличии"
                        binding.textView.visibility = View.VISIBLE
                        binding.searchView.visibility = View.INVISIBLE
                    }
                } else {
                    binding.searchView.visibility = View.INVISIBLE
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