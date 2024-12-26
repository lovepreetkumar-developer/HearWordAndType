package com.hear_word_and_type.app.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.hear_word_and_type.app.R
import com.hear_word_and_type.app.data.db.entities.WordModel
import com.hear_word_and_type.app.data.preferences.PreferenceProvider
import com.hear_word_and_type.app.databinding.DialogGoToWordBinding
import com.hear_word_and_type.app.databinding.FragmentSplashBinding
import com.hear_word_and_type.app.ui.base.BaseCallback
import com.hear_word_and_type.app.ui.base.BaseCustomDialog
import com.hear_word_and_type.app.ui.base.BaseFragment
import com.hear_word_and_type.app.ui.custom_views.circle_progress.CustomProgress
import com.hear_word_and_type.app.util.loadFileFromAsset
import com.hear_word_and_type.app.util.showToast
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import timber.log.Timber
import java.util.Locale

class SplashFragment : BaseFragment<FragmentSplashBinding>(), KodeinAware {

    override val kodein by kodein()

    private val mProgressView: CustomProgress by instance()
    private val mPrefProvider: PreferenceProvider by instance()

    private lateinit var mTextToSpeech: TextToSpeech
    private lateinit var mGoToWordDialog: BaseCustomDialog<DialogGoToWordBinding>
    private lateinit var mCurrentWordModel: WordModel

    private var mTotalWords = 0

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_splash
    }

    override fun onFragmentCreateView(savedInstanceState: Bundle?) {
        super.onFragmentCreateView(savedInstanceState)
        initView()
    }

    private fun initView() {
        setBaseCallback(baseCallback)
        activity?.let { activity ->
            mDBRepository.getAllWords().observe(this) { list ->
                if (list.isNotEmpty()) {
                    mTotalWords = list.size
                    mProgressView.hide()
                    getWord(mPrefProvider.getCurrentWordIndex())
                } else {
                    mProgressView.show(activity)
                    val words = loadFileFromAsset(activity, "words.md")
                    words?.let {
                        val listOfFinalWords = mutableListOf<WordModel>()
                        for (wordText in words.split("\n").withIndex()) {
                            val wordModel = WordModel(wordText.index, false, wordText.value)
                            listOfFinalWords.add(wordModel)
                        }
                        mDBRepository.insertWords(listOfFinalWords)
                    }
                }
            }
        }

        binding.switchShowWord.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) binding.tvActualWord.visibility = View.VISIBLE
            else binding.tvActualWord.visibility = View.GONE
        }

        mTextToSpeech = TextToSpeech(requireContext()) { status ->
            if (status == TextToSpeech.SUCCESS) {
                val ttsLang = mTextToSpeech.setLanguage(Locale.US)
                if (ttsLang == TextToSpeech.LANG_MISSING_DATA
                    || ttsLang == TextToSpeech.LANG_NOT_SUPPORTED
                ) {
                    Timber.e("TTS The Language is not supported!")
                } else {
                    Timber.i("TTS Language Supported.")
                }
                Timber.i("TTS Initialization success.")
            } else {
                Toast.makeText(context, "TTS Initialization failed!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.etWord.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                s?.let {
                    if (it.isNotEmpty() && it.toString() == binding.tvActualWord.text.toString()) {
                        binding.imgStatus.setImageResource(R.drawable.ic_green_tick)
                    } else {
                        binding.imgStatus.setImageResource(R.drawable.ic_criss_cross)
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        mTextToSpeech.stop()
        mTextToSpeech.shutdown()
    }

    private fun getWord(index: Int) {
        mDBRepository.getWord(index)
            .observe(this) { wordModel ->
                setWord(wordModel)
            }
    }

    @SuppressLint("SetTextI18n")
    private fun setWord(wordModel: WordModel) {
        mCurrentWordModel = wordModel
        binding.etWord.setText("")
        binding.tvActualWord.text = wordModel.word.replace("\r", "")
        binding.tvCurrentWord.text = "${wordModel.index + 1}/${mTotalWords}"
        mPrefProvider.setCurrentWordIndex(wordModel.index)
        binding.etWord.isFocusableInTouchMode = true
        binding.etWord.requestFocus()
        /*binding.etWord.post {
            if (binding.etWord.requestFocus()) {
                val imm =
                    binding.etWord.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
                imm?.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
            }
        }*/

        if (wordModel.bookmarked) {
            binding.imgBookmark.setImageResource(R.drawable.ic_bookmark_blue)
            binding.imgBookmark.tag = true
        } else {
            binding.imgBookmark.tag = false
            binding.imgBookmark.setImageResource(R.drawable.ic_bookmark_white)
        }
        speakWord()
    }

    private val baseCallback = BaseCallback { view ->
        when (view.id) {
            R.id.img_speaker -> speakWord()
            R.id.fab_previous -> {
                if (mPrefProvider.getCurrentWordIndex() != 0) {
                    getWord(mPrefProvider.getCurrentWordIndex() + -1)
                }
            }
            R.id.fab_next -> getWord(mPrefProvider.getCurrentWordIndex() + 1)
            R.id.tv_current_word -> showGoToWordDialog()
            R.id.img_bookmark -> bookMarkWord()
            R.id.fab_bookmarked -> findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToBookmarkedFragment())
        }
    }

    private fun bookMarkWord() {
        if (binding.imgBookmark.tag == true) {
            mDBRepository.updateBookmark(false, mCurrentWordModel.index)
            requireContext().showToast("Bookmarked Removed")
        } else {
            mDBRepository.updateBookmark(true, mCurrentWordModel.index)
            requireContext().showToast("Bookmarked Successfully")
        }
    }

    private fun speakWord() {
        try {
            mTextToSpeech.speak(
                binding.tvActualWord.text.toString(),
                TextToSpeech.QUEUE_FLUSH,
                null,
                null
            )
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    private fun showGoToWordDialog() {
        mGoToWordDialog = BaseCustomDialog(requireActivity(),
            R.layout.dialog_go_to_word,
            object : BaseCustomDialog.DialogListener {
                override fun onViewClick(view: View?) {
                    val numberText = mGoToWordDialog.getBinding().etNumber.text.toString()
                    if (numberText.isNotEmpty()) {
                        if (numberText.toInt() in 1..mTotalWords) {
                            getWord(numberText.toInt() - 1)
                            mGoToWordDialog.dismiss()
                        } else {
                            requireContext().showToast("Word not available")
                        }
                    }
                }
            })

        mGoToWordDialog.window?.setBackgroundDrawable(
            android.graphics.drawable.ColorDrawable(
                android.graphics.Color.TRANSPARENT
            )
        )
        mGoToWordDialog.setCancelable(true)
        mGoToWordDialog.getBinding().etNumber.isFocusableInTouchMode = true
        mGoToWordDialog.getBinding().etNumber.requestFocus()
        mGoToWordDialog.show()
    }
}