package com.julioguimaraes.projetofinaldm114.persistence

import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class Order(
    @Exclude var id: String? = null,
    var username: String? = null,
    var orderId: String? = null,
    var status: String? = null,
    var productCode: String? = null,
    var userId: String? = null,
    var date: String? = null
)