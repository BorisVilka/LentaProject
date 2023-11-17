package com.freshuit.completemodule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.navigation.Navigation
import com.completemodule.R
import com.completemodule.databinding.FragmentLoaderBinding
import com.google.firebase.firestore.FirebaseFirestore

class LoaderFragment : Fragment() {

    private lateinit var binding: FragmentLoaderBinding
    var key = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoaderBinding.inflate(inflater,container,false)
        FirebaseFirestore.getInstance().collection("main").document("key").get().addOnCompleteListener {
            key =  it.result.getString("key")!!
            binding.progressBar.visibility = View.GONE
        }
        binding.pass.addTextChangedListener {
            binding.pass.error = null
        }
        binding.button2.setOnClickListener {
            if(binding.pass.text.toString().trim()==key) {
                val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                navController.popBackStack()
                navController.navigate(R.id.listFragment)
            } else binding.pass.error = "Неверный пароль"
        }
        return binding.root
    }


}