package com.julioguimaraes.projetofinaldm114.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.julioguimaraes.projetofinaldm114.databinding.FragmentOrdersListBinding

private const val TAG = "OrderListFragment"

class OrderListFragment : Fragment() {
    private val orderListViewModel: OrderListViewModel by lazy {
        ViewModelProviders.of(this).get(OrderListViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding = FragmentOrdersListBinding.inflate(inflater)

        binding.setLifecycleOwner(this)

        binding.orderListViewModel= orderListViewModel
        val itemDecor = DividerItemDecoration(context, VERTICAL);

        binding.rcvOrders.addItemDecoration(itemDecor);

        /*
        binding.rcvOrders.adapter = OrderAdapter(OrderAdapter.OrderClickListener {
            Log.i(TAG,"Order selected: ${it.orderId}")

            this.findNavController()
                .navigate(OrdersListFragmentDirections.actionShowOrderDetail(it.code!!))
        })
        */
        return binding.root
    }
}