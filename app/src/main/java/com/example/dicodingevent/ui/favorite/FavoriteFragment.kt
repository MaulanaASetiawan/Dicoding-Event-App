package com.example.dicodingevent.ui.favorite

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentFavoriteBinding
import com.example.dicodingevent.ui.detail.DetailActivity
import com.example.dicodingevent.ui.factory.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }
    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding

    private lateinit var viewModel: FavoriteViewModel
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val factory = ViewModelFactory.getInstance(requireContext())
        viewModel = ViewModelProvider(this, factory)[FavoriteViewModel::class.java]

        adapter = FavoriteAdapter { favoriteEvent ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(EXTRA_EVENT_ID, favoriteEvent.id.toInt())
            startActivity(intent)
        }

        binding?.rvFavorite?.adapter = adapter
        binding?.rvFavorite?.layoutManager = LinearLayoutManager(requireContext())

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.getAllFavorite().observe(viewLifecycleOwner) { favoriteEvents ->
            if (favoriteEvents.isEmpty()) {
                adapter.setFavorite(favoriteEvents)
                Snackbar.make(requireView(), "No favorite events", Snackbar.LENGTH_SHORT).show()
            } else {
                adapter.setFavorite(favoriteEvents)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}