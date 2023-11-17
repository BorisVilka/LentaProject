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
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.google.firebase.auth.UserProfileChangeRequest
import com.lentaproject.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater,container,false)
        binding.phone.addTextChangedListener {
            binding.phone.error = null
        }
        binding.password.addTextChangedListener {
            binding.password.error = null
        }
        val activity = requireActivity()
        binding.button8.setOnClickListener {
            if(!binding.phone.text!!.startsWith('+',true)) binding.phone.error = "Номер должен начинаться с +"
            else if(binding.phone.text!!.isEmpty()) binding.phone.error = "Введите номер"
            else if(binding.password.text!!.length<8) binding.password.error = "Пароль должен быть длинее 8 символов"
            else {
                var string = binding.phone.text.toString().trim()
                string = string.replace("+","plus")
                string = "$string@gmail.com"
                Log.d("TAG",string)
                FirebaseAuth.getInstance().signInWithEmailAndPassword(string,binding.password.text.toString().trim()).addOnCompleteListener {
                    if(it.isSuccessful) {
                        startActivity(Intent(activity,MainActivity::class.java))
                        activity.finish()
                    } else {
                        Toast.makeText(activity,"Ошибка, попробуйте позже",Toast.LENGTH_SHORT).show()
                        when((it.exception as FirebaseException).localizedMessage) {
                            "ERROR_USER_NOT_FOUND" -> Toast.makeText(activity,"Номер не найден",Toast.LENGTH_SHORT).show()
                            "INVALID_LOGIN_CREDENTIALS" -> Toast.makeText(activity,"Неверный пароль",Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
        return binding.root
    }


}