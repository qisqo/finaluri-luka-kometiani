package com.usmaan.tictactoe

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.renderscript.Sampler
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ProfileActivity : AppCompatActivity() {

    private lateinit var imageView: ImageView
    private lateinit var textView: TextView
    private lateinit var editTextUrl: EditText
    private lateinit var editTextPersonName: EditText
    private lateinit var buttonSave : Button
    private lateinit var buttonLogout : Button
    private lateinit var buttonPlay : Button

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseDatabase.getInstance().getReference("UserInfo")



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
            inint()
            registerListener()
            db.child(auth.currentUser?.uid!!).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val userInfo: UserInfo = snapshot.getValue(UserInfo::class.java) ?: return

                    Glide.with(this@ProfileActivity)
                        .load(userInfo.imageUrl)
                        .placeholder(R.drawable.ic_launcher_foreground)
                        .into(imageView)
                    textView.text = userInfo.fullName
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }
    private fun inint() {
        imageView = findViewById(R.id.imageView)
        textView =findViewById(R.id.textview)
        editTextUrl = findViewById(R.id.editTextUrl)
        editTextPersonName= findViewById(R.id.editTextPersonName)
        buttonSave=findViewById(R.id.buttonSave)
        buttonLogout=findViewById(R.id.buttonLogout)
        buttonPlay=findViewById(R.id.buttonPlay)
    }
    private fun registerListener() {
        buttonLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        buttonSave.setOnClickListener {
            val fullname: String = editTextPersonName.text.toString()
            val url: String = editTextUrl.text.toString()

            val userInfo = UserInfo(fullname, url)




            db.child(auth.currentUser?.uid!!).setValue(userInfo)
        }
        buttonPlay.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()



        }
    }
}