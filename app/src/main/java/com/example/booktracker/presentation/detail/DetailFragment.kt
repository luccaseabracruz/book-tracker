package com.example.booktracker.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.example.booktracker.databinding.FragmentDetailBinding

class DetailFragment: Fragment() {

    private val args by navArgs<DetailFragmentArgs>()
    private var _bindind: FragmentDetailBinding? = null
    private val binding get() = _bindind!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _bindind = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvTitle.text = "${binding.tvTitle.text} - Book Id: ${args.bookId}"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _bindind = null
    }

}