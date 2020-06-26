package com.julioguimaraes.projetofinaldm114.order

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.firebase.iid.FirebaseInstanceId
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

        binding.lifecycleOwner = this

        binding.orderInfoViewModel = orderInfoViewModel

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    orderInfoViewModel.fcmRegistrationId.value = task.result?.token
                    Log.i("OrderInfoFragment", "FCM Token: ${task.result?.token}")
                }
            }

        if (this.arguments != null) {
            if (this.requireArguments().containsKey("orderInfo")) {
                val moshi = Moshi.Builder().build()
                val jsonAdapter: JsonAdapter<Order> =
                    moshi.adapter<Order>(Order::class.java)

                jsonAdapter.fromJson(this.requireArguments().getString("orderInfo")!!).let {
                    orderInfoViewModel.order.value = it
                }
            }
        }

        return binding.root
    }
}
