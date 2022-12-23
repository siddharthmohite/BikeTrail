package com.j18.trailbuddy

import android.content.Intent
import android.graphics.Typeface
import android.net.Uri
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.*
import android.graphics.pdf.PdfDocument
import android.media.Rating
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.*
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.View.OnTouchListener
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.google.gson.GsonBuilder
import com.j18.dao.FetchBikeTrails
import com.squareup.picasso.Picasso
import java.io.File
import java.io.FileOutputStream


// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [trail_details_fragment.newInstance] factory method to
 * create an instance of this fragment.
 */

private const val TAG = "List Details"

class trail_details_fragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var pageHeight = 1120
    var pageWidth = 792
    lateinit var bmp: Bitmap
    lateinit var scaledbmp: Bitmap
    var PERMISSION_CODE = 101


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bmp = BitmapFactory.decodeResource(resources, R.drawable.cycle)
        scaledbmp = Bitmap.createScaledBitmap(bmp, 140, 140, false)

        if (checkPermissions()) {
            Toast.makeText(context, "Permissions Granted..", Toast.LENGTH_SHORT).show()
        } else {
            requestPermission()
        }


        val view = inflater.inflate(R.layout.fragment_trail_details_fragment, container, false)
        val trailName = view.findViewById<TextView>(R.id.trailName)
        val distance = view.findViewById<TextView>(R.id.distance)
        val rating = view.findViewById<RatingBar>(R.id.ratingDetails)
        val difficulty = view.findViewById<TextView>(R.id.difficulty)
        val description = view.findViewById<TextView>(R.id.description)
        var imageView = view.findViewById<ImageView>(R.id.imageView)
        val args = arguments
        val dataJsonString = args!!.getString("Data")
        val gson = GsonBuilder().create()
        val traildata: FetchBikeTrails = gson.fromJson(dataJsonString, FetchBikeTrails::class.java)
        trailName.text = traildata.name
        trailName.setTypeface(Typeface.DEFAULT_BOLD);
        distance.text = "Trail Distance is " + traildata.length +"mi"
        distance.setTypeface(Typeface.DEFAULT_BOLD);
        rating.rating = traildata.rating?.toFloat()!!
        difficulty.text = "Trail Difficulty is " + traildata.difficulty
        difficulty.setTypeface(Typeface.DEFAULT_BOLD);
        description.text = traildata.description
        Log.i(TAG,traildata.thumbnail.toString())

        Picasso.get().load(traildata.thumbnail.toString()).into(imageView);
        Log.i(TAG, traildata.toString())
        val gesture = GestureDetector(
            activity,
            object : SimpleOnGestureListener() {
                override fun onDown(e: MotionEvent): Boolean {

                    return true
                }

                override fun onDoubleTap(e: MotionEvent): Boolean {
                    Log.i(TAG,"Double Tapped")
                    generatePDF(traildata)
                    return true
                }
                override fun onFling(
                    e1: MotionEvent, e2: MotionEvent, velocityX: Float,
                    velocityY: Float
                ): Boolean {
                    Log.i(TAG, "onFling has been called!")
                    val SWIPE_MIN_DISTANCE = 120
                    val SWIPE_MAX_OFF_PATH = 250
                    val SWIPE_THRESHOLD_VELOCITY = 200
                    try {
                        if (Math.abs(e1.y - e2.y) > SWIPE_MAX_OFF_PATH) return false
                        if (e1.x - e2.x > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
                        ) {
                            Log.i(TAG, "Right to Left")
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("geo:"+traildata.lat+","+traildata.lon+"?q="+traildata.name))
                            intent.setPackage("com.google.android.apps.maps")
                            context!!.startActivity(intent)
                        } else if (e2.x - e1.x > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY
                        ) {
                            Log.i(TAG, "Left to Right")
                        }
                    } catch (e: Exception) {
                        // nothing
                    }
                    return true
                }
            })

        view.setOnTouchListener(OnTouchListener { v, event -> gesture.onTouchEvent(event) })
        return view
    }

    fun generatePDF(trailData:FetchBikeTrails?) {
        var pdfDocument = PdfDocument()
        var paint: Paint = Paint()
        var title: Paint = Paint()
        var myPageInfo: PdfDocument.PageInfo? =
            PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
        var myPage: PdfDocument.Page = pdfDocument.startPage(myPageInfo)
        var canvas: Canvas = myPage.canvas
        title.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL))
        title.textSize = 15F
        context?.let { ContextCompat.getColor(it, R.color.purple_200) }?.let { title.setColor(it) }
        canvas.drawText("Bike Trail Info", 50F, 50F, title)
        trailData?.name?.let { canvas.drawText(it, 50F, 100F, title) }
        canvas.drawText("Trail distance is "+trailData?.length + "mi", 50F, 150F, title)
        trailData?.description?.let { canvas.drawText(it, 50F, 250F, title) }
        canvas.drawText("Trail difficulty is "+trailData?.difficulty , 50F, 200F, title)

        title.setTypeface(Typeface.defaultFromStyle(Typeface.NORMAL))
        context?.let { ContextCompat.getColor(it, R.color.purple_200) }?.let { title.setColor(it) }
        title.textSize = 15F
        title.textAlign = Paint.Align.CENTER
        pdfDocument.finishPage(myPage)
        val file: File = File(Environment.getExternalStorageDirectory(), trailData?.name+".pdf")
        try {
            pdfDocument.writeTo(FileOutputStream(file))
            Toast.makeText(context, "PDF file generated..", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "Fail to generate PDF file..", Toast.LENGTH_SHORT)
                .show()
        }
        pdfDocument.close()
    }

    fun checkPermissions(): Boolean {
        var writeStoragePermission = context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            )
        }

        var readStoragePermission = context?.let {
            ContextCompat.checkSelfPermission(
                it,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }

        return writeStoragePermission == PackageManager.PERMISSION_GRANTED
                && readStoragePermission == PackageManager.PERMISSION_GRANTED
    }
    fun requestPermission() {

        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), PERMISSION_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.size > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1]
                    == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(context, "Permission Granted..", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "Permission Denied..", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment trail_details_fragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            trail_details_fragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

}