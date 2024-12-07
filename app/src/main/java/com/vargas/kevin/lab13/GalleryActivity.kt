package com.vargas.kevin.lab13

import GalleryAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2

class GalleryActivity : AppCompatActivity() {

    val size: Int
        get() {
            TODO()
        }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_gallery)

        // Inicializar el ViewPager2
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)

        // Configurar el adaptador para el ViewPager2
        val adapter = GalleryAdapter(this) // Suponiendo que tienes un GalleryAdapter
        viewPager.adapter = adapter
    }

    operator fun get(position: Int) {
        TODO("Not yet implemented")
    }
}
