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
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.julioguimaraes.projetofinaldm114.order.OrderInfoFragmentDirections
import com.julioguimaraes.projetofinaldm114.order.OrderListFragmentDirections

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            val name = user.displayName
            val email = user.email
            setContentView(R.layout.activity_main)

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
            val response = IdpResponse.fromResultIntent(data)

            if (resultCode == Activity.RESULT_OK) {
                val user = FirebaseAuth.getInstance().currentUser
                setContentView(R.layout.activity_main)
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
                Log.d(TAG, "Event History clicked")
                this.findNavController(R.id.nav_host_fragment)
                    .navigate(OrderListFragmentDirections.actionShowOrderList())
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}