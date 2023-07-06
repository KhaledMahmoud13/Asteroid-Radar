package com.udacity.asteroidradar.main

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.squareup.picasso.Picasso
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding

class MainFragment : Fragment() {

    private val viewModel: MainViewModel by lazy {
        val activity = requireNotNull(this.activity)
        ViewModelProvider(
            this,
            MainViewModelFactory(activity.application)
        )[MainViewModel::class.java]
    }

    private lateinit var asteroidAdapter: AsteroidAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this

        binding.viewModel = viewModel

        setHasOptionsMenu(false)

        asteroidAdapter = AsteroidAdapter(AsteroidAdapter.OnClickListener {
            viewModel.displayAsteroidDetails(it)
        })

        viewModel.navigateToSelectedAsteroid.observe(viewLifecycleOwner) {
            if (null != it) {
                findNavController().navigate(MainFragmentDirections.actionShowDetail(it))
                viewModel.displayAsteroidDetailsDone()
            }
        }

        binding.asteroidRecycler.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = asteroidAdapter
        }

        viewModel.asteroid.observe(viewLifecycleOwner) { items ->
            asteroidAdapter.submitList(items)
        }

        return binding.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return true
    }
}
