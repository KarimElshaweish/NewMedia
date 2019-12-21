package com.karim.myapplication.Activites

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.auth.FirebaseAuth
import com.karim.myapplication.Activites.Login.LoginAdminActivity
import com.karim.myapplication.R
import com.karim.myapplication.Util
import kotlinx.android.synthetic.main.item.*

class FirebaseAutActivity : AppCompatActivity() {
    val providers = arrayListOf(
        AuthUI.IdpConfig.GoogleBuilder().build()
       )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_firebase_aut)
       // showSingInOption()
    }
    var RC_SIGN_IN=7117
    private fun showSingInOption() {
        startActivityForResult(
            AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .build(),
            RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val response = IdpResponse.fromResultIntent(data)
            if (resultCode == Activity.RESULT_OK) {
                // Successfully signed in
                val user = FirebaseAuth.getInstance().currentUser
                Toast.makeText(this,user?.email.toString(),Toast.LENGTH_LONG).show()
                    Util.empolyee = true
                    startActivity(Intent(this,MainActivity::class.java))
                finish()
            } else {
                Toast.makeText(this, response.toString(),Toast.LENGTH_LONG).show()
            }
        }
    }

    lateinit var text:String
    fun btnClicked(view: View) {
        text=(view as Button).text.toString()
        when(text){
           "تسجيل كمدير" -> {
               Util.empolyee = false
               startActivity(Intent(this, LoginAdminActivity::class.java))
           }
            else-> showSingInOption()
        }
    }
}
