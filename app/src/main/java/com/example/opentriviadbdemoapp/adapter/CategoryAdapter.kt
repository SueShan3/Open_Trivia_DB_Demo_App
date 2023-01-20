package com.example.opentriviadbdemoapp.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.opentriviadbdemoapp.databinding.CategoryListBinding
import com.example.opentriviadbdemoapp.model.Question

class CategoryAdapter :
    RecyclerView.Adapter<CategoryAdapter.ViewHolder>() {

    private var categoryList: List<Question> = listOf()

    class ViewHolder(val binding: CategoryListBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val viewBinding =
            CategoryListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(viewBinding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val newList = categoryList[position]

        with(holder.binding) {
            question.text =
                HtmlCompat.fromHtml(newList.question!!, HtmlCompat.FROM_HTML_MODE_LEGACY)
            difficulty.text = newList.difficulty
            type.text = newList.type
        }
    }

    override fun getItemCount(): Int {
        return categoryList.size
    }

    fun setCategoryList(categoryList: List<Question>) {
        this.categoryList += categoryList
        notifyDataSetChanged()
    }

    fun updateChangedCategoryList(categoryList: List<Question>) {
        this.categoryList = categoryList
        notifyDataSetChanged()
    }
}