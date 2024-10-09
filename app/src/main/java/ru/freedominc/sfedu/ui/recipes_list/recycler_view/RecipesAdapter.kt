package ru.freedominc.sfedu.ui.recipes_list.recycler_view

import android.content.Context
import android.graphics.Point
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ru.freedominc.sfedu.databinding.ItemRecipeBinding
import kotlin.math.log
import kotlin.math.max


class RecipesAdapter(private val dataSet: Array<String>, context: Context, private val onClick: (String) -> Unit) :
    RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    private val screenHeight : Int = context.resources.displayMetrics.heightPixels

    class ViewHolder(val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(text: String, onClick: (String) -> Unit) {
            with(binding) {
                tvRecipeText.text = text
                cardRecipeItem.setOnClickListener {
                    onClick(text)
                }
            }
        }
    }

    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRecipeBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        binding.root.layoutParams.height = max(binding.root.minimumHeight, screenHeight / 7)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder.bind(dataSet[position], onClick)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = dataSet.size

}
