package com.lentaproject

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth
import com.lentaproject.databinding.FragmentProfileBinding


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater,container,false)
        val user = FirebaseAuth.getInstance().currentUser
        if(user!!.displayName.isNullOrEmpty()) {
             binding.textView2.text = "Имя не указано"
            binding.textView3.text = "Адрес не указан"
        } else {
            val arr = user.displayName!!.split("|")
            binding.textView2.text = arr[0]
            binding.textView3.text = arr[1]
        }
        binding.textView4.text = "Номер телефона: ${user.phoneNumber}"
        binding.button2.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            requireActivity().finish()
        }
        binding.button3.setOnClickListener {
            val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
            navController.navigate(R.id.ordersFragment)
        }
        return binding.root
    }


}