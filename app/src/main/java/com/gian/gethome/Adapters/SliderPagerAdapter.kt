package com.gian.gethome.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.gian.gethome.Clases.SlideFirstScreen
import com.gian.gethome.R

class SliderPagerAdapter(private val mContext: Context, private val mList: List<SlideFirstScreen>) : PagerAdapter() {
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val slideLayout = inflater.inflate(R.layout.slide_item, null)
        val slideImg = slideLayout.findViewById<ImageView>(R.id.slide_img)
        slideImg.setImageResource(mList[position].image)
        container.addView(slideLayout)
        return slideLayout
    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun isViewFromObject(view: View, o: Any): Boolean {
        return view === o
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }
}