package com.example.booktracker.presentation.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.booktracker.databinding.FragmentBooksBinding
import com.example.booktracker.presentation.books.adapter.BookAdapter

class BooksFragment: Fragment() {

    private val viewModel: BooksViewModel by viewModels {
        BooksViewModel.Factory()
    }
    private var _binding: FragmentBooksBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        BookAdapter()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupAdapter()
        observeStates()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun setupListeners() {
        binding.fabBook.setOnClickListener {
            //@TODO go to register dialog
        }
    }

    fun setupAdapter() {
        binding.rvBooks.adapter = adapter
    }

    fun observeStates() {
        viewModel.state.observe(viewLifecycleOwner) { state ->
            when (state) {
                BookState.Empty -> {
                    binding.pbLoading.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        "Ups.. no books were found.",
                        Toast.LENGTH_LONG
                    ).show()

                }
                is BookState.Error -> {
                    binding.pbLoading.isVisible = false
                    Toast.makeText(
                        requireContext(),
                        state.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
                BookState.Loading -> {
                    binding.pbLoading.isVisible = true
                }
                is BookState.Success -> {
                    binding.pbLoading.isVisible = false
                    adapter.submitList(state.books)
                }
            }
        }
    }
}