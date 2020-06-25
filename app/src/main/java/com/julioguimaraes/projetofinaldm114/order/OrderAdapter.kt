package com.julioguimaraes.projetofinaldm114.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.julioguimaraes.projetofinaldm114.databinding.ItemOrderBinding
import com.julioguimaraes.projetofinaldm114.persistence.Order
import com.google.firebase.analytics.FirebaseAnalytics

class OrderAdapter(val onOrderClickListener: OrderClickListener) :
    ListAdapter<Order, OrderAdapter.OrderViewHolder>(OrderDiff) {
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OrderAdapter.OrderViewHolder {
        firebaseAnalytics = FirebaseAnalytics.getInstance(parent.context)
        return OrderViewHolder(ItemOrderBinding.inflate(LayoutInflater.from(parent.context)))
    }

    override fun onBindViewHolder(holder: OrderAdapter.OrderViewHolder, position: Int) {
        val order = getItem(position)
        holder.bind(order)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, order.orderId)
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_ITEM, bundle)

            onOrderClickListener.onClick(order)
        }

        holder.itemView.setOnLongClickListener {
            val bundle = Bundle()
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, order.orderId)
            firebaseAnalytics.logEvent("attempt_delete_order", bundle)
            true
        }
    }

    class OrderViewHolder(private var binding: ItemOrderBinding):
        RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order) {
            binding.order = order
            binding.executePendingBindings()
        }
    }

    companion object OrderDiff : DiffUtil.ItemCallback<Order>() {
        override fun areItemsTheSame(oldItem: Order, newItem: Order): Boolean {
            return oldItem === newItem
        }
        override fun areContentsTheSame(oldItem: Order, newItem: Order): Boolean {
            return ((oldItem.id == newItem.id)
                    && (oldItem.orderId.equals(newItem.orderId))
                    && (oldItem.status.equals(newItem.status))
                    && (oldItem.date == newItem.date))
        }
    }

    class OrderClickListener(val clickListener: (order: Order) -> Unit) {
        fun onClick(order: Order) = clickListener(order)
    }
}