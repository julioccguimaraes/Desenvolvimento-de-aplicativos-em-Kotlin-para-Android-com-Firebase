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
    private var productCode: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        Log.i(TAG, "Creating OrderDetailFragment")

        binding = FragmentOrderDetailBinding.inflate(inflater)

        binding.lifecycleOwner = this

        orderId = OrderDetailFragmentArgs.fromBundle(requireArguments()).orderId
        productCode = OrderDetailFragmentArgs.fromBundle(requireArguments()).productCode

        val orderDetailViewModelFactory = OrderDetailViewModelFactory(orderId, productCode)

        binding.orderDetailViewModel = ViewModelProviders.of(
            this, orderDetailViewModelFactory).get(OrderDetailViewModel::class.java)

        /*
        Verificando, com base na configuração do Firebase Remote Config,
        se a função de exclusão de eventos vai estar ou não disponível
         */
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

                /*
                Gera um evento no Firebase Analytics quando o usuário exclui
                um evento de um pedido
                 */
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