package com.kotlinninja.dailyupdates.activity

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.browser.customtabs.CustomTabsIntent
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.kotlinninja.dailyupdates.*
import com.kotlinninja.dailyupdates.model.News

class MainActivity : AppCompatActivity(), NewsItemClicked {
    lateinit var recyclerView: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var mAdapter: NewsListAdapter

    val newsArray = ArrayList<News>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        layoutManager = LinearLayoutManager(this)



        val queue = Volley.newRequestQueue(this)
        val url = "https://newsapi.org/v2/top-headlines?country=in&apiKey=8a2e308e3cdf4a668796a36fdda31a10"
        val getRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener {
              //  Log.e("answer","$it")
                val newsJsonArray = it.getJSONArray("articles")

                for(i in 0 until  newsJsonArray.length()){
                    val newsJsonObject = newsJsonArray.getJSONObject(i)
                    val news = News(
                        newsJsonObject.getString("publishedAt"),
                        newsJsonObject.getString("title"),
                        newsJsonObject.getString("url"),
                        newsJsonObject.getString("urlToImage")
                    )
                   // Toast.makeText(this,"Entered",Toast.LENGTH_LONG).show()
                    newsArray.add(news)
                    mAdapter = NewsListAdapter(this, newsArray)
                    recyclerView.adapter = mAdapter
                    recyclerView.layoutManager = layoutManager
                }

            },
            Response.ErrorListener { error ->

            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["User-Agent"] = "Mozilla/5.0"
                return params
            }
        }
        queue.add(getRequest)

       // MySingleton.getInstance(this).addToRequestQueue(getRequest)
    }
//
//    fun fetchData() {
//    val queue = Volley.newRequestQueue(this)
//    val url =
//        "https://newsapi.org/v2/top-headlines?country=us&apiKey=8a2e308e3cdf4a668796a36fdda31a10"
//    val jsonObjectRequest = object : JsonObjectRequest(
//        Request.Method.GET, url, null, Response.Listener {
//            val newsJsonArray = it.getJSONArray("articles")
//            val newsArray = ArrayList<News>()
//            for (i in 0 until newsJsonArray.length()) {
//                val newsJsonObject = newsJsonArray.getJSONObject(i)
//                val news = News(
//                    newsJsonObject.getString("author"),
//                    newsJsonObject.getString("title"),
//                    newsJsonObject.getString("url"),
//                    newsJsonObject.getString("urlToImage")
//                )
//                newsArray.add(news)
//            }
//            mAdapter.updateNews(newsArray)
//
//        },
//        Response.ErrorListener {
//
//        }
//    )
//    {
//    @Throws(AuthFailureError::class)
//    override fun getHeaders(): Map<String, String> {
//        val params: MutableMap<String, String> = HashMap()
//        params["User-Agent"] = "Mozilla/5.0"
//        return params
//    }
//}
//        queue.add(jsonObjectRequest)
//        MySingleton.getInstance(this).addToRequestQueue(jsonObjectRequest)
//
//    }


    //leads to the web page when user clicked on it
    override fun onItemClicked(item: News) {
        val builder = CustomTabsIntent.Builder()
        val customTabsIntent = builder.build()
        customTabsIntent.launchUrl(this, Uri.parse(item.url))
    }



    //action bar menus

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.action_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        /// opens action bar menus
        when (item.itemId) {
            // for rating
            R.id.rateUs -> {
                val uri: Uri =
                    Uri.parse("https://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                val goToMarket = Intent(Intent.ACTION_VIEW, uri)
                goToMarket.addFlags(
                    Intent.FLAG_ACTIVITY_NO_HISTORY or
                            Intent.FLAG_ACTIVITY_NEW_DOCUMENT or
                            Intent.FLAG_ACTIVITY_MULTIPLE_TASK
                )
                try {
                    startActivity(goToMarket)
                } catch (e: ActivityNotFoundException) {
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=" + applicationContext.packageName)
                        )
                    )
                }
            }
            // share this app
            R.id.shareApp -> {
                val shareBody =
                    "For latest Mehandi designs, Download Mehandi Adda app: https://play.google.com/store/apps/details?id=" + applicationContext.packageName
                val shareSub = "The best Menhadi App ever"
                val shareIntent = Intent(Intent.ACTION_SEND)
                shareIntent.type = "text/plain"

                shareIntent.putExtra(Intent.EXTRA_SUBJECT, shareSub)
                shareIntent.putExtra(Intent.EXTRA_TEXT, shareBody)
                startActivity(shareIntent)
            }
            R.id.exit -> {
                val alertDialogBuilder = AlertDialog.Builder(this@MainActivity)
                alertDialogBuilder.setTitle(R.string.app_name)
                alertDialogBuilder.setIcon(R.drawable.newspaper)
                alertDialogBuilder.setMessage("Are you sure do want to Exit?")
                alertDialogBuilder.setCancelable(false)
                alertDialogBuilder.setPositiveButton("Yes") { _, _ ->
                    finish()
                }
                alertDialogBuilder.setNegativeButton("No") { _, _ ->
                }
                alertDialogBuilder.setNeutralButton("Cancel") { _, _ ->
                }
                alertDialogBuilder.create().show()
            }
        }
        return super.onOptionsItemSelected(item)
    }


}