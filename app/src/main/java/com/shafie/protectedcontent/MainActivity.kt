package com.shafie.protectedcontent

import android.content.ContentValues
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.shafie.protectedcontent.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Insert mock data into  DB using Content Provider

        val tuple = ContentValues()
        tuple.put(Constants.TEXT, Constants.TEXT_DATA)
        contentResolver.insert(Constants.URL, tuple)


        val cols = arrayOf(Constants.ID, Constants.TEXT)
        val u = Constants.URL
        val c = contentResolver.query(u, cols, null, null, null)
        if (c!!.moveToFirst()) {
            binding.lab3TextView.text = "Data read from content provider ${c.getString(1)}"
        }


    }
}