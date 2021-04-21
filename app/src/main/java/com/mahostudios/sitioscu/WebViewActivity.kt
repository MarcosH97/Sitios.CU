package com.mahostudios.sitioscu

import android.app.Activity
import android.app.AlertDialog
import android.app.DownloadManager
import android.app.backup.BackupAgent
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.ConnectivityManager
import android.net.Uri
import android.os.*
import androidx.appcompat.app.AppCompatActivity
import android.transition.AutoTransition
import android.transition.TransitionManager
import android.view.View
import android.view.Window
import android.webkit.*
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import java.lang.Exception
import java.util.jar.Manifest

class WebViewActivity : AppCompatActivity() {
    lateinit var webView: WebView
    lateinit var swipeRefreshLayout: SwipeRefreshLayout
    lateinit var progressbar: ProgressBar
    lateinit var constlay: ConstraintLayout
    lateinit var favicon: ImageView
    lateinit var sURL: String
    lateinit var sFileName: String
    lateinit var sUserAgent: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.requestFeature(Window.FEATURE_PROGRESS)
        setContentView(R.layout.activity_web_view)
        val bundle = intent.extras
        val texturl: TextView = findViewById(R.id.text_url)
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
            if (!isNetworkAvaliable(this)) {
                Toast.makeText(this, "No hay conexión", Toast.LENGTH_SHORT).show()
            }
            if (progressbar.visibility == View.GONE) {
                Toast.makeText(this, "No hay conexión", Toast.LENGTH_SHORT).show()
                TransitionManager.beginDelayedTransition(constlay, AutoTransition())
                progressbar.visibility = View.VISIBLE
            }
            webView.reload()
        }

    }

    fun getFileType(url: String): String? {
        val contentResolver: ContentResolver = contentResolver
        val mimeTypeMap = MimeTypeMap.getSingleton()
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(Uri.parse(url)))
    }

    fun downloadFile(filename: String, url: String, userAgent: String) {
        try {
            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val request = DownloadManager.Request(Uri.parse(url))
            val cookie = CookieManager.getInstance().getCookie(url)
            request.setTitle(filename)
                    .setDescription("being downloaded")
                    .addRequestHeader("cookie", cookie)
                    .addRequestHeader("User-Agent", userAgent)
                    .setMimeType(getFileType(url))
                    .setAllowedOverRoaming(true)
                    .setAllowedOverMetered(true)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            downloadManager.enqueue(request)
            sURL = ""
            sURL = ""
            sFileName = ""
            Toast.makeText(applicationContext, "Descargando", Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(applicationContext, "Error $e", Toast.LENGTH_LONG).show()
        }

    }

    fun LoadWeb(url: String) {
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
        webView.settings.setSupportZoom(true)
        webView.settings.builtInZoomControls = true
        webView.settings.javaScriptEnabled = true
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.setSupportMultipleWindows(false)
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.cacheMode = WebSettings.LOAD_DEFAULT
        webView.settings.setAppCacheEnabled(true)
        webView.settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
        webView.settings.allowUniversalAccessFromFileURLs = true
        webView.settings.domStorageEnabled = true
//        favicon.setImageBitmap(webView.favicon)
        ProgressBar()
        webView.setDownloadListener { url, userAgent, contentDisposition, mimeType, contentLength ->

            val filename: String = URLUtil.guessFileName(url, contentDisposition, getFileType(url))
            sURL = url
            sFileName = filename
            sUserAgent = userAgent

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (ContextCompat.checkSelfPermission(applicationContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    downloadFile(filename, url, userAgent)
                } else {
                    ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE), 1001)
                }

            } else {
                downloadFile(filename, url, userAgent)
            }
        }
    }

    fun ProgressBar() {
        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView?, newProgress: Int) {
                super.onProgressChanged(view, newProgress)
                progressbar.progress = newProgress
                if (progressbar.progress == 100) {
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

    fun isNetworkAvaliable(context: Context): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetworkInfo = cm.activeNetworkInfo
        return activeNetworkInfo != null && activeNetworkInfo.isConnected
    }

    override fun onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack()
        } else
            super.onBackPressed()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1001) {
            if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                if (!sURL.equals("") && !sUserAgent.equals("") && !sFileName.equals(""))
                    downloadFile(sFileName, sURL, sUserAgent)
            }
        }
    }
}


                                     //SCRAP CODE

    //            val request = DownloadManager.Request(Uri.parse(url))
//            request.allowScanningByMediaScanner()
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            val dm = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
//            dm.enqueue(request)



//            request.setMimeType(mimeType)
//            request.addRequestHeader("cookie", CookieManager.getInstance().getCookie(url))
//            request.addRequestHeader("User-Agent", userAgent)
//            request.setDescription("Downloading file...")
//            request.setTitle(URLUtil.guessFileName(url, contentDisposition, mimeType))
//            request.allowScanningByMediaScanner()
//            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//            request.setDestinationInExternalFilesDir(this@WebViewActivity, Environment.DIRECTORY_DOWNLOADS, ".png")
//            val dm = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//            dm.enqueue(request)
