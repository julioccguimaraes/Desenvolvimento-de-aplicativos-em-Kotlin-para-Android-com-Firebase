package com.julioguimaraes.projetofinaldm114.order

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.julioguimaraes.projetofinaldm114.network.Product
import com.julioguimaraes.projetofinaldm114.network.SalesApi
import com.julioguimaraes.projetofinaldm114.persistence.Order
import com.julioguimaraes.projetofinaldm114.persistence.OrderRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

private const val TAG = "OrderDetailViewModel"

class OrderDetailViewModel(private val orderId: String?, private val productCode: String?): ViewModel() {
    // coroutines
    private var viewModelJob = Job()
    private val coroutineScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    lateinit var order: MutableLiveData<Order>
    private var _product = MutableLiveData<Product>()

    val product: MutableLiveData<Product>
        get() = _product

    init {
        if (orderId != null && productCode != null) {
            getOrder(orderId)
            getProduct(productCode)
        } else {
            order = MutableLiveData<Order>()
            order.value = Order()

            _product = MutableLiveData<Product>()
            _product.value = Product()
        }
    }

    private fun getOrder(orderId: String) {
        order = OrderRepository.getOrderByOrderId(orderId)
    }

    private fun getProduct(code: String) {
        Log.i(TAG, "Preparing to request a product by its code")

        coroutineScope.launch {
            var getProductDeferred = SalesApi.retrofitService.getProductByCode(code)

            try {
                Log.i(TAG, "Loading product by its code")

                var productByCode = getProductDeferred.await()

                Log.i(TAG, "Name of the product ${productByCode.name}")

                _product.value = productByCode
            } catch (e: Exception) {
                Log.i(TAG, "Error: ${e.message}")
            }
        }
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
        viewModelJob.cancel()
    }
}