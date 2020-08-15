package wooyun.weibohotsearch.adadpter

import android.content.Context
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import wooyun.weibohotsearch.R
import wooyun.weibohotsearch.bean.WBbean

class WbAdapter(private val context: Context) : BaseQuickAdapter<WBbean, BaseViewHolder>(R.layout.wb_item) {
    override fun convert(helper: BaseViewHolder, item: WBbean) {
        helper.setText(R.id.name, item.name)
    }
}