package com.example.booktracker.presentation.books.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.booktracker.presentation.books.allBooks.AllBooksFragment
import com.example.booktracker.presentation.books.onLoan.OnLoanFragment
import com.example.booktracker.presentation.books.onShelf.OnShelfFragment

class TabAdapter(fragment: Fragment): FragmentStateAdapter(fragment) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AllBooksFragment()
            1 -> OnShelfFragment()
            2 -> OnLoanFragment()
            else -> AllBooksFragment()
        }
    }

    override fun getItemCount(): Int = 3
}