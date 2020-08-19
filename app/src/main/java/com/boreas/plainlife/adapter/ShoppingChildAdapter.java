package com.boreas.plainlife.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.boreas.plainlife.R;
import com.boreas.plainlife.base.adapter.BaseRecyclerAdapter;
import com.boreas.plainlife.base.adapter.BaseRecyclerHolder;
import com.boreas.plainlife.mvp.models.ShoppingModel;
import com.boreas.plainlife.utils.ImageUtil;

import java.util.List;

public class ShoppingChildAdapter extends BaseRecyclerAdapter<ShoppingModel.ShoppingChildModel> {

    public ShoppingChildAdapter(Context context, List<ShoppingModel.ShoppingChildModel> list, int itemLayoutId) {
        super(context, list, itemLayoutId);
    }

    @Override
    public void convert(BaseRecyclerHolder holder, ShoppingModel.ShoppingChildModel item, int position, boolean isScrolling) {
        TextView childCheckbox = holder.getView(R.id.childCheckbox);
        ImageView childImage = holder.getView(R.id.childImage);
        TextView childTitle = holder.getView(R.id.childTitle);
        TextView childGoodsLocation = holder.getView(R.id.childGoodsLocation);
        childCheckbox.setSelected(item.isChecked());
        childTitle.setText(item.getTitle());
        childGoodsLocation.setText("货架位置："+item.getGoodsLocation());
        ImageUtil.loadImg(getContext(),item.getImgUrl(),childImage);
    }
}