package com.example.foody.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebViewClient
import com.example.foody.R
import com.example.foody.databinding.FragmentIngredientsBinding
import com.example.foody.databinding.FragmentInstructionBinding
import com.example.foody.model.Result
import com.example.foody.util.Constants

class InstructionFragment : Fragment() {

    private lateinit var binding:FragmentInstructionBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding= FragmentInstructionBinding.inflate(inflater, container, false)
        val args = arguments
        val myBundle: Result? = args?.getParcelable(Constants.RECIPE_RESULT_KEY)

        binding.instructionsWebView.webViewClient = object :WebViewClient(){}
        val websiteUrl:String = myBundle!!.sourceUrl
        binding.instructionsWebView.loadUrl(websiteUrl)
        return binding.root
    }

}