package com.wsr.api_checker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wsr.api_checker.databinding.ActivityMainBinding
import com.wsr.api_checker.methods.Get
import kotlinx.coroutines.runBlocking

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var methods: Get

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        methods = Get()

        _binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        binding.button.setOnClickListener {
            runBlocking {

                binding.text.text = methods.getRequest(binding.urlInput.text.toString())
            }
        }
    }
}