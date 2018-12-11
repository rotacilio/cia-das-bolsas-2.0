package br.com.rotacilio.ciadasbolsas.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.rotacilio.ciadasbolsas.R
import br.com.rotacilio.ciadasbolsas.domain.Category
import br.com.rotacilio.ciadasbolsas.listeners.OnCompleteRequestListener
import br.com.rotacilio.ciadasbolsas.requests.CategoriesRequests
import br.com.rotacilio.ciadasbolsas.views.CategoriesActivity
import kotlinx.android.synthetic.main.fragment_new_category.*
import kotlinx.android.synthetic.main.fragment_new_category.view.*

@SuppressLint("ValidFragment")
class UpdateCategoryDialog(val activity: CategoriesActivity) : DialogFragment(), View.OnClickListener {

    private var mCategory: Category? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
            = inflater.inflate(R.layout.fragment_new_category, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mCategory = arguments?.getSerializable("category_obj") as Category
        edtCategoryName.setText(mCategory?.name)

        view.btnCancel.setOnClickListener(this)
        view.btnSave.setOnClickListener(this)
    }

    override fun onResume() {
        super.onResume()
        dialog.setCancelable(false)
        dialog.setCanceledOnTouchOutside(false)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnCancel -> dismiss()
            R.id.btnSave -> {
                if (isValidData()) {
                    dismiss()
                    activity.showProgressLoading(R.string.saving)
                    mCategory?.name = edtCategoryName.text.toString()
                    val categoriesRequests = CategoriesRequests(context!!)
                    categoriesRequests.updateCategory(mCategory!!, object : OnCompleteRequestListener<Category> {
                        override fun success(t: Category) {
                            activity.loadData()
                        }
                        override fun fail(message: String?) {
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            activity.dismissProgressLoading()
                        }
                    })
                }
            }
        }
    }

    private fun isValidData(): Boolean {
        if (edtCategoryName.text.toString().isBlank()) {
            edtCategoryName.error = context?.getString(R.string.error_message_empty_category_name)
            edtCategoryName.requestFocus()
            return false
        }
        return true
    }

    companion object {
        fun newInstance(activity: CategoriesActivity, category: Category): UpdateCategoryDialog {
            val dialog = UpdateCategoryDialog(activity)
            val args = Bundle()
            args.putSerializable("category_obj", category)
            dialog.arguments = args
            return dialog
        }
    }
}