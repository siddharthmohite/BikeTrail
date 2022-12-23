package com.j18.trailbuddy

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity


class MainActivity : AppCompatActivity() {

    private var TAG: String = "Main Activity"
    private lateinit var community:Button
    private lateinit var displayname: TextView



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        community=this.findViewById(R.id.community)
        displayname=this.findViewById(R.id.displayName)
        val sharedPreferences: SharedPreferences =
            getSharedPreferences("myPref", MODE_PRIVATE)
        val displayName: String? =sharedPreferences.getString("displayName","")

        val stremail: String = intent.getStringExtra("email").toString()
        val nv1: String="null"
        if(stremail.toString()!=nv1){
            community.setOnClickListener{
                val intent=Intent(this,Discussion::class.java)
                startActivity(intent)
            }
        }
        else{
            community.setOnClickListener{
                Toast.makeText(this,"Please sign in or sign up to view community posts",Toast.LENGTH_LONG).show()

            }
        }

        displayname.text=displayName.toString()
        val nv:String=""
        if(displayName.toString()!=nv){
            displayname.text=displayName.toString()
        }
        else{
            displayname.text="Guest User"
        }


        if(displayName.toString()!=nv){
            community.setOnClickListener{
                val intent=Intent(this,Discussion::class.java)
                startActivity(intent)
            }
        }else{
            community.setOnClickListener{
                Toast.makeText(this,"Please sign in to access community posts ",Toast.LENGTH_SHORT).show()

            }

        }







        val searchFragment = search_fragment()

        val bundle = intent.extras
//        val apiKey = getString(R.string.api_key)
//
//        /**
//         * Initialize Places. For simplicity, the API key is hard-coded. In a production
//         * environment we recommend using a secure mechanism to manage API keys.
//         */
//        /**
//         * Initialize Places. For simplicity, the API key is hard-coded. In a production
//         * environment we recommend using a secure mechanism to manage API keys.
//         */
//        if (!Places.isInitialized()) {
//            Places.initialize(applicationContext, apiKey)
//        }
//
//// Create a new Places client instance.
//
//// Create a new Places client instance.
//        val placesClient = Places.createClient(this)
//        val searchFragment = search_fragment()
//        val autocompleteFragment =
//            supportFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
//
//
//
//        // Specify the types of place data to return.
////        autocompleteFragment?.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME))
////
////        // Set up a PlaceSelectionListener to handle the response.
////        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
////            override fun onPlaceSelected(place: Place) {
////                //
////
////
////                Log.i(TAG, "Place: ${place.name}, ${place.id}, ${place.latLng?.latitude}")
////            }
////
////            override fun onError(status: Status) {
////
////                Log.i(TAG, "An error occurred: $status")
////            }
////        })
//
//
//        // Specify the types of place data to return.
//        autocompleteFragment?.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
//
        //Sample code for api calls
        /*   val restCall: MutableLiveData<String> = RestApiCall().fetchBikeTrailsWrapper(33.870350,-117.924301);
           restCall.observe(this) { responseString ->
               Log.d(TAG, "Response list received $responseString")
           }

           val response: MutableLiveData<String> = RestApiCall().fetchBikeTrailsInfoWrapper(285239)
           response.observe(this) { responseString ->
               Log.d(TAG, "Response Info received $responseString")
           }

           val restResponse: MutableLiveData<String> = RestApiCall().fetchBikeTrailsMapListWrapper(285239)
           response.observe(this) { responseString ->
               Log.d(TAG, "Response Map List received $responseString")
           }*/

        val apiKey = getString(R.string.api_key)
        if(bundle?.containsKey("callingActivity") == true && bundle?.getString("callingActivity") =="CurrentLocation")
        {
            Log.i(TAG,"hereeeee")
            val args = Bundle()
            args.putString("latitude", bundle.getString("lat"))
            args.putString("longitude",bundle.getString("long"))
            val listTrailFrag = list_trail_fragment()

            listTrailFrag.arguments = args

            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.FLContainer, listTrailFrag)
            transaction.commit()
        }
        else{
            supportFragmentManager.beginTransaction().replace(R.id.FLContainer,searchFragment).commit()

        }


    }
}