package com.example.foody.fragments

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foody.R
import com.example.foody.adapters.FavoriteRecipeAdapter
import com.example.foody.databinding.FragmentFavoriteRecipesBinding
import com.example.foody.databinding.FragmentRecipesBinding
import com.example.foody.viewModels.MainViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoriteRecipesFragment : Fragment() {
    private var _binding: FragmentFavoriteRecipesBinding? = null
    private val binding get() = _binding!!
    private val mainViewModel :MainViewModel by viewModels()
    private val mAdapter:FavoriteRecipeAdapter by lazy { FavoriteRecipeAdapter(requireActivity(),mainViewModel) }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding= FragmentFavoriteRecipesBinding.inflate(inflater,container,false)
        binding.lifecycleOwner = this
        binding.mainViewModel= mainViewModel
        binding.mAdapter = mAdapter
        setHasOptionsMenu(true)
        setUpRecyclerView(binding.favoriteRecipeRecyclerview)
        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_recipes_menu,menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId== R.id.deleteAll_favorite_recipes_menu){
            mainViewModel.deleteAllFavoriteRecipes()
            showSnackBar("Recipes removed")
        }
        return super.onOptionsItemSelected(item)
    }
    private fun showSnackBar(message:String){
        Snackbar.make(binding.root,message, Snackbar.LENGTH_LONG).setAction("Okay"){}
            .show()
    }

    private fun setUpRecyclerView(recyclerview:RecyclerView){

        recyclerview.adapter = mAdapter
        recyclerview.layoutManager = LinearLayoutManager(requireContext())
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
        mAdapter.clearContextualActionMode()
    }
}