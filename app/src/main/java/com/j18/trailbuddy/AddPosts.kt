package com.j18.trailbuddy

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.j18.trailbuddy.databinding.ActivityAddPostsBinding
import com.j18.trailbuddy.databinding.ActivityMainBinding

class AddPosts : AppCompatActivity() {
    val ImageBack=1
    val REQUEST_CODE=200
    private lateinit var storageRef: StorageReference
    private lateinit var submit: Button
    private lateinit var comments: EditText
    private lateinit var upload:ImageView
    lateinit var binding: ActivityAddPostsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPostsBinding.inflate(layoutInflater)
        val view=binding.root
        setContentView(view)
        submit=this.findViewById(R.id.submit)
        comments=this.findViewById(R.id.comments)
        upload=this.findViewById(R.id.upload)

        binding.upload.setOnClickListener{
            val intent= Intent(Intent.ACTION_GET_CONTENT)
            intent.setType("image/*")
            startActivityForResult(intent,ImageBack)

        }



        storageRef= FirebaseStorage.getInstance().reference.child("Images")



    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == ImageBack && data != null) {
            upload.setImageBitmap(data.extras?.get("data") as Bitmap?)
        }

        submit.setOnClickListener {

            val comments=comments.text.toString()

            if (requestCode == ImageBack) {
                if (resultCode == RESULT_OK) {
                    val imageData = data!!.getData()
                    val imageName: StorageReference =
                        storageRef.child("images" + imageData?.lastPathSegment)
                    imageName.putFile(imageData!!)
                        .addOnSuccessListener(OnSuccessListener { taskSnapshot ->
                            Log.e("inside", "inside upload image")
                            imageName.getDownloadUrl()
                                .addOnSuccessListener(OnSuccessListener<Uri> { uri ->
                                    Log.e("inside", "inside the downloadurl")
                                    val databaseReference: DatabaseReference =
                                        FirebaseDatabase.getInstance()
                                            .getReferenceFromUrl("https://trailbuddy-632fb-default-rtdb.firebaseio.com")
                                            .child("Image")

                                    val hashMap: HashMap<String, String> = HashMap()
                                    val userId =databaseReference.push().key!!
                                    val imageUri= uri.toString()
                                    Log.e("uri", uri.toString())
                                    hashMap.put("imageUrl", uri.toString())
                                    hashMap.put("comments",comments)
                                    databaseReference.child(userId).setValue(hashMap)
                                    Toast.makeText(
                                        this,
                                        "data inserted in realtime database",
                                        Toast.LENGTH_LONG
                                    ).show()

                                })
                        })

                }
            }
        }
    }
}