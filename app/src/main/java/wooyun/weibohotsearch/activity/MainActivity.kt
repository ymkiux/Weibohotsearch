package wooyun.weibohotsearch.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import wooyun.weibohotsearch.R
import wooyun.weibohotsearch.adadpter.WbAdapter
import wooyun.weibohotsearch.api.Api
import wooyun.weibohotsearch.bean.WBbean
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity() {
    private val List: MutableList<WBbean> = ArrayList()
    private var document: Document? = null
    private var url: String? = null
    private var adapter: WbAdapter? = null
    private var updateUi: SwipeRefreshLayout? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wb_main)
        if (supportActionBar != null) {
            supportActionBar!!.hide()
        }
        val api = Api()
        url = api.url
        val recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        updateUi = findViewById<View>(R.id.update_ui) as SwipeRefreshLayout
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        adapter = WbAdapter(this@MainActivity)
        adapter!!.openLoadAnimation(BaseQuickAdapter.SCALEIN)
        recyclerView.adapter = adapter
        updateUI()
        initView()
    }
    private fun initView() {
        updateUi!!.setColorSchemeColors(Color.RED, Color.WHITE)
        updateUi!!.setOnRefreshListener {
            updateUi!!.isRefreshing = true
            updateUI()
            Handler().postDelayed({ updateUi!!.isRefreshing = false }, 1000)
        }
    }

    private fun updateUI() {
        Thread(Runnable {
            try {
                document = Jsoup.connect(url).get()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            val elements1 = document!!.select("tbody")
            val elements2 = elements1.select("tr")
            for (element in elements2) {
                val bean = WBbean()
                bean.name = element.select("td.td-02").text()
                List.add(bean)
            }
            runOnUiThread { adapter!!.setNewData(List) }
        }).start()
    }
}