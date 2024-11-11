package com.example.booktracker.presentation.books

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.booktracker.databinding.FragmentBooksBinding
import com.example.booktracker.presentation.BooksViewModel
import com.example.booktracker.presentation.books.adapter.BookAdapter
import com.example.booktracker.presentation.dialogBook.DialogBookFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@AndroidEntryPoint
class BooksFragment : Fragment() {

    private val viewModel: BooksViewModel by activityViewModels()
    private var _binding: FragmentBooksBinding? = null
    private val binding get() = _binding!!

    private val adapter by lazy {
        BookAdapter().apply {
            click = { book ->
                val action = BooksFragmentDirections.actionBooksToDetail(book.id)
                findNavController().navigate(action)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBooksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()
        setupAdapter()
        observeStates()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getAllBooks()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupListeners() {
        binding.fabBook.setOnClickListener {
            showDialog()
        }

        setFragmentResultListener(DialogBookFragment.BOOK_FRAGMENT_RESULT) { requestKey: String, bundle ->
            val title = bundle.getString(DialogBookFragment.TIL_TITLE_VALUE)
            val author = bundle.getString(DialogBookFragment.TIL_AUTHOR_VALUE)
            val publicationYear = bundle.getString(DialogBookFragment.TIL_PUBLICATION_YEAR_VALUE)
            val isbn = bundle.getString(DialogBookFragment.TIL_ISBN_VALUE)


            viewModel.insertBook(
                title = title ?: "No Title",
                author = author ?: "No Author",
                publicationYear = if (publicationYear.isNullOrEmpty()) null else publicationYear.toInt(),
                isbn = if (isbn.isNullOrEmpty()) null else isbn,
                loanedTo = null,
                loanDate = null,
                returnDate = null
            )
        }
    }

    private fun setupAdapter() {
        binding.rvBooks.adapter = adapter
    }

    private fun observeStates() {
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

    private fun showDialog() {
        DialogBookFragment.show(
            dialogTitle = "New Book",
            fragmentManager = parentFragmentManager
        )
    }

    private fun <T> Flow<T>.observe(owner: LifecycleOwner, observe: (T) -> Unit) {
        owner.lifecycleScope.launch {
            owner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                this@observe.collect(observe)
            }

        }
    }
}