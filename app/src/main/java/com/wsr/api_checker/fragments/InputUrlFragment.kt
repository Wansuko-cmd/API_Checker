package com.wsr.api_checker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.wsr.api_checker.R
import com.wsr.api_checker.adapters.SetNonUseValueAdapter
import com.wsr.api_checker.adapters.SetValueAdapter
import com.wsr.api_checker.databinding.FragmentInputUrlBinding
import com.wsr.api_checker.entities.Parameter
import com.wsr.api_checker.item_touch_helper.SetValueItemTouchHelper
import com.wsr.api_checker.methods.*
import com.wsr.api_checker.view_model.SetValueViewModel
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient

class InputUrlFragment : Fragment() {

    private var _binding: FragmentInputUrlBinding? = null
    private val binding get() = _binding!!

    private var setValueRecyclerView: RecyclerView? = null
    private var setNonUseValueRecyclerView: RecyclerView? = null

    private lateinit var setValueAdapter: SetValueAdapter
    private lateinit var setNonUseValueAdapter: SetNonUseValueAdapter

    private lateinit var setValueViewModel: SetValueViewModel

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

        //Methodを決めるためのスピナーの設定
        setSpinner()

        //パラメータのためのViewModelの設定
        setValueViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SetValueViewModel::class.java)

        //アダプターの設定
        setValueAdapter = SetValueAdapter(setValueViewModel)

        //RecyclerViewの設定
        setValueRecyclerView = binding.setValueRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = setValueAdapter
        }

        setNonUseValueAdapter = SetNonUseValueAdapter(setValueViewModel)

        setNonUseValueRecyclerView = binding.setNonUseValueRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = setNonUseValueAdapter
        }


        val setValueItemTouchHelperCallback
                = ItemTouchHelper(SetValueItemTouchHelper(setValueViewModel, setValueAdapter, setNonUseValueAdapter))

        setValueItemTouchHelperCallback.attachToRecyclerView(setValueRecyclerView)


        //Viewの設定
        binding.run{
            //双方向データバインディングをするViewModelの設定
            this.serValueViewModel = setValueViewModel

            //パラメータを追加するボタンを押したときのイベントの設定
            addParameter.setOnClickListener(addParameterSetOnClickListener)

            //リクエストを送るボタンを押したときのイベントの設定
            sendButton.setOnClickListener(sendButtonSetOnClickListener)
        }
    }



    //メソッドを指定するスピナーの設定をする関数
    private fun setSpinner(){
        ArrayAdapter
            .createFromResource(requireContext(), R.array.methods, android.R.layout.simple_spinner_item)
            .apply {
                setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                binding.methodsSpinner.adapter = this
            }
    }



    //リクエストを送信するためのボタンの設定をする関数
    private val sendButtonSetOnClickListener: (View) -> Unit = {

        //クライアントの作成
        val client = OkHttpClient.Builder().build()

        //メソッドの設定
        when(binding.methodsSpinner.selectedItem){
            "GET" -> methods = Get(client)
            "POST" -> methods = Post(client)
            "PUT" -> methods = Put(client)
            "DELETE" -> methods = Delete(client)
        }

        runBlocking {

            //リクエストを飛ばす処理
            val (isShowResult, result) = methods.getRequest(setValueViewModel)

            //結果を表示する画面へ遷移
            if(isShowResult){
                val action = InputUrlFragmentDirections
                    .actionInputUrlFragmentToShowResultFragment(result)
                findNavController().navigate(action)

            }
            //Toastを用いてエラー等を表示
            else{
                val message = when(result){
                    "UnknownHostException" -> "ホストが見つかりません"
                    "IllegalArgumentException" -> "無効なホスト、ポスト名です"
                    "ConnectException" -> "localhostとの接続に失敗しました"
                    "SSLHandshakeException" -> "接続先はSSL通信が有効ではありません"
                    else -> result
                }
                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()
            }
        }
    }



    //新しいパラメーターを追加するためのボタンの設定をする関数
    private val addParameterSetOnClickListener: (View) -> Unit = {
        val parameters = setValueViewModel.parameters
        parameters.add(Parameter())
        setValueViewModel.parameters = parameters
        setValueAdapter.notifyDataSetChanged()
    }
}