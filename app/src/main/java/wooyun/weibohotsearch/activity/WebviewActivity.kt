package wooyun.weibohotsearch.activity

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import wooyun.weibohotsearch.R


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class WebviewActivity : AppCompatActivity() {
    private var wbTitle: TextView? = null
    private var webView: WebView? = null
    private var mPbShow: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wb_webv)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val link = intent.getStringExtra("link")
        val title = intent.getStringExtra("title")
        init()
        initView(link!!, title!!)
    }

    private fun init() {
        webView = findViewById(R.id.mWebview)
        mPbShow = findViewById(R.id.progress)
        wbTitle = findViewById(R.id.wb_title);

    }


    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    private fun initView(link: String, title: String) {
        webView!!.settings.javaScriptEnabled = true
        webView!!.setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View, keyCode: Int, event: KeyEvent): Boolean {
                if (event.action === KeyEvent.ACTION_DOWN) {
                    val webView = v as WebView
                    when (keyCode) {
                        KeyEvent.KEYCODE_BACK -> if (webView.canGoBack()) {
                            webView.goBack()
                            return true
                        }
                    }
                }
                return false
            }
        })

        webView!!.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                super.onPageFinished(view, url)
                // 加载完成
                wbTitle!!.text = title
            }

            override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
                super.onPageStarted(view, url, favicon)
                // 加载开始
                wbTitle!!.text = "加载中"
            }
        }

        webView!!.loadUrl(link)
    }

    override fun onBackPressed() {
        if (webView!!.canGoBack()) {
            webView!!.goBack()
        } else {
            super.onBackPressed()
        }
    }

}
