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
import com.google.android.material.snackbar.Snackbar
import com.wsr.api_checker.R
import com.wsr.api_checker.adapters.SetNonUseValueAdapter
import com.wsr.api_checker.adapters.SetValueAdapter
import com.wsr.api_checker.databinding.FragmentInputUrlBinding
import com.wsr.api_checker.entities.Parameter
import com.wsr.api_checker.item_touch_helper.SetNonUseValueItemTouchHelper
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

    //UNDOを表示するためのsnackBar
    private lateinit var snackBar: Snackbar

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


        snackBar = setSnackBar()

        //パラメータのためのViewModelの設定
        setValueViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(SetValueViewModel::class.java)

        //有効パラメーター用アダプターの設定
        setValueAdapter = SetValueAdapter(setValueViewModel)

        //有効パラメーター用RecyclerViewの設定
        setValueRecyclerView = binding.setValueRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = setValueAdapter
        }

        //無効パラメーター用アダプターの設定
        setNonUseValueAdapter = SetNonUseValueAdapter(setValueViewModel)

        //無効パラメーター用RecyclerViewの設定
        setNonUseValueRecyclerView = binding.setNonUseValueRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = setNonUseValueAdapter
        }


        val setValueItemTouchHelperCallback = ItemTouchHelper(SetValueItemTouchHelper(
            setValueViewModel,
            setValueAdapter,
            setNonUseValueAdapter,
            snackBar
        ))

        setValueItemTouchHelperCallback.attachToRecyclerView(setValueRecyclerView)

        val setNonUseValueItemTouchHelperCallback = ItemTouchHelper(SetNonUseValueItemTouchHelper(
            setValueViewModel,
            setValueAdapter,
            setNonUseValueAdapter,
            snackBar
        ))

        setNonUseValueItemTouchHelperCallback.attachToRecyclerView(setNonUseValueRecyclerView)


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
        val methods: HttpMethod = when(binding.methodsSpinner.selectedItem){
            "GET" -> Get(client)
            "POST" -> Post(client)
            "PUT" -> Put(client)
            "DELETE" -> Delete(client)
            else -> throw Exception()
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



    //Undo機能の設定
    private fun setSnackBar(): Snackbar {

        //snackBarの設定
        return Snackbar.make(
            binding.showSnackBarLayout,
            getString(R.string.snack_bar_message),
            Snackbar.LENGTH_LONG
        )
            .setAction(getString(R.string.snack_bar_action)) {

                setValueViewModel.deleteValue?.let{ value ->

                    if(value.first == "parameters"){
                        val parameters = setValueViewModel.parameters
                        parameters.add(value.second)
                        setValueViewModel.parameters = parameters
                        setValueAdapter.notifyItemInserted(parameters.size)

                    }else{
                        val nonUseParameters = setValueViewModel.nonUseParameters
                        nonUseParameters.add(value.second)
                        setValueViewModel.nonUseParameters = nonUseParameters
                        setNonUseValueAdapter.notifyItemInserted(nonUseParameters.size)
                    }

                }

                //無事に要素を戻したことを伝えるsnackBarの設定
                Snackbar.make(
                    binding.showSnackBarLayout,
                    getString(R.string.snack_bar_after),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
    }



    //新しいパラメーターを追加するためのボタンの設定をする関数
    private val addParameterSetOnClickListener: (View) -> Unit = {
        val parameters = setValueViewModel.parameters
        parameters.add(Parameter())
        setValueViewModel.parameters = parameters
        setValueAdapter.notifyItemInserted(parameters.size)
    }
}