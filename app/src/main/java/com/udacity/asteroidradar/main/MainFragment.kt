package com.udacity.asteroidradar.main

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    lateinit var adapter: AsteroidsAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        adapter = AsteroidsAdapter(onClickListenerNavigate())

        binding.asteroidRecycler.adapter = adapter

        var imageView = binding.activityMainImageOfTheDay

        viewModel.asteroidlist.observe(viewLifecycleOwner, Observer { asteroid ->
            adapter.submitList(asteroid)
        })

        viewModel.pictureOfDay.observe(viewLifecycleOwner, Observer { picture ->
                Picasso
                    .with(context)
                    .load(picture.url)
                    .into(imageView)
        })

        setHasOptionsMenu(true)

        return binding.root
    }

    private fun onClickListenerNavigate() = AsteroidsAdapter.OnClickListener { asteroid ->
        this.findNavController().navigate(MainFragmentDirections.actionShowDetail(asteroid))
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.show_week_menu -> viewModel.setAsteroidFilter(MainViewModel.AsteroidFilter.WEEKLY)
            R.id.show_today_menu -> viewModel.setAsteroidFilter(MainViewModel.AsteroidFilter.TODAY)
            else -> viewModel.setAsteroidFilter(MainViewModel.AsteroidFilter.WEEKLY)
        }

        return true
    }
}
