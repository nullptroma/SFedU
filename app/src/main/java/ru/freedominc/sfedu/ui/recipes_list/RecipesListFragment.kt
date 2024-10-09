package ru.freedominc.sfedu.ui.recipes_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import dagger.hilt.android.AndroidEntryPoint
import ru.freedominc.sfedu.R
import ru.freedominc.sfedu.databinding.FragmentRecipesListBinding
import ru.freedominc.sfedu.navigation.NavigationHelper
import ru.freedominc.sfedu.ui.recipes_list.recycler_view.RecipesAdapter
import javax.inject.Inject

@AndroidEntryPoint
class RecipesListFragment : Fragment() {
    @Inject
    lateinit var navigationHelper: NavigationHelper

    private var _binding: FragmentRecipesListBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val recipesListViewModel =
            ViewModelProvider(this)[RecipesListViewModel::class.java]

        _binding = FragmentRecipesListBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val dataset = resources.getStringArray(R.array.recipes)
        dataset.sort()
        val customAdapter = RecipesAdapter(dataset, requireContext()) { name ->
            navigationHelper.onRecipeSelect(name)
        }
        val recyclerView: RecyclerView = binding.recyclerViewRecipes
        recyclerView.layoutManager = GridLayoutManager(this.context, 2)
        recyclerView.adapter = customAdapter
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}