package com.melissa.flixter

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.RatingBar
import android.widget.TextView
import com.codepath.asynchttpclient.AsyncHttpClient
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler
import com.google.android.youtube.player.YouTubeBaseActivity
import com.google.android.youtube.player.YouTubeInitializationResult
import com.google.android.youtube.player.YouTubePlayer
import com.google.android.youtube.player.YouTubePlayerView
import okhttp3.Headers

private const val TRAILERS_URL = "https://api.themoviedb.org/3/movie/%d/videos?api_key=${BuildConfig.MOVIES_KEY}"
private const val TAG = "DetailActivity"
class DetailActivity : YouTubeBaseActivity() {
    private lateinit var tvTitle:TextView
    private lateinit var tvOverview:TextView
    private lateinit var rvRatingBar: RatingBar
    private lateinit var ytPlayerView: YouTubePlayerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        tvTitle = findViewById(R.id.tvTitle)
        tvOverview = findViewById(R.id.tvOverview)
        rvRatingBar = findViewById(R.id.rbVoteAverage)
        ytPlayerView = findViewById(R.id.player)

        val movie = intent.getParcelableExtra<Movie>(MOVIE_EXTRA) as Movie
        Log.i(TAG, "Movie is $movie")
        tvTitle.text = movie.title
        tvOverview.text = movie.overview
        rvRatingBar.rating = movie.voteAverage.toFloat()

        val client = AsyncHttpClient()
        client.get(TRAILERS_URL.format(movie.movieId), object: JsonHttpResponseHandler(){
            override fun onFailure(
                statusCode: Int,
                headers: Headers?,
                response: String?,
                throwable: Throwable?
            ) {
                Log.e(TAG, "onFailure ${movie.movieId}")
                Log.e(TAG, "onFailure $statusCode")
            }

            override fun onSuccess(statusCode: Int, headers: Headers?, json: JSON) {
                Log.i(TAG, "onSuccess $statusCode")
                // not null
                val results = json.jsonObject.getJSONArray("results")
                Log.i(TAG, "onSuccess $results")
                if (results.length() == 0) {
                    Log.w(TAG, "no movie trailers found")
                    return
                }
                val movieTrailerJson = results.getJSONObject(0)
                val youtubeKey = movieTrailerJson.getString("key")
                // play youtube video with this trailer
                Log.i(TAG, "Success calling YT API Load on title: ${movie.title}, id: ${movie.movieId}, movie key: ${youtubeKey}")
                initializeYoutube(youtubeKey)
            }

        })
    }

    private fun initializeYoutube(youtubeKey: String) {
        ytPlayerView.initialize(BuildConfig.YOUTUBE_KEY, object: YouTubePlayer.OnInitializedListener{

            override fun onInitializationSuccess(
                provider: YouTubePlayer.Provider?,
                player: YouTubePlayer?,
                p2: Boolean
            ) {
                Log.i(TAG, "onInitializationSuccess $youtubeKey")
                Log.i(TAG, "onInitializationSuccess")
                player?.cueVideo(youtubeKey.toString())
            }

            override fun onInitializationFailure(
                p0: YouTubePlayer.Provider?,
                p1: YouTubeInitializationResult?
            ) {
                Log.i(TAG, "onInitializationFailure $youtubeKey")
                Log.e(TAG, "onInitializationFailure $p1")
                Log.e(TAG, "onInitializationFailure $p0")
                Log.i(TAG, BuildConfig.YOUTUBE_KEY)
                Log.i(TAG, "onInitializationFailure")
            }
        })
    }
}