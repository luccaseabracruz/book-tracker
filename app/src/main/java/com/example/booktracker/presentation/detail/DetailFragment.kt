package com.example.booktracker.presentation.detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.booktracker.R
import com.example.booktracker.databinding.FragmentDetailBinding
import com.example.booktracker.presentation.dialogConfirmation.DialogConfirmationFragment

class DetailFragment : Fragment() {
    private val viewModel: DetailViewModel by viewModels {
        DetailViewModel.Factory()
    }
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
                            viewModel.book.value?.let { book ->
                                viewModel.deleteBook(book)
                                findNavController().popBackStack()
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

        viewModel.book.observe(viewLifecycleOwner)
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

                }
            } ?: run {
                binding.tvTitle.text = "Book not found."
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}

private fun updateTextView(textView: TextView, label: TextView, value: Any?) {
    value?.let {
        textView.text = value.toString()
        textView.isVisible = true
        label.setTextColor(label.context.getColor(R.color.black))

    } ?: run {
        Log.d("DetailFragment", "${label.text}  $value")
        textView.isVisible = false
        label.setTextColor(label.context.getColor(R.color.light_grey_invisible))
    }
}