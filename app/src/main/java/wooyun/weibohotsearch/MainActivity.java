package wooyun.weibohotsearch;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.chad.library.adapter.base.BaseQuickAdapter;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import wooyun.weibohotsearch.adadpter.WbAdapter;
import wooyun.weibohotsearch.api.Api;
import wooyun.weibohotsearch.bean.WBbean;

public class MainActivity extends AppCompatActivity {
    private List<WBbean> List = new ArrayList<>();
    private Document document;
    private String url;
    private WbAdapter adapter;
    private SwipeRefreshLayout updateUi;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.wb_main);
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        Api api = new Api();
        url = api.getUrl();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        updateUi = (SwipeRefreshLayout) findViewById(R.id.update_ui);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new WbAdapter(MainActivity.this);
        adapter.openLoadAnimation(BaseQuickAdapter.SCALEIN);
        recyclerView.setAdapter(adapter);
        updateUI();
        initView();
    }

    private void initView() {
        updateUi.setColorSchemeColors(Color.RED, Color.WHITE);
        updateUi.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateUi.setRefreshing(true);
                updateUI();
                (new Handler()).postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        updateUi.setRefreshing(false);
                    }
                }, 1000);
            }
        });
    }

    private void updateUI() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    document = Jsoup.connect(url).get();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements elements1 = document.select("tbody");
                Elements elements2 = elements1.select("tr");
                for (Element element : elements2) {
                    WBbean bean = new WBbean();
                    bean.setName(element.select("td.td-02").text());
                    List.add(bean);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.setNewData(List);
                    }
                });

            }
        }).start();
    }
}