package wooyun.weibohotsearch.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import wooyun.weibohotsearch.R


@Suppress("DEPRECATED_IDENTITY_EQUALS")
class WebviewActivity : AppCompatActivity() {
    /*
        private var mTbShow: Toolbar? = null
    */
    private var webView: WebView? = null
    private var mPbShow: ProgressBar? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wb_webv)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val link = intent.getStringExtra("link")

        init()
        initView(link!!)

    }

    private fun init() {
        webView = findViewById(R.id.mWebview)
        mPbShow = findViewById(R.id.pb_show_activity)
    }


    @SuppressLint("SetJavaScriptEnabled", "ObsoleteSdkInt")
    private fun initView(link: String) {
        webView!!.settings.javaScriptEnabled = true
        webView!!.webViewClient = WebViewClient()
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
