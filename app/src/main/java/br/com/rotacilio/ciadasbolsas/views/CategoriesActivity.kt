package br.com.rotacilio.ciadasbolsas.views

import android.app.ProgressDialog
import android.os.Bundle
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.View
import android.widget.Toast
import br.com.rotacilio.ciadasbolsas.R
import br.com.rotacilio.ciadasbolsas.adapters.CategoriesAdapter
import br.com.rotacilio.ciadasbolsas.domain.Category
import br.com.rotacilio.ciadasbolsas.fragments.CreateCategoryDialog
import br.com.rotacilio.ciadasbolsas.listeners.OnCompleteRequestListener
import br.com.rotacilio.ciadasbolsas.requests.CategoriesRequests
import kotlinx.android.synthetic.main.activity_categories.*
import kotlinx.android.synthetic.main.content_categories_list.*

class CategoriesActivity : AppCompatActivity(), View.OnClickListener {

    private var mCategories: MutableList<Category>? = null
    private var mAdapter: CategoriesAdapter? = null
    private var mProgressDialog: ProgressDialog? = null

    private val refreshListener = SwipeRefreshLayout.OnRefreshListener {
        loadData()
    }

    private val queryTextListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(p0: String?): Boolean = false
        override fun onQueryTextChange(newText: String?): Boolean {
            mAdapter?.filter(newText!!)
            return false
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_categories)

        fabNewCategory.setOnClickListener(this)
        refreshLayoutCategories.setOnRefreshListener(refreshListener)
        searchViewCategories.setOnQueryTextListener(queryTextListener)

        configureRecyclerView()
        loadData()
    }

    fun loadData() {
        showProgressLoading(R.string.loading)
        val categoriesRequests = CategoriesRequests(this)
        categoriesRequests.loadAllCategories(object : OnCompleteRequestListener<MutableList<Category>> {
            override fun success(t: MutableList<Category>) {
                mCategories = t
                mAdapter?.mCategories = mCategories!!
                finishRefreshing()
                dismissProgressLoading()
            }
            override fun fail(message: String?) {
                Toast.makeText(baseContext, message, Toast.LENGTH_SHORT).show()
                finishRefreshing()
                dismissProgressLoading()
            }
        })
    }

    private fun configureRecyclerView() {
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerViewCategories.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(recyclerViewCategories.context, layoutManager.orientation)
        recyclerViewCategories.addItemDecoration(itemDecoration)
        mAdapter = CategoriesAdapter(this)
        recyclerViewCategories.adapter = mAdapter
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.fabNewCategory -> {
                val fragmentManager = supportFragmentManager
                val dialogFragment = CreateCategoryDialog.newInstance(this)
                dialogFragment.show(fragmentManager, dialogFragment.tag)
            }
        }
    }

    private fun finishRefreshing() {
        if (refreshLayoutCategories.isRefreshing) {
            refreshLayoutCategories.isRefreshing = false
        }
    }

    fun showProgressLoading(strId: Int) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog(this)
        }
        if (!refreshLayoutCategories.isRefreshing
            && !mProgressDialog?.isShowing!!) {
            mProgressDialog!!.setMessage(getString(strId))
            mProgressDialog!!.setCancelable(false)
            mProgressDialog!!.show()
        }
    }

    fun dismissProgressLoading() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }
}