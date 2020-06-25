package com.julioguimaraes.projetofinaldm114.order

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.ktx.Firebase
import com.julioguimaraes.projetofinaldm114.R
import com.julioguimaraes.projetofinaldm114.databinding.FragmentOrderInfoBinding
import com.julioguimaraes.projetofinaldm114.persistence.Order
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

private const val TAG = "OrderInfoFragment"

class OrderInfoFragment : Fragment() {

    private val orderInfoViewModel: OrderInfoViewModel by lazy {
        ViewModelProviders.of(this).get(OrderInfoViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Log.i(TAG, "Creating OrderInfoFragment")

        val binding = FragmentOrderInfoBinding.inflate(inflater)

        binding.setLifecycleOwner(this)

        binding.orderInfoViewModel = orderInfoViewModel

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    orderInfoViewModel.fcmRegistrationId.value = task.result?.token
                    Log.i("OrderInfoFragment", "FCM Token: ${task.result?.token}")
                }
            }

        if (this.arguments != null) {
            if (this.arguments!!.containsKey("orderInfo")) {
                val moshi = Moshi.Builder().build()
                val jsonAdapter: JsonAdapter<Order> =
                    moshi.adapter<Order>(Order::class.java)

                jsonAdapter.fromJson(this.arguments!!.getString("orderInfo")!!).let {
                    orderInfoViewModel.order.value = it
                }
            }
        }

        return binding.root
    }
}
