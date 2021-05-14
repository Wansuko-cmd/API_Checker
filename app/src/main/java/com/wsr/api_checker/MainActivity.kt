package com.wsr.api_checker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wsr.api_checker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        binding.button.setOnClickListener {
            binding.text.text = binding.urlInput.text
        }
    }
}