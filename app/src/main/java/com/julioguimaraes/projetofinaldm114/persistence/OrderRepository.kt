package com.julioguimaraes.projetofinaldm114.persistence

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject

private const val TAG = "OrderRepository"
private const val COLLECTION = "orders"
private const val FIELD_USER_ID = "userId"
private const val FIELD_USERNAME = "username"
private const val FIELD_ORDER_ID = "orderId"
private const val FIELD_STATUS = "status"
private const val FIELD_PRODUCT_CODE = "productCode"
private const val FIELD_DATE = "date"

object OrderRepository {
    private val firebaseAuth: FirebaseAuth by lazy {
        FirebaseAuth.getInstance()
    }

    private val firebaseFirestore: FirebaseFirestore by lazy {
        FirebaseFirestore.getInstance()
    }

    fun saveOrder(order: Order): String {
        val document = if (order.id != null) {
            firebaseFirestore.collection(COLLECTION).document(order.id!!)
        } else {
            order.userId = firebaseAuth.uid!! //order.userId = firebaseAuth.getUid()!!
            order.date = System.currentTimeMillis().toString()
            firebaseFirestore.collection(COLLECTION).document()
        }
        document.set(order)

        return document.id
    }

    fun deleteOrder(orderId: String) {
        val document = firebaseFirestore.collection(COLLECTION).document(orderId)
        document.delete()
    }

    fun getOrders(): MutableLiveData<List<Order>> {
        val liveOrders = MutableLiveData<List<Order>>()

        firebaseFirestore.collection(COLLECTION)
            .whereEqualTo(FIELD_USER_ID, firebaseAuth.uid)
            .orderBy(FIELD_DATE, Query.Direction.DESCENDING)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                    return@addSnapshotListener
                }

                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val orders = ArrayList<Order>()
                    querySnapshot.forEach {
                        val order = it.toObject<Order>()
                        order.id = it.id
                        orders.add(order)
                    }
                    liveOrders.postValue(orders)
                } else {
                    Log.d(TAG, "No order has been found")
                }
            }

        return liveOrders
    }

    fun getOrderByOrderId(orderId: String): MutableLiveData<Order> {
        val liveOrder: MutableLiveData<Order> = MutableLiveData()

        firebaseFirestore.collection(COLLECTION)
            .whereEqualTo(FIELD_ORDER_ID, orderId)
            .whereEqualTo(FIELD_USER_ID, firebaseAuth.uid)
            .addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                if (firebaseFirestoreException != null) {
                    Log.w(TAG, "Listen failed.", firebaseFirestoreException)
                    return@addSnapshotListener
                }
                if (querySnapshot != null && !querySnapshot.isEmpty) {
                    val orders = ArrayList<Order>()
                    querySnapshot.forEach {
                        val order = it.toObject<Order>()
                        order.id = it.id
                        orders.add(order)
                    }
                    liveOrder.postValue(orders[0])
                } else {
                    Log.d(TAG, "No order has been found")
                }
            }
        return liveOrder
    }
}
