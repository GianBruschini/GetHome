package com.gian.gethome.Activities

import android.graphics.drawable.Drawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.gian.gethome.R
import com.gian.gethome.databinding.ActivityTestBinding
import com.skydoves.powerspinner.IconSpinnerAdapter
import com.skydoves.powerspinner.IconSpinnerItem

class testActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityTestBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.spinnerView.apply {
            setSpinnerAdapter(IconSpinnerAdapter(this))
            setItems(
                    arrayListOf(
                            IconSpinnerItem(text = "USA"),
                            IconSpinnerItem(text = "UK"),
                            IconSpinnerItem(text = "France"),
                            IconSpinnerItem(text = "Canada"),
                            IconSpinnerItem(text = "South Korea"),
                            IconSpinnerItem(text = "Germany"),
                            IconSpinnerItem(text = "Spain"),
                            IconSpinnerItem(text = "China")
                    )
            )
            setOnSpinnerItemSelectedListener<IconSpinnerItem> { _, _, _, item ->
                Toast.makeText(applicationContext, item.text, Toast.LENGTH_SHORT).show()
            }
            getSpinnerRecyclerView().layoutManager = GridLayoutManager(baseContext, 2)
            selectItemByIndex(4)
            preferenceName = "country"
        }

        binding.spinnerView1.apply {
            setOnSpinnerItemSelectedListener<String> { _, _, _, item ->
                binding.spinnerView2.hint = item
                Toast.makeText(applicationContext, item, Toast.LENGTH_SHORT).show()
            }
            preferenceName = "question1"
        }

        binding.spinnerView2.preferenceName = "question2"

        binding.spinnerView3.preferenceName = "year"

        binding.spinnerView4.preferenceName = "month"

        binding.spinnerView5.preferenceName = "day"
    }

    private fun contextDrawable(@DrawableRes resource: Int): Drawable? {
        return ContextCompat.getDrawable(this, resource)
    }
}