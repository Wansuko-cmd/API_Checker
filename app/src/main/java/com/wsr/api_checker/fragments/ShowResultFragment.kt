package com.wsr.api_checker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.wsr.api_checker.databinding.FragmentShowResultBinding

class ShowResultFragment : Fragment() {

    private var _binding: FragmentShowResultBinding? = null
    private val binding get() = _binding!!

    private val args: ShowResultFragmentArgs by navArgs()
    private lateinit var result: String

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentShowResultBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        result = args.result

        binding.showResult.text = result
    }
}