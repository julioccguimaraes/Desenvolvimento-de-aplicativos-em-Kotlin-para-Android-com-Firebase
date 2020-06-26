package com.julioguimaraes.projetofinaldm114

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.widget.Toast
import androidx.navigation.findNavController
import com.firebase.ui.auth.AuthUI
import com.google.firebase.FirebaseApp
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.julioguimaraes.projetofinaldm114.order.OrderInfoFragmentDirections
import com.julioguimaraes.projetofinaldm114.order.OrderListFragmentDirections

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*
        Fazendo login do usuário no Firebase Authentication
         */
        FirebaseApp.initializeApp(this)
        val user = FirebaseAuth.getInstance().currentUser

        if (user != null) {
            setContentView(R.layout.activity_main)

            /*
            Criando a instância do Firebase Remote Config
             */
            setFirebaseRemoteConfig()

            if (this.intent.hasExtra("order")) {
                showOrderInfo(intent.getStringExtra("order")!!)
            }
        } else {
            val providers = arrayListOf(AuthUI.IdpConfig.GoogleBuilder().build())

            startActivityForResult(
                AuthUI.getInstance()
                    .createSignInIntentBuilder()
                    .setAvailableProviders(providers)
                    .build(), 1)
        }
    }

    override fun onNewIntent(intent: Intent) {
        if (intent.hasExtra("order")) {
            showOrderInfo(intent.getStringExtra("order")!!)
        }
        super.onNewIntent(intent)
    }

    private fun showOrderInfo(orderInfo: String) {
        this.findNavController(R.id.nav_host_fragment)
            .navigate(OrderInfoFragmentDirections.actionShowOrderInfo(orderInfo))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                setContentView(R.layout.activity_main)

                /*
                Criando a instância do Firebase Remote Config
                 */
                setFirebaseRemoteConfig()
            } else {
                Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.nav_sign_out -> {
                AuthUI.getInstance()
                    .signOut(this)
                    .addOnCompleteListener {
                        this.recreate()
                    }
                true
            }
            R.id.nav_event_history -> {
                Log.d(TAG, "Event history clicked")

                this.findNavController(R.id.nav_host_fragment)
                    .navigate(OrderListFragmentDirections.actionShowOrderList())

                /*
                Gera um evento no Firebase Analytics quando o usuário exibe
                a lista de eventos dos pedidos
                 */
                val firebaseAnalytics = FirebaseAnalytics.getInstance(this)
                firebaseAnalytics.logEvent("show_orders_list_event", null)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    fun setFirebaseRemoteConfig() {
        val remoteConfig = Firebase.remoteConfig

        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 60
        }
        remoteConfig.setConfigSettingsAsync(configSettings)

        val defaultConfigMap: MutableMap<String, Any> = HashMap()

        /*
        Configuração remota para habilitar/desatilitar a função de exclusão de eventos na tela
        de detalhes de pedidos
         */
        defaultConfigMap["delete_detail_view"] = true

        remoteConfig.setDefaultsAsync(defaultConfigMap)

        remoteConfig.fetchAndActivate()
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val updated = task.result
                    Log.d("MainActivity", "Remote config updated: $updated")
                } else {
                    Log.d("MainActivity", "Failed to load remote config")
                }
            }
    }
}