package com.example.booktracker.presentation.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.setFragmentResultListener
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.booktracker.R
import com.example.booktracker.databinding.FragmentDetailBinding
import com.example.booktracker.presentation.BooksViewModel
import com.example.booktracker.presentation.dialogBook.DialogBookFragment
import com.example.booktracker.presentation.dialogConfirmation.DialogConfirmationFragment
import com.example.booktracker.presentation.dialogLoan.DialogLoanFragment
import com.example.booktracker.presentation.showSnackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private val viewModel: BooksViewModel by activityViewModels()
    private val args by navArgs<DetailFragmentArgs>()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupListeners()

        val toolbar = binding.toolbar
        (activity as AppCompatActivity).setSupportActionBar(toolbar)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_detail_fragment, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.i_action_delete -> {
                        val message = "Are you shure you to delete it?"
                        val onConfirm = {
                            viewModel.selectedBook.value?.let { book ->
                                viewModel.deleteBook(book)
                                findNavController().popBackStack()
                                requireView().showSnackbar("The book has been removed!")
                            }
                        }
                        DialogConfirmationFragment.showDialog(
                            message,
                            parentFragmentManager,
                            onConfirm = { onConfirm() }
                        )

                        true
                    }

                    R.id.i_action_edit -> {
                        showEditDialog()

                        true
                    }

                    else -> {
                        false
                    }
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)

        val bookId = args.bookId

        viewModel.retrieveBookById(bookId)

        viewModel.selectedBook.observe(viewLifecycleOwner)
        { book ->

            book?.let {

                binding.tvTitle.text = book.title
                binding.tvAuthor.text = book.author
                binding.tvPublicationYear.text = book.publicationYear.toString()
                updateTextView(
                    binding.tvPublicationYear,
                    binding.tvPublicationYearLabel,
                    book.publicationYear
                )
                updateTextView(binding.tvIsbn, binding.tvIsbnLabel, book.isbn)
                if (book.loanedTo.isNullOrEmpty()) {
                    binding.tvAvailability.text = "On Shelf"
                    binding.clLoanInformation.isVisible = false
                    binding.btnRegisterLoan.isVisible = true

                    binding.ivAvailability.setColorFilter(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.green
                        ),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )
                } else {
                    binding.tvAvailability.text = "On Loan"
                    binding.clLoanInformation.isVisible = true
                    binding.btnRegisterLoan.isVisible = false

                    binding.ivAvailability.setColorFilter(
                        ContextCompat.getColor(
                            binding.root.context,
                            R.color.red
                        ),
                        android.graphics.PorterDuff.Mode.SRC_IN
                    )

                    updateTextView(
                        binding.tvLoanedTo,
                        binding.tvLoanedToLabel,
                        book.loanedTo
                    )
                    updateTextView(
                        binding.tvLoanDate,
                        binding.tvLoanDateLabel,
                        book.loanDate
                    )
                    updateTextView(
                        binding.tvReturnDate,
                        binding.tvReturnDateLabel,
                        book.returnDate
                    )
                }

            } ?: run {
                binding.tvTitle.text = "Book not found."
            }
        }
    }

    private fun setupListeners() {


        binding.btnRegisterLoan.setOnClickListener {
            showRegisterLoanDialog()
        }

        binding.btnReturnLoan.setOnClickListener {
            viewModel.selectedBook.value?.let { book ->
                val updatedBook = book.copy(
                    loanedTo = null,
                    loanDate = null,
                    returnDate = null
                )

                viewModel.updateBook(updatedBook)
                requireView().showSnackbar("Book return recorded successfully!")
            }
        }

        setFragmentResultListener(DialogLoanFragment.LOAN_FRAGMENT_RESULT) { requestKey, bundle ->
            val loanedTo = bundle.getString(DialogLoanFragment.TIL_LOANED_TO)
            val loanDate = bundle.getString(DialogLoanFragment.LOAN_DATE)
            val returnDate = bundle.getString(DialogLoanFragment.TIL_RETURN_DATE)

            viewModel.selectedBook.value?.let { book ->
                val updatedBook = book.copy(
                    loanedTo = loanedTo,
                    loanDate = loanDate,
                    returnDate = returnDate
                )

                viewModel.updateBook(updatedBook)
                requireView().showSnackbar("Loan registered successfully!")
            }
        }

        setFragmentResultListener(DialogBookFragment.BOOK_FRAGMENT_RESULT) { requestKey, bundle ->
            val title = bundle.getString(DialogBookFragment.TIL_TITLE_VALUE)
            val author = bundle.getString(DialogBookFragment.TIL_AUTHOR_VALUE)
            val publicationYear = bundle.getString(DialogBookFragment.TIL_PUBLICATION_YEAR_VALUE)
            val isbn = bundle.getString(DialogBookFragment.TIL_ISBN_VALUE)

            viewModel.selectedBook.value?.let { book ->
                val updatedBook = book.copy(
                    title = title ?: book.title,
                    author = author ?: book.author,
                    publicationYear = if (publicationYear.isNullOrEmpty()) null else publicationYear.toInt(),
                    isbn = if (isbn.isNullOrEmpty()) null else isbn

                )

                viewModel.updateBook(updatedBook)
                requireView().showSnackbar("Book updated successfully!")
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun showEditDialog() {
        DialogBookFragment.show(
            dialogTitle = "Edit Book",
            fragmentManager = parentFragmentManager,
            book = viewModel.selectedBook.value
        )
    }

    private fun showRegisterLoanDialog() {
        DialogLoanFragment.show(
            dialogTitle = "Loan Register",
            fragmentManager = parentFragmentManager,
            book = viewModel.selectedBook.value
        )
    }

}

private fun updateTextView(textView: TextView, label: TextView, value: Any?) {
    value?.let {
        textView.text = value.toString()
        textView.isVisible = true
        label.setTextColor(label.context.getColor(R.color.black))

    } ?: run {
        textView.isVisible = false
        label.setTextColor(label.context.getColor(R.color.light_grey_invisible))
    }
}

