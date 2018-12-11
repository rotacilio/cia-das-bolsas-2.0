package br.com.rotacilio.ciadasbolsas.requests

import android.content.Context
import android.util.Log
import br.com.rotacilio.ciadasbolsas.R
import br.com.rotacilio.ciadasbolsas.domain.Category
import br.com.rotacilio.ciadasbolsas.listeners.OnCompleteRequestListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CategoriesRequests(val context: Context) {

    fun createNewCategory(category: Category, listener: OnCompleteRequestListener<Category>) {
        val dbReference = FirebaseDatabase.getInstance().reference
        val key = dbReference.child("/categories").push().key
        category.key = key!!
        dbReference.child("/categories/$key")
            .setValue(category)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener.success(category)
                } else {
                    listener.fail(it.exception?.message)
                }
            }
    }

    fun updateCategory(category: Category, listener: OnCompleteRequestListener<Category>) {
        val dbReference = FirebaseDatabase.getInstance().reference
        dbReference.child("/categories/${category.key}")
            .setValue(category)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    listener.success(category)
                } else {
                    listener.fail(it.exception?.message)
                }
            }
    }

    fun loadAllCategories(listener: OnCompleteRequestListener<MutableList<Category>>) {
        val dbReference = FirebaseDatabase.getInstance().reference
        dbReference.child("/categories")
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dbError: DatabaseError) {
                    listener.fail(dbError.message)
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val categories = mutableListOf<Category>()
                    if (dataSnapshot.childrenCount > 0) {
                        for (dtSnapshot in dataSnapshot.children) {
                            val currentCategory = dtSnapshot.getValue(Category::class.java)
                            categories.add(currentCategory!!)
                        }
                    }
                    listener.success(categories)
                }
        })
    }

    fun deleteCategory(category: Category, listener: OnCompleteRequestListener<Boolean>) {
        val dbReference = FirebaseDatabase.getInstance().reference
        dbReference.child("/products")
            .orderByChild("category/key")
            .equalTo(category.key)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onCancelled(dbError: DatabaseError) {
                    listener.fail(dbError.message)
                }
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.hasChildren()) {
                        listener.fail(context.getString(R.string.error_message_category_has_children))
                    } else {
                        dbReference.child("/categories/${category.key}")
                            .removeValue()
                            .addOnCompleteListener {
                                if (it.exception != null) {
                                    listener.fail(it.exception?.message)
                                } else {
                                    listener.success(it.isSuccessful)
                                }
                            }
                    }
                }
        })
    }
}