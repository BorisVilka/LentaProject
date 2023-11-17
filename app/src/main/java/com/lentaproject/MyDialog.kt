package com.lentaproject

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.lentaproject.databinding.DialogBinding

class MyDialog(val call: ()->Unit): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var adb = AlertDialog.Builder(requireContext())
        val binding = DialogBinding.inflate(layoutInflater,null,false)
        val user = FirebaseAuth.getInstance().currentUser
        val arr = user!!.displayName!!.trim().split("|")
        binding.fio.setText(arr[0])
        var adrs = ""
        binding.adress.addTextChangedListener {
            binding.adress.error = null
        }
        binding.floor.addTextChangedListener {
            binding.floor.error = null
        }
        binding.podNumber.addTextChangedListener {
            binding.podNumber.error = null
        }
        binding.domofon.addTextChangedListener {
            binding.domofon.error = null
        }
        binding.kvNumber.addTextChangedListener {
            binding.kvNumber.error = null
        }
        binding.button5.setOnClickListener {
            if(binding.adress.text!!.isEmpty()) binding.adress.error = "Введите точный адрес"
            else if(binding.floor.text!!.isEmpty()) binding.floor.error = "Введите этаж"
            else if(binding.domofon.text!!.isEmpty()) binding.domofon.error = "Введите домофон"
            else if(binding.podNumber.text!!.isEmpty()) binding.podNumber.error = "Введите подъезд"
            else if(binding.kvNumber.text!!.isEmpty()) binding.kvNumber.error = "Введите номер квартиры"
            else {
                adrs = "${binding.adress.text.toString()}, п. ${binding.podNumber.text.toString()}, эт. ${binding.floor.text.toString()}, кв. ${binding.kvNumber.text.toString()}, домофон ${binding.domofon.text.toString()}"
                FirebaseAuth.getInstance().currentUser!!.updateProfile(UserProfileChangeRequest.Builder().setDisplayName("${binding.fio.text}|${adrs}").build()).addOnCompleteListener {
                    call()
                    dismiss()
                }
            }

        }
        adb = adb.setView(binding.root)
        val dialog = adb.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}