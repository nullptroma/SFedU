package ru.freedominc.sfedu.ui.screens.favorite_list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import ru.freedominc.sfedu.databinding.FragmentFavoritesListBinding
import ru.freedominc.sfedu.navigation.NavigationHelper
import ru.freedominc.sfedu.ui.dialogs.FavoriteDialog
import ru.freedominc.sfedu.ui.screens.recipes_list.RecipesAdapter
import javax.inject.Inject

@AndroidEntryPoint
class FavoritesListFragment : Fragment() {
    @Inject
    lateinit var navigationHelper: NavigationHelper

    private var _binding: FragmentFavoritesListBinding? = null

    private val binding get() = _binding!!
    private val viewModel: FavoritesListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val customAdapter = RecipesAdapter(
            screenHeight = resources.displayMetrics.heightPixels,
            onClick = { recipe ->
                navigationHelper.onRecipeSelect(recipe)
            },
            onLongTap = { recipe ->
                FavoriteDialog(viewModel.isFavorite(recipe), recipe.name) {
                    viewModel.toggleFavoriteRecipe(recipe)
                }.show(this.parentFragmentManager, "FAVORITE_DIALOG")
            }
        )
        val recyclerView: RecyclerView = binding.recyclerViewFavorites
        recyclerView.layoutManager = GridLayoutManager(this.context, 2)
        recyclerView.adapter = customAdapter

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getFavoriteRecipesPackage().collect {
                    val dataset = it.data
                    customAdapter.list.submitList(dataset)
                }
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}