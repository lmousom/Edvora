package com.mousom.edvora.ui

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupWindow
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayoutMediator
import com.mousom.edvora.R
import com.mousom.edvora.data.adapter.RideStateAdapter
import com.mousom.edvora.data.repository.BaseRepository
import com.mousom.edvora.databinding.ActivityMainBinding
import com.mousom.edvora.viewmodel.MainViewModel
import com.mousom.edvora.viewmodel.MainViewModelFactory
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var profileMenu: MenuItem
    private lateinit var nameMenu: MenuItem
    private lateinit var name: String
    private lateinit var imageUrl: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        setSupportActionBar(binding.toolbar)
        val window = PopupWindow(this)
        val windowView = layoutInflater.inflate(R.layout.filter_custom_layout, null)
        window.apply {
            width = 600
            contentView = windowView
            isOutsideTouchable = true
            setBackgroundDrawable(
                ColorDrawable(
                    Color.TRANSPARENT
                )
            )
        }

        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val adapter = RideStateAdapter(supportFragmentManager, lifecycle)
        viewPager.adapter = adapter


//        val popup = PopupMenu(this, binding.filters)
//        menuInflater.inflate(R.menu.filter_menu, popup.menu)
        binding.filters.setOnClickListener {
            window.showAsDropDown(binding.filters, -410, 0)
        }
        val repository = BaseRepository()
        val viewModelFactory = MainViewModelFactory(repository)
        viewModel = ViewModelProvider(
            this,
            viewModelFactory
        )[MainViewModel::class.java]

        viewModel.getUserResponse("/user")

        viewModel.getUserResponse.observe(this) {
            if (it.isSuccessful) {
                name = it.body()?.name ?: ""
                imageUrl = it.body()?.url ?: ""
            }


            val img: CircleImageView =
                profileMenu.actionView.findViewById(R.id.toolbar_profile_image)
            nameMenu.title = name
            Picasso.get().load(imageUrl).into(img)
        }


        val ridesViews = listOf(
            "Nearest",
            "Upcoming (4)",
            "Past (2)"
        )



        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = ridesViews.get(position)

        }.attach()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.profile_menu, menu)
        profileMenu = menu!!.findItem(R.id.menu_two)
        nameMenu = menu.findItem(R.id.menu_one)
        return super.onCreateOptionsMenu(menu)
    }


}