package com.julioguimaraes.projetofinaldm114.order

import android.widget.TextView
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.julioguimaraes.projetofinaldm114.persistence.Order
import java.text.SimpleDateFormat

@BindingAdapter("ordersList")
fun bindOrdersList(recyclerView: RecyclerView, orders: List<Order>?) {
    val adapter = recyclerView.adapter as OrderAdapter
    adapter.submitList(orders)
}

@BindingAdapter("orderDate")
fun bindOrderDate(txtOrderDate: TextView, orderDate: String?) {
    orderDate?.let {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd hh:mm")
        val dateString: String = simpleDateFormat.format(orderDate)

        txtOrderDate.text = dateString
    }
}
