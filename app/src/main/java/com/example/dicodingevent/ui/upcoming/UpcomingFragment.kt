package com.example.dicodingevent.ui.upcoming

import android.content.Context.CONNECTIVITY_SERVICE
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.dicodingevent.databinding.FragmentUpcomingBinding
import com.example.dicodingevent.ui.detail.DetailActivity
import com.example.dicodingevent.ui.factory.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class UpcomingFragment : Fragment() {
    private lateinit var adapter : UpcomingAdapter
    private lateinit var upcomingViewModel: UpcomingViewModel

    private var _binding: FragmentUpcomingBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUpcomingBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        upcomingViewModel = ViewModelProvider(this, factory)[UpcomingViewModel::class.java]
        adapter = UpcomingAdapter()

        adapter.setOnItemClickListener { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            startActivity(intent)
        }

        binding.rvUpcoming.adapter = adapter
        binding.rvUpcoming.layoutManager = LinearLayoutManager(requireContext())

        observeViewModel()
        if(networkAvailable()){
            upcomingViewModel.getItem()
            upcomingViewModel.resetErrorMessage()
        } else {
            Snackbar.make(requireView(), "No Internet Connection", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        upcomingViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        upcomingViewModel.upComingModel.observe(viewLifecycleOwner) {events ->
            adapter.submitList(events)
        }

        upcomingViewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                Snackbar.make(requireView(), "No Internet Connection", Snackbar.LENGTH_SHORT).show()
                upcomingViewModel.resetErrorMessage()
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun networkAvailable(): Boolean {
        val connectivityManager = requireContext().getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}