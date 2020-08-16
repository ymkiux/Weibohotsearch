package wooyun.weibohotsearch.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.view.View.OnLongClickListener
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.listener.OnItemChildClickListener
import com.melnykov.fab.FloatingActionButton
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import wooyun.weibohotsearch.R
import wooyun.weibohotsearch.adadpter.WbAdapter
import wooyun.weibohotsearch.api.Api
import wooyun.weibohotsearch.bean.WBbean
import java.io.IOException
import java.util.*


class MainActivity : AppCompatActivity() {
    private var fab: FloatingActionButton? = null
    private var recyclerView: RecyclerView? = null
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
        recyclerView = findViewById<View>(R.id.recycler_view) as RecyclerView
        updateUi = findViewById<View>(R.id.update_ui) as SwipeRefreshLayout
        fab = findViewById(R.id.fab)
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        adapter = WbAdapter(this@MainActivity)
        adapter!!.openLoadAnimation(BaseQuickAdapter.SCALEIN)
        recyclerView!!.adapter = adapter
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


        recyclerView!!.addOnItemTouchListener(object : OnItemChildClickListener() {
            override fun onSimpleItemChildClick(adapter: BaseQuickAdapter<*, *>, view: View, position: Int) {
                when (view.id) {
                    R.id.name -> {
                        val title = Intent(this@MainActivity, WebviewActivity::class.java)
                        title.putExtra("link", (adapter.getItem(position) as WBbean?)!!.url)
                        title.putExtra("title", (adapter.getItem(position) as WBbean?)!!.name)
                        startActivity(title)
                    }
                }
            }
        })


     /*   recyclerView!!.addOnScrollListener(object : RecyclerViewScrollListener() {
            override fun onScrollToBottom() {
                fab!!.visibility = View.VISIBLE
            }
        })*/
        recyclerView!!.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                //获得recyclerView的线性布局管理器
                val manager = recyclerView.layoutManager as LinearLayoutManager?
                //获取到第一个item的显示的下标  不等于0表示第一个item处于不可见状态 说明列表没有滑动到顶部 显示回到顶部按钮
                val firstVisibleItemPosition = manager!!.findFirstVisibleItemPosition()
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 判断是否滚动超过一屏
                    if (firstVisibleItemPosition == 0) {
                        fab!!.visibility = View.GONE
                    } else {
                        fab!!.visibility = View.VISIBLE
                        fab!!.setOnClickListener(View.OnClickListener { recyclerView.scrollToPosition(0) })
                    } //获取RecyclerView滑动时候的状态
                }
            }
        })

        fab!!.setOnLongClickListener(OnLongClickListener {
            fab!!.visibility = View.GONE
            false
        })
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
                bean.name = element.select("a").text()
                bean.url = element.select("a").attr("abs:href")
                var a = element.select("a").text()
                if (a != null) {
                    Log.d("测试", "updateUI: $a")
                }
                List.add(bean)
            }
            runOnUiThread { adapter!!.setNewData(List) }
        }).start()
    }
}