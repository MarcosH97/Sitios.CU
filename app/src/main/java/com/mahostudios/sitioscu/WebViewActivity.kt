package com.mahostudios.sitioscu

import android.content.Context
import android.graphics.Bitmap
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.Window
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

class WebViewActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var progressbar : ProgressBar
    lateinit var constlay : ConstraintLayout
    lateinit var favicon : ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_PROGRESS)
        setContentView(R.layout.activity_web_view)
        val bundle = intent.extras
        val texturl : TextView = findViewById(R.id.text_url)
        swipeRefreshLayout = findViewById(R.id.swipe_refresh)
        webView = findViewById(R.id.webView)
        constlay = findViewById(R.id.cdda)
        favicon = findViewById(R.id.favicon)

        progressbar = findViewById(R.id.progress_bar)
        progressbar.max = 100

        texturl.text = bundle!!.getCharSequence("url").toString()

        window.setFeatureInt(Window.FEATURE_PROGRESS, Window.PROGRESS_VISIBILITY_ON)

        LoadWeb(bundle.getCharSequence("url").toString())

        swipeRefreshLayout.setOnRefreshListener {
            if(!isNetworkAvaliable(this)){
                Toast.makeText(this, "No hay conexión", Toast.LENGTH_SHORT).show()
            }
            if(progressbar.visibility == View.GONE){
                TransitionManager.beginDelayedTransition(constlay, AutoTransition())
                progressbar.visibility = View.VISIBLE
            }
            webView.reload()
        }

    }
    fun LoadWeb(url : String){
        webView.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                view?.loadUrl(url)
                return true
            }

            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                swipeRefreshLayout.isRefreshing = false
            }
        }
        webView.loadUrl(url)
        webView.settings.javaScriptEnabled = true
        webView.settings.useWideViewPort = true
        webView.settings.loadWithOverviewMode = true
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
//        favicon.setImageBitmap(webView.favicon)
        ProgressBar()

    }
    fun ProgressBar(){
        webView.webChromeClient = object : WebChromeClient(){
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressbar.progress = newProgress
                if(progressbar.progress == 100){
                    TransitionManager.beginDelayedTransition(constlay, AutoTransition())
                    progressbar.visibility = View.GONE
                }

            }

            override fun onReceivedTitle(view: WebView?, title: String?) {
                super.onReceivedTitle(view, title)

            }

            override fun onReceivedIcon(view: WebView?, icon: Bitmap?) {
                favicon.setImageBitmap(icon)
                super.onReceivedIcon(view, icon)
            }
        }

    }
    fun isNetworkAvaliable(context : Context) : Boolean{
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }
}