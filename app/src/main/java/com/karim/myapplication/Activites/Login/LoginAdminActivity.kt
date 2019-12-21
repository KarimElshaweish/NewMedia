package com.karim.myapplication.Activites.Login

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.karim.myapplication.Activites.MainActivity
import com.karim.myapplication.R
import com.karim.myapplication.Util
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login_admin.*

class LoginAdminActivity : AppCompatActivity() {

    lateinit var mAuth:FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_admin)
        mAuth=FirebaseAuth.getInstance()
    }
    fun login(email:String,password:String){
        val dialog: android.app.AlertDialog? = SpotsDialog.Builder()
            .setContext(this)
            .setTheme(R.style.login)
            .build()
        dialog!!.show()
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(object :OnCompleteListener<AuthResult>{
            override fun onComplete(p0: Task<AuthResult>) {
                dialog.dismiss()
                if(p0.isSuccessful){
                    Util.empolyee=false
                    startActivity(Intent(baseContext,MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(baseContext,p0.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun BtnClicked(view: View) {
        var id=view.id
        when(id){
            R.id.btn_login->login(email_text.text.toString(),password_text.text.toString())
        }
    }

}
