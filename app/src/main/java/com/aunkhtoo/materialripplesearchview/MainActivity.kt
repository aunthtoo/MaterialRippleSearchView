package com.aunkhtoo.materialripplesearchview

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.util.Log
import android.util.TypedValue
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.view.MenuItemCompat

class MainActivity : AppCompatActivity() {

    private var menuSearch: Menu? = null
    private var itemSearch: MenuItem? = null
    private var toolbar: Toolbar? = null
    private var toolbarSearch: Toolbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupToolbar()


    }

    fun setupToolbar() {
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)

        setupToolbarSearch()
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {

            R.id.menuSearch -> {

                itemSearch?.expandActionView()

                return true
            }

        }

        return super.onOptionsItemSelected(item)

    }

    //toolbar ripple search
    protected fun setupToolbarSearch() {

        toolbarSearch = findViewById(R.id.searchToolbar)

        toolbarSearch?.inflateMenu(R.menu.menu_search)
        menuSearch = toolbarSearch?.menu

        toolbarSearch?.setNavigationOnClickListener {
           itemSearch?.collapseActionView()

        }


        //search item
        itemSearch = menuSearch?.findItem(R.id.action_filter_search)


        MenuItemCompat.setOnActionExpandListener(itemSearch, object : MenuItemCompat.OnActionExpandListener {
            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {

                // Do something when collapsed
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.searchToolbar, 1, true, false)
                else
                    toolbarSearch?.visibility = View.GONE

                return true
            }

            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                // Do something when expanded
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
                    circleReveal(R.id.searchToolbar, 1, true, true)
                else
                    toolbarSearch?.visibility = View.VISIBLE

                return true
            }
        })

        initSearchView()


    }


    //initialize search view
    fun initSearchView() {
        val searchView = menuSearch?.findItem(R.id.action_filter_search)?.actionView as SearchView

        // Enable/Disable Submit button in the keyboard

        searchView.isSubmitButtonEnabled = false

        // Change search close button image

        val closeButton = searchView.findViewById<ImageView>(R.id.search_close_btn)
        closeButton.setImageResource(R.drawable.ic_close)


        // set hint and the text colors

        val txtSearch = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        txtSearch.hint = resources.getString(R.string.tosearch)
        txtSearch.setHintTextColor(Color.DKGRAY)
        txtSearch.setBackgroundColor(Color.parseColor("#00ffffff"))
        txtSearch.setTextColor(Color.DKGRAY)



        closeButton.setOnClickListener {
            txtSearch.setText("")

        }

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                callSearch(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                //callSearch(newText);
                Toast.makeText(applicationContext, newText, Toast.LENGTH_SHORT).show()
                return false
            }

            fun callSearch(query: String) {
                //Do searching in submit event of keyboard
                Toast.makeText(applicationContext, "Submit", Toast.LENGTH_SHORT).show();

                Log.i("query", "" + query)

            }

        })

    }


    //toolbar ripple search
    //circular reveal
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    fun circleReveal(viewID: Int, posFromRight: Int, containsOverflow: Boolean, isShow: Boolean) {
        val myView = findViewById<View>(viewID)

        var width = myView.width

        if (posFromRight > 0)
            width -= posFromRight * resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_material) - resources.getDimensionPixelSize(
                R.dimen.abc_action_button_min_width_material
            ) / 2

        if (containsOverflow)
            width -= resources.getDimensionPixelSize(R.dimen.abc_action_button_min_width_overflow_material)

        val cx = width
        val cy = myView.height / 2

        val anim: Animator
        if (isShow)
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, 0f, width.toFloat())
        else
            anim = ViewAnimationUtils.createCircularReveal(myView, cx, cy, width.toFloat(), 0f)

        anim.duration = 220.toLong()

        // make the view invisible when the animation is done
        anim.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                if (!isShow) {
                    super.onAnimationEnd(animation)
                    myView.visibility = View.INVISIBLE
                }
            }
        })

        // make the view visible and start the animation
        if (isShow)
            myView.visibility = View.VISIBLE

        // start the animation
        anim.start()


    }


    override fun onBackPressed() {

        if (toolbarSearch?.visibility == View.VISIBLE) {
            itemSearch?.collapseActionView()

        } else
            finish()

    }

}
