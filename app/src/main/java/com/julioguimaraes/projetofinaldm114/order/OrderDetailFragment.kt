package com.julioguimaraes.projetofinaldm114.order

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.julioguimaraes.projetofinaldm114.R
import com.julioguimaraes.projetofinaldm114.databinding.FragmentOrderDetailBinding

private const val TAG = "OrderDetailFragment"

class OrderDetailFragment : Fragment() {

    private lateinit var binding: FragmentOrderDetailBinding
    private var orderId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Log.i(TAG, "Creating OrderDetailFragment")

        val binding = FragmentOrderDetailBinding.inflate(inflater)

        binding.setLifecycleOwner(this)

        //Fetch the orderId and create the ViewModel here
        orderId = OrderDetailFragmentArgs.fromBundle(arguments!!).orderId

        val productDetailViewModelFactory = OrderDetailViewModelFactory(orderId)

        binding.orderDetailViewModel = ViewModelProviders.of(
            this, productDetailViewModelFactory).get(OrderDetailViewModel::class.java)

        val remoteConfig = Firebase.remoteConfig
        setHasOptionsMenu(remoteConfig.getBoolean("delete_detail_view"))

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.order_detail_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.delete_order -> {
                binding.orderDetailViewModel?.deleteOrder()

                //val firebaseAnalytics = FirebaseAnalytics.getInstance(this.context!!)
                val firebaseAnalytics = FirebaseAnalytics.getInstance(this.requireContext())
                val bundle = Bundle()
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, orderId)
                firebaseAnalytics.logEvent("delete_item", bundle)

                findNavController().popBackStack()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}