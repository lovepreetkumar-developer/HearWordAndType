package com.hear_word_and_type.app.ui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import androidx.fragment.app.Fragment
import com.hear_word_and_type.app.R
import com.hear_word_and_type.app.data.preferences.PreferenceProvider
import com.hear_word_and_type.app.data.respositories.MainRepository
import com.hear_word_and_type.app.ui.activities.AuthActivity

abstract class BaseFragment<V : ViewDataBinding> : Fragment() {

    protected lateinit var binding: V
    protected var baseContext: Context? = null
    //private var mMessageDialog: BaseCustomDialog<DialogMessageBinding>? = null

    protected lateinit var mDBRepository: MainRepository

    override fun onAttach(context: Context) {
        super.onAttach(context)
        this.baseContext = context
    }

    protected open fun setBaseCallback(baseCallback: BaseCallback?) {
        binding.setVariable(BR.callback, baseCallback)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            getFragmentLayout(),
            container,
            false
        )

        mDBRepository = MainRepository(requireContext())

        onFragmentCreateView(savedInstanceState)
        return binding.root
    }


    @CallSuper
    protected open fun onFragmentCreateView(savedInstanceState: Bundle?) {
    }

    protected abstract fun getFragmentLayout(): Int

    protected open fun goBack() {
        requireActivity().onBackPressed()
        requireActivity().overridePendingTransition(
            R.anim.activity_back_in,
            R.anim.activity_back_out
        )
    }

    protected open fun goNextAnimation() {
        requireActivity().overridePendingTransition(
            R.anim.activity_in,
            R.anim.activity_out
        )
    }

    protected open fun goBackAnimation() {
        requireActivity().overridePendingTransition(
            R.anim.activity_back_in,
            R.anim.activity_back_out
        )
    }

    protected open fun goToLoginScreen(prefProvider: PreferenceProvider) {
        prefProvider.clear()
        startActivity(
            Intent(
                requireContext(),
                AuthActivity::class.java
            ).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                goNextAnimation()
            }
        )
    }

    /*protected fun showMessageDialog(message: String) {
        mMessageDialog = BaseCustomDialog(
            requireContext(),
            R.layout.dialog_message,
            object : BaseCustomDialog.DialogListener {
                override fun onViewClick(view: View?) {
                    view?.let {
                        when (it.id) {
                            R.id.tvAction -> mMessageDialog?.dismiss()
                            else -> mMessageDialog?.dismiss()
                        }
                    }
                }

            })
        Objects.requireNonNull<Window>(mMessageDialog?.window).setBackgroundDrawable(
            ColorDrawable(
                Color.TRANSPARENT
            )
        )

        mMessageDialog?.getBinding()?.tvMessage?.text = message
        mMessageDialog?.getBinding()?.tvAction?.text = getString(R.string.text_ok)
        mMessageDialog?.setCancelable(true)
        mMessageDialog?.show()
    }*/
}