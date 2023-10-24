package com.lentaproject

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.ktx.Firebase
import com.lentaproject.databinding.DialogBinding

class MyDialog(val call: ()->Unit): DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        var adb = AlertDialog.Builder(requireContext())
        val binding = DialogBinding.inflate(layoutInflater,null,false)
        binding.button5.setOnClickListener {
            FirebaseAuth.getInstance().currentUser!!.updateProfile(UserProfileChangeRequest.Builder().setDisplayName("${binding.fio.text}|${binding.adres.text}").build())
            call()
            dismiss()
        }
        adb = adb.setView(binding.root)
        val dialog = adb.create()
        dialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        return dialog
    }
}