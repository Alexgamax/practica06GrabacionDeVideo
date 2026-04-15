package com.example.practica06grabaciondevideo.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts.StartActivityForResult
import androidx.appcompat.app.AppCompatActivity
import com.example.practica06grabaciondevideo.R


class ShowMedia : AppCompatActivity() {
    private lateinit var imageView: ImageView
    private lateinit var videoView: VideoView
    private lateinit var btnAbrir: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_media)

        btnAbrir = findViewById(R.id.btnAbrir)
        imageView = findViewById(R.id.imageView)
        videoView = findViewById(R.id.videoView)

        btnAbrir.setOnClickListener { abrirArchivo() }
    }

    private val abrirArchivoLauncher = registerForActivityResult(
        StartActivityForResult() as ActivityResultContract<Intent?, ActivityResult?>,
        ActivityResultCallback { result: ActivityResult? ->
            if (result!!.resultCode == RESULT_OK && result.data != null) {
                val uri = result.data!!.getData()

                if (uri != null) {
                    val type = getContentResolver().getType(uri)

                    if (type != null && type.startsWith("image")) {
                        mostrarImagen(uri)
                    } else if (type != null && type.startsWith("video")) {
                        reproducirVideo(uri)
                    }
                }
            }
        })

    private fun abrirArchivo() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.setType("*/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf<String>("image/*", "video/*"))
        abrirArchivoLauncher.launch(intent)
    }

    private fun mostrarImagen(uri: Uri?) {
        imageView.setVisibility(View.VISIBLE)
        videoView.setVisibility(View.GONE)

        imageView.setImageURI(uri)
    }

    private fun reproducirVideo(uri: Uri?) {
        videoView.setVisibility(View.VISIBLE)
        imageView.setVisibility(View.GONE)

        val mediaController = MediaController(this)
        mediaController.setAnchorView(findViewById<View?>(R.id.videoContainer))
        videoView.setMediaController(mediaController)

        videoView.setVideoURI(uri);
        videoView.start();
    }
}