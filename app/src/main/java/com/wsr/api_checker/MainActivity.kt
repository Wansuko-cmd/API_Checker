package com.wsr.api_checker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wsr.api_checker.databinding.ActivityMainBinding
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request

class MainActivity : AppCompatActivity() {

    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityMainBinding.inflate(layoutInflater).apply {
            setContentView(this.root)
        }

        binding.button.setOnClickListener {
            runBlocking {

//                "https://i10jan-api.herokuapp.com/v1.1/api"
                binding.text.text = okHttpGet(binding.urlInput.text.toString())
            }
        }
    }

    private suspend fun okHttpGet(url: String) = withContext(Dispatchers.IO){
        val request = Request.Builder().url(url).build()
        val client = OkHttpClient.Builder().build()

        val response = client.newCall(request).execute()

        return@withContext response.body?.string() ?: ""
    }
}