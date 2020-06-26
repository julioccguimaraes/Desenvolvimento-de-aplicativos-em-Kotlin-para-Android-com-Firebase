package com.julioguimaraes.projetofinaldm114.order

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.julioguimaraes.projetofinaldm114.persistence.Order
import com.julioguimaraes.projetofinaldm114.persistence.OrderRepository

private const val TAG = "OrderDetailViewModel"

class OrderDetailViewModel(private val orderId: String?): ViewModel() {
    lateinit var order: MutableLiveData<Order>

    init {
        if (orderId != null) {
            getOrder(orderId)
        } else {
            order = MutableLiveData<Order>()
            order.value = Order()
        }

    }

    private fun getOrder(orderId: String) {
        order = OrderRepository.getOrderByOrderId(orderId)
    }

    fun deleteOrder() {
        if (order.value?.id != null) {
            OrderRepository.deleteOrder(order.value!!.id!!)
            order.value = null
        }
    }
    override fun onCleared() {
        Log.i(TAG, "onCleared")
        super.onCleared()
    }
}