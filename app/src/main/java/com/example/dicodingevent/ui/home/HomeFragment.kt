package com.example.dicodingevent.ui.home

import android.content.Context
import android.content.Intent
import android.graphics.Rect
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.databinding.FragmentHomeBinding
import com.example.dicodingevent.ui.detail.DetailActivity
import com.example.dicodingevent.ui.factory.ViewModelFactory
import com.example.dicodingevent.ui.finished.FinishedAdapter
import com.example.dicodingevent.ui.search.SearchActivity
import com.example.dicodingevent.ui.upcoming.UpcomingAdapter
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private lateinit var upcomingAdapter: UpcomingAdapter
    private lateinit var finishedAdapter: FinishedAdapter
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireActivity())
        homeViewModel = ViewModelProvider(this, factory)[HomeViewModel::class.java]
        upcomingAdapter = UpcomingAdapter()
        finishedAdapter = FinishedAdapter()

        binding.rvUpcomingHome.adapter = upcomingAdapter
        binding.rvFinishedHome.adapter = finishedAdapter
        binding.rvFinishedHome.layoutManager = LinearLayoutManager(requireContext())
        binding.searchBar.setOnClickListener {
            val intent = Intent(requireContext(), SearchActivity::class.java)
            startActivity(intent)
        }

        observeViewModel()
        if(netWorkAvailable()){
            homeViewModel.getUpcomingEvent()
            homeViewModel.getFinishedEvent()
        }else{
            Snackbar.make(view, "No Internet Connection", Snackbar.LENGTH_LONG).show()
        }
    }

    private fun observeViewModel() {
        homeViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        homeViewModel.upcomingEvent.observe(viewLifecycleOwner) { events ->
            setItem(events, true)
        }

        homeViewModel.finishedEvent.observe(viewLifecycleOwner) { events ->
            setItem(events, false)
        }

        homeViewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        }
    }

    private fun setItem(list: List<ListEventsItem>, isList: Boolean){
        if(isList){
            val upcomingAdapter = HomeAdapter(list, true)
            binding.rvUpcomingHome.adapter = upcomingAdapter
            binding.rvUpcomingHome.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            binding.rvUpcomingHome.addItemDecoration(ItemSpacingDecoration(10))
            upcomingAdapter.setOnItemClickListener { event ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
                startActivity(intent)
            }
        } else {
            val finishedAdapter = HomeAdapter(list, false)
            binding.rvFinishedHome.adapter = finishedAdapter
            binding.rvFinishedHome.layoutManager = LinearLayoutManager(context)
            binding.rvFinishedHome.addItemDecoration(ItemSpacingDecoration(10))
            finishedAdapter.setOnItemClickListener { event ->
                val intent = Intent(requireContext(), DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
                startActivity(intent)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    class ItemSpacingDecoration(private val spacing: Int) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            outRect.apply {
                left = spacing
                right = spacing
                bottom = spacing
                top = spacing
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun netWorkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}
