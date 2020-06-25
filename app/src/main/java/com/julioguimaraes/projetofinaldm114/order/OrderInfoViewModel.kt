package com.julioguimaraes.projetofinaldm114.order

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.julioguimaraes.projetofinaldm114.persistence.Order

class OrderInfoViewModel : ViewModel() {
    val fcmRegistrationId = MutableLiveData<String>()

    val order : MutableLiveData<Order> by lazy {
        MutableLiveData<Order>()
    }
}
