package com.hear_word_and_type.app.ui.fragments

import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import com.hear_word_and_type.app.R
import com.hear_word_and_type.app.databinding.FragmentBookmarkedBinding
import com.hear_word_and_type.app.ui.base.BaseFragment

class BookmarkedFragment : BaseFragment<FragmentBookmarkedBinding>() {

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_bookmarked
    }

    override fun onFragmentCreateView(savedInstanceState: Bundle?) {
        super.onFragmentCreateView(savedInstanceState)
        initView()
    }

    private fun initView() {
        mDBRepository.getAllBookmarkedWords(true).observe(this) { list ->
            if (list.isNotEmpty()) {
                val builder = StringBuilder()
                for (wordModel in list) {
                    builder.append(wordModel.word)
                    builder.append("\n")
                }
                binding.tvBookmarked.text = builder.toString()
                binding.tvBookmarked.movementMethod = ScrollingMovementMethod()
            }
        }
    }
}