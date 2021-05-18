package com.wsr.api_checker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import com.wsr.api_checker.databinding.ActivityMainBinding
import com.wsr.api_checker.methods.Get
import com.wsr.api_checker.methods.HttpMethod
import com.wsr.api_checker.methods.Post
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var methods: HttpMethod

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        binding.run{

            ArrayAdapter
                .createFromResource(this@MainActivity, R.array.methods, android.R.layout.simple_spinner_item)
                .apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                spinner.adapter = this
            }

            button.setOnClickListener {

                val client = OkHttpClient.Builder().build()

                when(spinner.selectedItem){
                    "GET" -> methods = Get(client)
                    "POST" -> methods = Post(client)
                    "PUT" -> TODO()
                    "DELETE" -> TODO()
                }

                runBlocking {
                    binding.text.text = methods.getRequest(binding.urlInput.text.toString())
                }
            }
        }
    }
}
