package com.j18.trailbuddy

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageButton
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.google.gson.GsonBuilder
import com.j18.dao.FetchBikeTrails
import com.j18.dao.FetchBikeTrailsResponse
import com.j18.db.MyDatabaseRepository
import com.j18.db.Trail
import com.j18.utility.RecentlyViewedAdapter
import com.j18.utility.RestApiCall


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [search_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private var TAG: String = "Search Fragment"

class search_fragment : Fragment(R.layout.fragment_search_fragment) {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        val view =  inflater.inflate(R.layout.fragment_search_fragment, container, false)
        var listView: ListView
        var adapter: RecentlyViewedAdapter
        val dpRepo = MyDatabaseRepository(this)
        val recentlyViewed :LiveData<List<Trail>> = dpRepo.fetchTrails()
        val buttonClick: ImageButton= view.findViewById(R.id.MapButton)
        buttonClick.setOnClickListener {
            val intent:Intent = Intent(activity, CurrentLocation::class.java)
            startActivity(intent)
        }
        recentlyViewed.observe(viewLifecycleOwner) { it ->

            val data: ArrayList<Trail> = it as ArrayList<Trail>
            Log.i("Count", data.size.toString())
            if (data.isNotEmpty()) {
                Log.i(TAG, "Response list received " + data[0])
                listView = view.findViewById(R.id.recently_viewed)
                adapter = this.context?.let { RecentlyViewedAdapter(it, data) }!!
                listView.adapter = adapter
                listView.onItemClickListener =
                    AdapterView.OnItemClickListener { parent, view, position, _ -> // selected item
                        val dpRepo = MyDatabaseRepository(this)
                        Log.i(TAG, "Clicked " + data[position].trailName)
                        val response: MutableLiveData<String> = RestApiCall().fetchBikeTrailsInfoWrapper(Integer.valueOf(data[position].trailId))
                        response.observe(viewLifecycleOwner) { responseString ->
                            var gson = GsonBuilder().create()
                            val response = gson.fromJson(responseString, FetchBikeTrailsResponse::class.java)
                            val data: java.util.ArrayList<FetchBikeTrails> = response.data
                            val args = Bundle()
                            var trail = FetchBikeTrails(data[0].id, data[0].name, data[0].url, data[0].length,
                            data[0].description, data[0].city, data[0].region, data[0].country,data[0].lat, data[0].lon, data[0].difficulty,
                            data[0].features,data[0].rating, data[0].thumbnail)
                            val dataJsonString: String = gson.toJson(trail)
                            args.putString("Data", dataJsonString)
                            Log.i(TAG, "Response Info received " + dataJsonString)
                            val trailsDetailsFragment = trail_details_fragment()
                            trailsDetailsFragment.arguments = args
                            val transaction = activity?.supportFragmentManager?.beginTransaction()
                            transaction?.replace(R.id.FLContainer, trailsDetailsFragment)
                            transaction?.addToBackStack(TAG)
                            transaction?.commit()
                        }

                    }
            }
        }
        return view
    }

    @SuppressLint("UseRequireInsteadOfGet")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val apiKey = getString(R.string.api_key)
        if (!Places.isInitialized()) {
            this.context?.let { Places.initialize(it, apiKey) }
        }

        val autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment?
        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment?.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG))
        Log.i(TAG,autocompleteFragment.toString())

        autocompleteFragment?.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onError(p0: Status) {
                Log.e(TAG,"Place Not Found")

            }
            override fun onPlaceSelected(p0: Place) {
                Log.i(TAG, p0.latLng?.latitude.toString())
                Log.i(TAG, p0.latLng?.longitude.toString())
                val tempFragment = list_trail_fragment()
                val args = Bundle()
                args.putString("latitude", p0.latLng?.latitude.toString())
                args.putString("longitude", p0.latLng?.longitude.toString())
                tempFragment.arguments = args

                val transaction = activity?.supportFragmentManager?.beginTransaction()
                transaction?.replace(R.id.FLContainer, tempFragment)
                transaction?.addToBackStack(TAG)
                transaction?.commit()
            }

        });
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment search_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            search_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}