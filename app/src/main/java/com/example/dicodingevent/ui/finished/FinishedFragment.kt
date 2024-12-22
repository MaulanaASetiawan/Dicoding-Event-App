package com.example.dicodingevent.ui.finished

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.dicodingevent.databinding.FragmentFinishedBinding
import com.example.dicodingevent.ui.detail.DetailActivity
import com.example.dicodingevent.ui.factory.ViewModelFactory
import com.google.android.material.snackbar.Snackbar

class FinishedFragment : Fragment() {
    private lateinit var adapter: FinishedAdapter
    private lateinit var finishedViewModel: FinishedViewModel

    private var _binding: FragmentFinishedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFinishedBinding.inflate(inflater, container, false)
        val root: View = binding.root
        return root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        finishedViewModel = ViewModelProvider(this, factory)[FinishedViewModel::class.java]
        adapter = FinishedAdapter()

        adapter.setOnItemClickListener { event ->
            val intent = Intent(requireContext(), DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_EVENT_ID, event.id)
            startActivity(intent)
        }

        binding.rvFinished.adapter = adapter
        binding.rvFinished.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)

        observeViewModel()
        if(networkAvailable()){
            finishedViewModel.getFinished()
            finishedViewModel.resetErrorMessage()
        } else {
            Snackbar.make(requireView(), "No Internet Connection", Snackbar.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        finishedViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        finishedViewModel.finishedModel.observe(viewLifecycleOwner) { events ->
            adapter.submitList(events)
        }

        finishedViewModel.errorMessage.observe(viewLifecycleOwner) {
            if (it != null) {
                Snackbar.make(requireView(), "No Internet Connection", Snackbar.LENGTH_SHORT).show()
                finishedViewModel.resetErrorMessage()
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
        val connectivityManager = requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        return networkInfo != null && networkInfo.isConnected
    }
}