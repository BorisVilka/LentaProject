package com.lenta.adminmodule

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.firedata.StoreList
import com.firedata.StoreObject
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.lenta.adminmodule.databinding.FragmentAddBinding
import java.io.ByteArrayOutputStream
import java.util.Random


class AddFragment : Fragment() {

    private val SELECT_PICTURE = 1

    private var selectedImagePath: String? = null

    private lateinit var binding: FragmentAddBinding

    private val arr = arrayListOf("кг","литр")
    var ind =0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddBinding.inflate(inflater,container,false)
        binding.imageView.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(
                    intent,
                    "Select Picture"
                ), SELECT_PICTURE
            )
        }
        binding.title.addTextChangedListener {
            binding.title.error = null
        }
        binding.price.addTextChangedListener {
            binding.price.error = null
        }
        val activity = requireActivity()
        val navController = Navigation.findNavController(activity,R.id.fragmentContainerView)
        binding.radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            if(binding.radioButton.isChecked) ind = 0
            else ind = 1
        }
        binding.button2.setOnClickListener {
            if(binding.title.text!!.isEmpty()) {
                binding.title.error = "Введите название"
            } else if(binding.price.text!!.isEmpty()) {
                binding.price.error = "Введите цену"
            } else {
                binding.progressBar.visibility = View.VISIBLE
                val random = Random()
                binding.imageView.isDrawingCacheEnabled = true
                binding.imageView.buildDrawingCache()
                val bitmap = (binding.imageView.drawable as BitmapDrawable).bitmap
                val baos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
                val data = baos.toByteArray()
                val ref = FirebaseStorage.getInstance().reference.child("images/${binding.title.text}${random.nextInt(Int.MAX_VALUE)}")
                ref.putBytes(data)
                    .continueWithTask { it1->
                        if(!it1.isSuccessful) {
                            it1.exception?.let { throw it }
                        }
                        ref.downloadUrl
                    }
                    .addOnCompleteListener { it1 ->
                        if(it1.isSuccessful) {
                            val url = it1.result.toString()
                            Log.d("TAG",url)
                            val obj = StoreObject(
                                name = binding.title.text.toString(),
                                count = 0,
                                labelPrice =  "",
                                price = binding.price.text.toString().toDouble(),
                                photoUrl =  url
                            )
                            FirebaseFirestore.getInstance().collection("main").document("list").get()
                                .addOnCompleteListener {
                                    val r: StoreList? = if(it.isSuccessful) {
                                        it.result.toObject(StoreList::class.java)
                                    } else {
                                        StoreList().apply { list = mutableListOf() }
                                    }
                                    r!!.list!!.add(obj)
                                    FirebaseFirestore.getInstance().collection("main").document("list").set(r).addOnCompleteListener {
                                        Toast.makeText(activity,"Товар успешно добавлен",Toast.LENGTH_SHORT).show()
                                    }
                                    navController.popBackStack()
                                }
                        }
                    }
            }
        }
        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SELECT_PICTURE) {
                val selectedImageUri = data!!.data
                binding.imageView.setImageURI(selectedImageUri)
            }
        }
    }
}