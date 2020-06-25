package com.julioguimaraes.projetofinaldm114.order

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.julioguimaraes.projetofinaldm114.persistence.Order
import com.julioguimaraes.projetofinaldm114.persistence.OrderRepository

private const val TAG = "OrderListViewModel"

class OrderListViewModel: ViewModel() {

    private var _orders = MutableLiveData<List<Order>>()
    val orders: LiveData<List<Order>>
        get() = _orders

    init {
       getOrders()
    }

    private fun getOrders() {
        _orders = OrderRepository.getOrders()
    }

    override fun onCleared() {
        super.onCleared()
    }

    fun refreshOrders() {
        _orders.value = null
        getOrders()
    }
}