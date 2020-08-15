package wooyun.weibohotsearch.adadpter;

import android.content.Context;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import wooyun.weibohotsearch.R;
import wooyun.weibohotsearch.bean.WBbean;

public class WbAdapter extends BaseQuickAdapter<WBbean, BaseViewHolder> {
    private Context context;

    public WbAdapter(Context context) {
        super(R.layout.wb_item);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, WBbean item) {
        helper.setText(R.id.name, item.getName());
    }
}