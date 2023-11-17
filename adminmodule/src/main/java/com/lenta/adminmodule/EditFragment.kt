package com.lenta.adminmodule

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.google.firebase.firestore.FirebaseFirestore
import com.lenta.adminmodule.databinding.FragmentEditBinding
import java.util.Objects

class EditFragment : Fragment() {

    var key = ""
    private lateinit var binding: FragmentEditBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentEditBinding.inflate(inflater,container,false)
        FirebaseFirestore.getInstance().collection("main").document("key").get().addOnCompleteListener {
            key =  it.result.getString("key")!!
            binding.pass.setText(key)
            binding.progressBar.visibility = View.GONE
        }
        binding.button2.setOnClickListener {
              FirebaseFirestore.getInstance().collection("main").document("key").set(HashMap<String,String>().apply {
                put("key",binding.pass.text.toString().trim())
            })
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.popBackStack()
            Toast.makeText(requireContext(),"Пароль сохранен",Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }


}