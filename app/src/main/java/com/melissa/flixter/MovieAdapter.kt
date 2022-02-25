package com.melissa.flixter

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

private const val TAG = "MovieAdapter"
class MovieAdapter(private val context: Context, private val movies: List<Movie>)
    : RecyclerView.Adapter<MovieAdapter.ViewHolder>(){
    // not a cheap operation
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        Log.i(TAG, "onCreateViewHolder")
        val view = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false)
        return ViewHolder(view)
    }

    // cheap operation
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Log.i(TAG, "onBindViewHolder position $position")
        val movie = movies[position]
        holder.bind(movie)
    }

    override fun getItemCount() = movies.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val tvTitle = itemView.findViewById<TextView>(R.id.textViewTitle)
        private val tvOverview = itemView.findViewById<TextView>(R.id.textViewOverview)
        private val ivPoster = itemView.findViewById<ImageView>(R.id.imageViewPoster)

        fun bind(movie:Movie) {
            tvTitle.text = movie.title
            tvOverview.text = movie.overview
            val resources = context.resources
            var image = ""
            val orientation = resources.configuration.orientation
            if (orientation == Configuration.ORIENTATION_PORTRAIT) {
                image = movie.posterImageUrl
            } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                image = movie.backdropImageUrl
            }
            Glide.with(context).load(image).placeholder(R.drawable.movie_broll).into(ivPoster)
        }
    }
}
