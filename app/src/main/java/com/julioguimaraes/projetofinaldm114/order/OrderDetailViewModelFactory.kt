package com.julioguimaraes.projetofinaldm114.order

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class OrderDetailViewModelFactory(private val orderId: String?, private val productCode: String?) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(OrderDetailViewModel::class.java)) {
            return OrderDetailViewModel(orderId, productCode) as T
        }
        throw IllegalArgumentException("The OrderDetailViewModel class is required")
    }

}
