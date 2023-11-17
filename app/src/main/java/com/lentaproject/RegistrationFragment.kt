package com.lentaproject

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserProfileChangeRequest
import com.lentaproject.databinding.FragmentRegistrationBinding


class RegistrationFragment : Fragment() {

    private lateinit var binding: FragmentRegistrationBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegistrationBinding.inflate(inflater,container,false)
        binding.name.addTextChangedListener {
            binding.name.error = null
        }
        binding.phone.addTextChangedListener {
            binding.phone.error = null
        }
        binding.password.addTextChangedListener {
            binding.password.error = null
        }
        val activity = requireActivity()
        binding.button8.setOnClickListener {
            if(binding.name.text!!.isEmpty()) binding.name.error = "Введите имя"
            else if(!binding.phone.text!!.startsWith('+',true)) binding.phone.error = "Номер должен начинаться с +"
            else if(binding.phone.text!!.isEmpty()) binding.phone.error = "Введите номер"
            else if(binding.password.text!!.length<8) binding.password.error = "Пароль должен быть длинее 8 символов"
            else {
                var string = binding.phone.text.toString().trim()
                string = string.replace("+","plus")
                string = "$string@gmail.com"
                Log.d("TAG",string)
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(string,binding.password.text.toString().trim()).addOnCompleteListener {
                    if(it.isSuccessful) {
                        val user = FirebaseAuth.getInstance().currentUser
                        user!!.updateProfile(UserProfileChangeRequest.Builder().setDisplayName("${binding.name.text.toString().trim()}|").build())
                        startActivity(Intent(activity,MainActivity::class.java))
                        activity.finish()
                    } else {
                       when((it.exception as FirebaseAuthException).errorCode) {
                           "ERROR_EMAIL_ALREADY_IN_USE" -> Toast.makeText(activity,"Номер уже занят",Toast.LENGTH_SHORT).show()
                           "ERROR_WEAK_PASSWORD" -> Toast.makeText(activity,"Неверный пароль",Toast.LENGTH_SHORT).show()
                       }
                    }
                }
            }
        }
        return binding.root
    }


}