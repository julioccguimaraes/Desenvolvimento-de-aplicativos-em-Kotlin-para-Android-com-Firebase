package com.julioguimaraes.projetofinaldm114.messaging

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.julioguimaraes.projetofinaldm114.MainActivity
import com.julioguimaraes.projetofinaldm114.R
import com.julioguimaraes.projetofinaldm114.persistence.Order
import com.julioguimaraes.projetofinaldm114.persistence.OrderRepository
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi

private const val TAG = "FCMService"

class FCMService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d(TAG, "FCM refreshed token: $token")
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        remoteMessage.data.isNotEmpty().let {
            Log.d(TAG, "Order Detail: " + remoteMessage.data)

            val user = FirebaseAuth.getInstance().currentUser
            val order = getOrderInfo(remoteMessage.data.get("orderDetail")!!)

            if (user != null) {

                // a mensagem é gerada somente se é destinada para o mesmo usuário logado no app
                if (remoteMessage.data.containsKey("orderDetail") && user.email == order.username) {
                    sendOrderNotification(remoteMessage.data.get("orderDetail")!!)

                    // salvando o evento no Firestore
                    saveOrder(order)
                }
            }
        }
    }

    private fun sendOrderNotification(orderInfo: String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("order", orderInfo)
        sendNotification(intent)
    }

    private fun sendNotification(intent: Intent) {
        val pendingIntent = PendingIntent.getActivity(
            this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val channelId = "1"

        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setContentTitle("Sales Message")
            .setAutoCancel(true)
            .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Sales provider",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }
        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun getOrderInfo(orderInfo: String): Order {
        val moshi = Moshi.Builder().build()
        val jsonAdapter: JsonAdapter<Order> = moshi.adapter<Order>(Order::class.java)

        return jsonAdapter.fromJson(orderInfo)!!
    }

    private fun saveOrder(order: Order) {
        if (order != null) {
            OrderRepository.saveOrder(order)
        }
    }
}

