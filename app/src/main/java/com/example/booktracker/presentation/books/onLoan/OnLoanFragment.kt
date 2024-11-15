package com.example.booktracker.presentation.books.onLoan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.booktracker.databinding.FragmentBooksListBinding
import com.example.booktracker.presentation.BooksViewModel
import com.example.booktracker.presentation.books.BookState
import com.example.booktracker.presentation.books.BooksFragmentDirections
import com.example.booktracker.presentation.books.adapter.BookAdapter
import kotlinx.coroutines.launch

class OnLoanFragment : Fragment() {
    private val viewModel: BooksViewModel by activityViewModels()
    private var _binding: FragmentBooksListBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeStates()
        setupAdapter()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllBooks()
    }

    private val adapter by lazy {
        BookAdapter().apply {
            click = { book ->
                val action = BooksFragmentDirections.actionBooksToDetail(book.id)
                findNavController().navigate(action)
            }
        }
    }

    private fun setupAdapter() {
        binding.rvAllCarsList.adapter = adapter
    }

    private fun observeStates() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.state.collect { state ->
                when (state) {
                    BookState.Empty -> {
                        binding.pbLoading.isVisible = false
                        Toast.makeText(
                            requireContext(),
                            "Ups.. no books were found.",
                            Toast.LENGTH_LONG

                        ).show()

                        binding.tvEmptyList.isVisible = true
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
                        binding.tvEmptyList.isVisible = false
                        binding.pbLoading.isVisible = true
                    }

                    is BookState.Success -> {
                        binding.pbLoading.isVisible = false
                        val filteredBooks = state.books.filter { it.loanedTo != null }
                        adapter.submitList(filteredBooks)

                        if(filteredBooks.isEmpty()) {
                            binding.tvEmptyList.isVisible = true
                        }
                    }
                }
            }
        }
    }
}