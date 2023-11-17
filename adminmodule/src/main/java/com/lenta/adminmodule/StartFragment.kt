package com.lenta.adminmodule

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.firedata.StoreList
import com.google.firebase.firestore.FirebaseFirestore
import com.lenta.adminmodule.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private lateinit var binding: FragmentStartBinding
    lateinit var adapter: MyAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentStartBinding.inflate(inflater,container,false)
        binding.floatingActionButton.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.navigate(R.id.addFragment)
        }
        binding.floatingActionButton2.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.navigate(R.id.editFragment)
        }
        val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
        FirebaseFirestore.getInstance().collection("main").document("list").get()
            .addOnCompleteListener {
                if(it.isSuccessful) {
                    val res = it.result.toObject(StoreList::class.java)
                    binding.progressBar3.visibility = View.INVISIBLE
                    if(res!=null && res.list!=null) {
                        adapter = MyAdapter(res.list!!) {
                            navController.navigate(R.id.addFragment)
                        }
                        binding.list.adapter = adapter
                    } else {
                       // binding.textView.text = "Товаров нет в наличии"
                      //  binding.textView.visibility = View.VISIBLE
                    }
                } else {
                    it.exception!!.printStackTrace()
                   // binding.progressBar3.visibility = View.INVISIBLE
                   // binding.textView.text = "Произошла ошибка. Попробуйте позже"
                   // binding.textView.visibility = View.VISIBLE
                    Log.d("TAG",it.exception.toString())
                }
            }
        return binding.root
    }


}