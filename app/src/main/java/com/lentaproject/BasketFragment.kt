package com.lentaproject

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.Navigation
import com.firedata.Order
import com.firedata.OrderList
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.lentaproject.databinding.FragmentBasketBinding
import ru.yoomoney.sdk.kassa.payments.Checkout.createTokenizationResult
import ru.yoomoney.sdk.kassa.payments.Checkout.createTokenizeIntent
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.Amount
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.GooglePayParameters
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.MockConfiguration
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentMethodType
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.PaymentParameters
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.SavePaymentMethod
import ru.yoomoney.sdk.kassa.payments.checkoutParameters.TestParameters
import java.math.BigDecimal
import java.util.Currency
import java.util.Locale
import java.util.UUID

class BasketFragment : Fragment() {

    private lateinit var binding: FragmentBasketBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentBasketBinding.inflate(inflater,container,false)
        binding.list.adapter = BasketAdapter(MyApp.list)
        binding.button4.visibility = if(MyApp.list.isNotEmpty()) View.VISIBLE else View.INVISIBLE
        binding.button4.setOnClickListener {
            //onTokenizeButtonCLick()
            val user = FirebaseAuth.getInstance().currentUser
            var arr = user!!.displayName!!.trim().split("|")
            if(!(user!!.displayName!!.isNullOrEmpty() || arr[0].isNullOrEmpty() || arr[1].isNullOrEmpty())) onTokenizeButtonCLick()
            else {
                val dialog = MyDialog() {
                   onTokenizeButtonCLick()
                }
                dialog.show(requireActivity().supportFragmentManager,"TAG")
            }
        }
        return binding.root
    }

    private fun onTokenizeButtonCLick() {
        val paymentMethodTypes = setOf(
            PaymentMethodType.BANK_CARD,
            PaymentMethodType.SBERBANK,
            PaymentMethodType.SBP,
        )
        var sum = 0.0
        for(i in (binding.list.adapter as BasketAdapter).data) sum+=i.price!!*i.count!!
        val paymentParameters = PaymentParameters(
            amount = Amount(BigDecimal.valueOf(sum), Currency.getInstance("RUB")),
            title = "Заказ",
            subtitle = "",
            clientApplicationKey = "test_w2eZqJqMwjLDlQvySzx6NRTsb7KZqNxyrh93pn12jFE",
            shopId = "268816",
            savePaymentMethod = SavePaymentMethod.OFF,
            paymentMethodTypes = paymentMethodTypes,
            gatewayId = "",
            customReturnUrl = "https://ya.ru",
            userPhoneNumber = FirebaseAuth.getInstance().currentUser!!.email!!.replace("plus","+").removeSuffix("@gmail.com"),
            googlePayParameters = GooglePayParameters(),
            authCenterClientId = UUID.randomUUID().toString()
        )

        val intent = createTokenizeIntent(requireContext(), paymentParameters, TestParameters(showLogs = true, mockConfiguration = MockConfiguration(completeWithError = false)))
        startActivityForResult(intent, REQUEST_CODE_TOKENIZE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE_TOKENIZE) {
            when (resultCode) {
                RESULT_OK -> {
                    // successful tokenization
                    val user = FirebaseAuth.getInstance().currentUser
                    val navController = Navigation.findNavController(requireActivity(),R.id.fragmentContainerView)
                    val result = data?.let { createTokenizationResult(it) }

                    val order = Order().apply {
                        phone = FirebaseAuth.getInstance().currentUser!!.email!!.replace("plus","+").removeSuffix("@gmail.com")
                        products = MyApp.list.toMutableList()
                    }
                    MyApp.list.clear()
                    FirebaseFirestore.getInstance().collection("main").document("orders").get().addOnCompleteListener {
                        var res = OrderList()
                        if(it.isSuccessful) {
                            res = it.result.toObject(OrderList::class.java)!!
                        }
                        if(res?.list == null) res = OrderList().apply { list = mutableListOf() }
                        res.list!!.add(order)
                        FirebaseFirestore.getInstance().collection("main").document("orders").set(res).addOnCompleteListener {
                            Toast.makeText(requireContext(),"Заказ добавлен",Toast.LENGTH_SHORT).show()
                            navController.popBackStack()
                        }
                    }
                }
                RESULT_CANCELED -> {
                    // user canceled tokenization
                }
            }
        }
    }

    private fun showError() {
        Toast.makeText(requireContext(), "ERROR", Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_CODE_TOKENIZE = 1
    }
}