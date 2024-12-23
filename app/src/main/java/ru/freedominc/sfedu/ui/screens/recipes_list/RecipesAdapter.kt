package ru.freedominc.sfedu.ui.screens.recipes_list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.freedominc.sfedu.databinding.ItemRecipeBinding
import ru.freedominc.sfedu.domain.Recipe
import kotlin.math.max


class RecipesAdapter(private val screenHeight: Int, private val onClick: (Recipe) -> Unit, private val onLongTap: (Recipe)->Unit) :
    RecyclerView.Adapter<RecipesAdapter.ViewHolder>() {
    val list = AsyncListDiffer(this, object : DiffUtil.ItemCallback<Recipe>() {
        override fun areItemsTheSame(
            oldItem: Recipe, newItem: Recipe
        ): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(
            oldItem: Recipe, newItem: Recipe
        ): Boolean {
            return oldItem == newItem
        }
    })

    class ViewHolder(private val binding: ItemRecipeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(recipe: Recipe, onClick: (Recipe) -> Unit, onLongTap: (Recipe)->Unit) {
            with(binding) {
                tvRecipeText.text = recipe.name
                cardRecipeItem.setOnClickListener {
                    onClick(recipe)
                }
                cardRecipeItem.setOnLongClickListener {
                    onLongTap(recipe)
                    return@setOnLongClickListener true
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
        viewHolder.bind(list.currentList[position], onClick, onLongTap)
    }

    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = list.currentList.size

}
