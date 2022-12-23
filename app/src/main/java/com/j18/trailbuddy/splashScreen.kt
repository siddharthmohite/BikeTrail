package com.j18.trailbuddy

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn

class splashScreen : AppCompatActivity() {
    private lateinit var register: Button
    private lateinit var Guest:Button
    override fun onCreate(savedInstanceState: Bundle?) {


        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        register=this.findViewById(R.id.registerButton)
        Guest=this.findViewById(R.id.GuestText)
        var mediaPlayer = MediaPlayer.create(this, R.raw.cycle)
        mediaPlayer.start()

        register.setOnClickListener{
            val intent= Intent(this,SignUp::class.java)
            startActivity(intent)
        }
        Guest.setOnClickListener{
            val intent=Intent(this,MainActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onStart() {
        super.onStart()
        if (GoogleSignIn.getLastSignedInAccount(this) != null) {
            Toast.makeText(this,"Already Signed in with Google", Toast.LENGTH_SHORT).show()
            startActivity(
                Intent(
                    this, MainActivity
                    ::class.java
                )
            )
            finish()
        }
    }

}