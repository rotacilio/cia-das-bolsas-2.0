package br.com.rotacilio.ciadasbolsas.adapters

import android.content.DialogInterface
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import br.com.rotacilio.ciadasbolsas.R
import br.com.rotacilio.ciadasbolsas.commons.Utils
import br.com.rotacilio.ciadasbolsas.domain.Category
import br.com.rotacilio.ciadasbolsas.listeners.OnCompleteRequestListener
import br.com.rotacilio.ciadasbolsas.listeners.OnConfirmDialogSelectionListener
import br.com.rotacilio.ciadasbolsas.requests.CategoriesRequests
import br.com.rotacilio.ciadasbolsas.views.CategoriesActivity
import kotlinx.android.synthetic.main.categories_list_item.view.*

class CategoriesAdapter(val activity: CategoriesActivity) : RecyclerView.Adapter<CategoriesAdapter.CategoryViewHolder>() {

    var mCategories = mutableListOf<Category>()
        set(value) {
            field.clear()
            field.addAll(value)
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.categories_list_item, parent, false)
        return CategoryViewHolder(view)
    }

    override fun getItemCount(): Int = mCategories.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = mCategories[position]
        holder.categoryName.text = category.name
        holder.btnRemoveCategory.setOnClickListener {
            Utils.showConfirmDialog(holder.itemView.context, object : OnConfirmDialogSelectionListener {
                override fun yes(dialogInterface: DialogInterface, i: Int) {
                    activity.showProgressLoading(R.string.removing_category)
                    val categoriesRequests = CategoriesRequests(holder.itemView.context)
                    categoriesRequests.deleteCategory(category, object : OnCompleteRequestListener<Boolean> {
                        override fun success(t: Boolean) {
                            activity.loadData()
                        }
                        override fun fail(message: String?) {
                            Toast.makeText(holder.itemView.context, message, Toast.LENGTH_SHORT).show()
                            activity.dismissProgressLoading()
                        }
                    })
                }
                override fun no(dialogInterface: DialogInterface, i: Int) {
                    dialogInterface.dismiss()
                }
            })
        }
    }

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val categoryName = itemView.txtCategoryName!!
        val btnRemoveCategory = itemView.btnDeleteCategory!!
    }
}