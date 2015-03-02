package ca.vijaysharma.resume.adapters;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ca.vijaysharma.resume.R;

public class SocialAdapter extends PagerAdapter {
    private final Context context;
    private final LayoutInflater inflater;

    public SocialAdapter(Context context) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object instantiateItem(ViewGroup collection, int position) {
        View view = new TextItemBuild(this.context)
            .setText("Social")
            .setConnectorColor(this.context.getResources().getColor(R.color.yellow))
            .setBackgroundDrawable(this.context.getDrawable(R.drawable.yellow_circle))
            .setAddConnection(position != 0)
            .build();
        collection.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup collection, int position, Object view) {
        collection.removeView((View) view);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return PagerAdapter.POSITION_NONE;
    }
}