package com.example.selectimage

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


class MainActivity : AppCompatActivity() {
    private lateinit var imageView: ImageView
    lateinit var button: Button
    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var button2:Button
    private val PERMISSION_REQUEST_CAMERA = 0
    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
        val selectImage=registerForActivityResult(
            ActivityResultContracts.GetContent()
        ) {
            imageView.setImageURI(it)
        }
        activityResultLauncher=registerForActivityResult(ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback<ActivityResult> {
                if (it.resultCode == RESULT_OK ){
                    if(it.data != null){
                        val bundle: Bundle? = it.data!!.extras
                        val bitmap:Bitmap= bundle?.get("data") as Bitmap
                        imageView.setImageBitmap(bitmap)
                    }
                }
            })
        button.setOnClickListener {
            selectImage.launch("image/*")
        }
        button2.setOnClickListener {
            val intent:Intent= Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if(intent.resolveActivity(packageManager)==null){
                activityResultLauncher.launch(intent)
            }else{
                Log.d("camera","Intent is not work")
            }
        }
    }


    private fun initViews() {
        imageView=findViewById(R.id.imageView)
        button=findViewById(R.id.button)
        button2=findViewById(R.id.button2)
    }
    override fun onStart() {
        super.onStart()
        checkPermission(
            PERMISSION_REQUEST_CAMERA
        )
    }

    private fun checkPermission(permissionRequestCamera: Int) {
        if (ContextCompat.checkSelfPermission(this@MainActivity, Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this@MainActivity, arrayOf(Manifest.permission.CAMERA), permissionRequestCamera)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_REQUEST_CAMERA) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this@MainActivity, "Camera Permission Granted", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this@MainActivity, "Camera Permission Denied", Toast.LENGTH_SHORT).show()
            }
        }
    }
}