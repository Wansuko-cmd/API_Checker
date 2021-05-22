package com.wsr.api_checker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.wsr.api_checker.R
import com.wsr.api_checker.databinding.FragmentInputUrlBinding
import com.wsr.api_checker.methods.Get
import com.wsr.api_checker.methods.HttpMethod
import com.wsr.api_checker.methods.Post
import com.wsr.api_checker.methods.Put
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient

class InputUrlFragment : Fragment() {

    private var _binding: FragmentInputUrlBinding? = null
    private val binding get() = _binding!!

    private lateinit var methods: HttpMethod

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentInputUrlBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run{

            ArrayAdapter
                .createFromResource(requireContext(), R.array.methods, android.R.layout.simple_spinner_item)
                .apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    spinner.adapter = this
                }

            button.setOnClickListener {


                val client = OkHttpClient.Builder().build()

                when(spinner.selectedItem){
                    "GET" -> methods = Get(client)
                    "POST" -> methods = Post(client)
                    "PUT" -> methods = Put(client)
                    "DELETE" -> TODO()
                }

                runBlocking {
                    val (isShowResult, result) = methods.getRequest(binding.urlInput.text.toString())

                    if(isShowResult){
                        val action = InputUrlFragmentDirections
                            .actionInputUrlFragmentToShowResultFragment(result)
                        findNavController().navigate(action)
                    }else{
                        val message = when(result){
                            "UnknownHostException" -> "ホストが見つかりません"
                            "IllegalArgumentException" -> "無効なホスト、ポスト名です"
                            "ConnectException" -> "localhostとの接続に失敗しました"
                            else -> result
                        }
                        Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}