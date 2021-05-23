package com.wsr.api_checker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsr.api_checker.R
import com.wsr.api_checker.adapters.SetValueAdapter
import com.wsr.api_checker.databinding.FragmentInputUrlBinding
import com.wsr.api_checker.methods.*
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient

class InputUrlFragment : Fragment() {

    private var _binding: FragmentInputUrlBinding? = null
    private val binding get() = _binding!!

    private var recyclerView: RecyclerView? = null
    lateinit var setValueAdapter: SetValueAdapter

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

        recyclerView = binding.setValueRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
//            adapter =
        }

        binding.run{

            ArrayAdapter
                .createFromResource(requireContext(), R.array.methods, android.R.layout.simple_spinner_item)
                .apply {
                    setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                    methodsSpinner.adapter = this
                }

            button.setOnClickListener {


                val client = OkHttpClient.Builder().build()

                when(methodsSpinner.selectedItem){
                    "GET" -> methods = Get(client)
                    "POST" -> methods = Post(client)
                    "PUT" -> methods = Put(client)
                    "DELETE" -> methods = Delete(client)
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