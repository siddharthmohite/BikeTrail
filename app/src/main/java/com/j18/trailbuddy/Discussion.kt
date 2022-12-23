package com.j18.trailbuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*

class Discussion : AppCompatActivity() {
    private lateinit var dbRef: DatabaseReference
    private lateinit var add:FloatingActionButton
    private lateinit var disRv: RecyclerView
    //private lateinit var image: ImageView
    private lateinit var Loading: TextView
    private lateinit var disList: ArrayList<DiscussionModel>
    private val TAG = "Discussion"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_discussion)
        val sharedPreferences = getSharedPreferences("myPref", MODE_PRIVATE)
        val userName = sharedPreferences.getString("displayName",null)
        val email = sharedPreferences.getString("email",null)
        Log.i(TAG,"Username "+userName)
        Log.i(TAG,"Email "+email)
        dbRef= FirebaseDatabase.getInstance().getReference("Image")
        add=this.findViewById(R.id.add)
        disRv=this.findViewById(R.id.rvPosts)
        // image=this.findViewById(R.id.image)
        disRv.layoutManager= LinearLayoutManager(this)
        Loading=this.findViewById(R.id.Loading)
        disList= arrayListOf<DiscussionModel>()


        add.setOnClickListener{
            val intent= Intent(this,AddPosts::class.java)
            startActivity(intent)
        }


        getPostsData()
    }

    private fun getPostsData(){
        disRv.visibility= View.VISIBLE
        Loading.visibility= View.GONE
        Log.e("if fucntion is called","called")

        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                Log.e("on data change","entered")
                disList.clear()
                if(snapshot.exists()){
                    for(disSnap in snapshot.children){
                        val disData= disSnap.getValue(DiscussionModel::class.java)

                        disList.add(disData!!)
                        Log.e("data",disList.toString())
                    }

                    val mAdapter= DisAdapter(disList)
                    disRv.adapter=mAdapter
                    disRv.visibility= View.VISIBLE
                    Loading.visibility= View.GONE

                }
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
    }

