package com.example.dicodingevent.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.dicodingevent.R
import com.example.dicodingevent.data.local.entity.FavoriteEntity
import com.example.dicodingevent.data.remote.response.Event
import com.example.dicodingevent.databinding.ActivityDetailBinding
import com.example.dicodingevent.ui.factory.ViewModelFactory
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.Locale

class DetailActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_EVENT_ID = "extra_event_id"
    }

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DetailViewModel
    private lateinit var favoriteEntity: FavoriteEntity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = getString(R.string.detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val factory = ViewModelFactory.getInstance(this)
        viewModel = ViewModelProvider(this, factory)[DetailViewModel::class.java]

        val eventId = intent.getIntExtra(EXTRA_EVENT_ID, -1)
        viewModel.getDetail(eventId)
        viewModel.checkFavorite(eventId)

        viewModel.events.observe(this) {events ->
            setupData(events)
        }

        viewModel.isLoading.observe(this) {
            showLoading(it)
        }

        viewModel.errorMessage.observe(this) { error ->
            if (error != null) {
                Snackbar.make(binding.root, error, Snackbar.LENGTH_SHORT).show()
            }
        }

        viewModel.isFavorite.observe(this) { isFavorite ->
            if (isFavorite) {
                binding.favorite.setImageResource(R.drawable.favorite)
            } else {
                binding.favorite.setImageResource(R.drawable.favorite_border)
            }
        }

        binding.favorite.setOnClickListener {
            if(viewModel.isFavorite.value == true) {
                viewModel.removeEventFromFavorite(favoriteEntity)
            } else {
                viewModel.addEventToFavorite(favoriteEntity)
            }
        }
    }

    private fun setupData(event: Event) {
        val setupDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        val outputDate = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        val date = setupDate.parse(event.beginTime)
        val formattedDate = date?.let { outputDate.format(it) }

        binding.nameDetail.text = event.name
        binding.summaryDetail.text = event.summary
        binding.waktuDetail.text =binding.root.context.getString(R.string.valid, formattedDate)
        binding.quotaDetail.text = binding.root.context.getString(R.string.quota_remaining, event.quota- event.registrants)
        binding.owner.text = event.ownerName
        binding.descriptionDetail.text = HtmlCompat.fromHtml(event.description, HtmlCompat.FROM_HTML_MODE_LEGACY)
        Glide.with(this)
            .load(event.mediaCover)
            .into(binding.imgDetail)

        favoriteEntity = FavoriteEntity(
            id = event.id.toString(),
            name = event.name,
            owner = event.ownerName,
            imageLogo = event.imageLogo
        )

        binding.btnRegis.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(event.link)
            startActivity(intent)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}