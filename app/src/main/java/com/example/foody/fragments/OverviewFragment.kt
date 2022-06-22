package com.example.foody.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import coil.load
import com.example.foody.R
import com.example.foody.databinding.FragmentOverviewBinding
import com.example.foody.model.Result
import com.example.foody.util.Constants.Companion.RECIPE_RESULT_KEY
import org.jsoup.Jsoup

class OverviewFragment : Fragment() {
    private lateinit var binding:FragmentOverviewBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentOverviewBinding.inflate(inflater, container, false)

        val args = arguments
        val myBundle:Result? = args?.getParcelable(RECIPE_RESULT_KEY)
        binding.mainImageView.load(myBundle?.image)
        binding.titleText.text = myBundle?.title
        binding.likesTextView.text = myBundle?.aggregateLikes.toString()
        binding.clockTextView.text = myBundle?.readyInMinutes.toString()
        myBundle?.summary.let {
            val summary = Jsoup.parse(it).text()
            binding.description.text = summary
        }

        if (myBundle?.vegetarian == true){
            binding.vegetarianImageview.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.vegetarianTextview.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }

        if (myBundle?.vegan == true){
            binding.vegetarianImageview.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.vegetarianTextview.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.glutenFree == true){
            binding.glutenfreeImageview.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.glutenfreeTextview.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.dairyFree == true){
            binding.diaryfreeImageview.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.diaryfreeTextview.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.veryHealthy == true){
            binding.healthyImageview.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.healthyTextview.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        if (myBundle?.cheap == true){
            binding.cheapImageview.setColorFilter(ContextCompat.getColor(requireContext(),R.color.green))
            binding.cheapTextview.setTextColor(ContextCompat.getColor(requireContext(),R.color.green))
        }
        return  binding.root
    }


}