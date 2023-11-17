package com.lentaproject

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.Navigation
import com.google.firebase.auth.FirebaseAuth

class LoaderFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
        if(FirebaseAuth.getInstance().currentUser!=null) {
            startActivity(Intent(requireContext(),MainActivity::class.java))
            requireActivity().finish()
        } else {
            navController.popBackStack()
            navController.navigate(R.id.selectFragment)
        }
        return inflater.inflate(R.layout.fragment_loader, container, false)
    }


}