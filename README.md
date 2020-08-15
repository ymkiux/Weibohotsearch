> 简单爬取微博热搜内容并显示的demo

#### 添加依赖 ####

    implementation 'org.jsoup:jsoup:1.9.2'
    implementation group: 'com.github.CymChad', name: 'BaseRecyclerViewAdapterHelper', version: '2.9.34'
    implementation group: 'androidx.swiperefreshlayout', name: 'swiperefreshlayout', version: '1.2.0-alpha01'

#### 打开微博热搜页 ####

> [微博热搜页](https://s.weibo.com/top/summary?Refer=top_hot&topnav=1&wvr=6)

#### 爬数据  ####

获取 `Document` 对象 

```
Document document = Jsoup.connect(url)
                         .get();
```
>这里通过建立与服务器的链接，并获取 `Document` 对象

获取跟标签的 `Elements` 对象

```
Elements elements1 = document.select("tbody");
Elements elements2 = elements1.select("tr");
```
>热搜内容列表通过 `select` 方法查询节点所有信息

 通过遍历显示即可

```
for (Element element : elements2) {
    WBbean bean = new WBbean();
    bean.setName(element.select("td.td-02").text());
    List.add(bean);
}
```

> 由于空闲时间编写 如有不足还请指正 