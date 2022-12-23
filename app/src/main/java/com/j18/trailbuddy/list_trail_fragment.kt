package com.j18.trailbuddy

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView.OnItemClickListener
import android.widget.LinearLayout
import android.widget.ListView
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.GsonBuilder
import com.j18.dao.FetchBikeTrails
import com.j18.dao.FetchBikeTrailsResponse
import com.j18.db.MyDatabaseRepository
import com.j18.db.Trail
import com.j18.utility.ListViewAdapter
import com.j18.utility.RestApiCall
import java.util.*


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [list_trail_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private var TAG: String = "List Trail Fragment"

class list_trail_fragment :  Fragment(R.layout.fragment_list_trail_fragment){
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
        val view = inflater.inflate(R.layout.fragment_list_trail_fragment, container, false)
        val bundle = arguments
        val longitude = bundle?.getString("longitude")
        val latitude = bundle?.getString("latitude")
        Log.i(TAG, "longitude $longitude")
        Log.i(TAG, "latitude $latitude")
        var listView: RecyclerView
        var adapter: ListViewAdapter

        val restCall: MutableLiveData<String>? =
            latitude?.toDouble()
                ?.let { longitude?.toDouble()
                    ?.let { it1 -> RestApiCall().fetchBikeTrailsWrapper(it, it1) } };

        restCall?.observe(viewLifecycleOwner) { responseString ->
            var gson = GsonBuilder().create()
            val response = gson.fromJson(responseString, FetchBikeTrailsResponse::class.java)
            val data: ArrayList<FetchBikeTrails> = response.data
            if(data.size>0) {
                Log.i(TAG, "Response list received "+ data[0])
                listView = view.findViewById(R.id.recyclerView)
                listView.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = ListViewAdapter( onClickListener = { view, trail -> openActivity(view, trail) },data)
                listView.adapter = adapter

            }
        }

        return view
    }

    private fun openActivity(view: View, fetchBikeTrails: FetchBikeTrails){
        Log.i(TAG,"Clicked " +fetchBikeTrails.id.toString())
        val dpRepo = MyDatabaseRepository(this)
        val trail =  Trail(UUID.randomUUID().toString(),fetchBikeTrails.name,fetchBikeTrails.id,fetchBikeTrails.description,System.currentTimeMillis())
        dpRepo.addTrail(trail)
        fetchBikeTrails.name?.let { Log.i("Obj", it) }
        val args = Bundle()
        var gson = GsonBuilder().create()
        val dataJsonString: String = gson.toJson(fetchBikeTrails)
        args.putString("Data", dataJsonString)
        val trailsDetailsFragment = trail_details_fragment()
        trailsDetailsFragment.arguments = args
        val transaction = activity?.supportFragmentManager?.beginTransaction()
        transaction?.replace(R.id.FLContainer, trailsDetailsFragment)
        transaction?.addToBackStack(TAG)
        transaction?.commit()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment list_trail_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            list_trail_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}

