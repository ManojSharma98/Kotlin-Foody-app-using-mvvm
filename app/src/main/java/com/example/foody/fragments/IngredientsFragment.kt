package com.example.foody.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.foody.R
import com.example.foody.adapters.IngredientsAdapter
import com.example.foody.databinding.FragmentIngredientsBinding
import com.example.foody.model.Result
import com.example.foody.util.Constants.Companion.RECIPE_RESULT_KEY

class IngredientsFragment : Fragment() {

    private lateinit var mAdapter : IngredientsAdapter
    private lateinit var binding: FragmentIngredientsBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentIngredientsBinding.inflate(inflater, container, false)
        val args  = arguments
        val myBundle :Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        mAdapter = IngredientsAdapter()
        setupRecyclerView()
        myBundle?.extendedIngredients?.let {
            mAdapter.setData(it)
        }
        return binding.root
    }

    private fun setupRecyclerView(){
        binding.ingredientsRecyclerView.adapter = mAdapter
        binding.ingredientsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

    }

}