package com.example.dicodingevent.ui.search

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.ActivitySearchBinding
import com.example.dicodingevent.ui.detail.DetailActivity
import com.example.dicodingevent.ui.factory.ViewModelFactory
import com.example.dicodingevent.ui.home.HomeAdapter
import com.google.android.material.snackbar.Snackbar

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var adapter: HomeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val factory = ViewModelFactory.getInstance(this)
        searchViewModel = ViewModelProvider(this, factory)[SearchViewModel::class.java]
        adapter = HomeAdapter(emptyList(), false)

        binding.rvSearch.adapter = adapter
        binding.rvSearch.layoutManager = LinearLayoutManager(this)
        binding.searchView.setupWithSearchBar(binding.searchBar)

        binding.searchView.editText.setOnEditorActionListener { _, _, _ ->
            val query = binding.searchView.editText.text.toString()
            if (query.isNotEmpty()) {
                searchViewModel.searchEvent(query)
            }
            true
        }

        searchViewModel.searchEvent.observe(this) {events ->
            if(HomeAdapter(events, true).itemCount == 0){
                Snackbar.make(binding.root, "No event found", Snackbar.LENGTH_SHORT).show()
            }else{
                adapter = HomeAdapter(events, false)
                binding.rvSearch.adapter = adapter

                adapter.setOnItemClickListener {
                    val intent = Intent(this, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_EVENT_ID, it.id)
                    startActivity(intent)
                }
            }
        }

        searchViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.progressBar.visibility = View.VISIBLE
            } else {
                binding.progressBar.visibility = View.GONE
            }
        }

        searchViewModel.errorMessage.observe(this) { error ->
            if (error != null) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
