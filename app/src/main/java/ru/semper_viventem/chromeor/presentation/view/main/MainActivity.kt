package ru.semper_viventem.chromeor.presentation.view.main

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.SearchView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.pawegio.kandroid.find
import ru.semper_viventem.chromeor.R
import ru.semper_viventem.chromeor.presentation.dialog.DialogManager
import ru.semper_viventem.chromeor.presentation.model.LoginEntity
import ru.semper_viventem.chromeor.presentation.presenter.MainPresenter
import ru.semper_viventem.chromeor.presentation.view.about.AboutActivity
import ru.semper_viventem.chromeor.presentation.view.main.adapter.LoginListAdapter

class MainActivity : MvpAppCompatActivity(), MainView {

    lateinit var mAdapter: LoginListAdapter
    lateinit var mDialogManager: DialogManager

    @InjectPresenter
    lateinit var mMainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            mMainPresenter.loadChromeData()
        }

        val fabYa = findViewById(R.id.fabYa) as FloatingActionButton
        fabYa.setOnClickListener {
            mMainPresenter.loadChromeBetaDataba()
        }

        mDialogManager = DialogManager(this)

        val selectListener = object : LoginListAdapter.SelectLoginModelListener {

            override fun onLoginModelSelected(loginEntity: LoginEntity) {
                mDialogManager.showLoginDetails(loginEntity)
            }

            override fun onShareButtonClicked(loginEntity: LoginEntity) {
                mMainPresenter.shareLoginData(loginEntity)
            }
        }

        mAdapter = LoginListAdapter(selectListener)
        find<RecyclerView>(R.id.vRecyclerView).adapter = mAdapter
        find<RecyclerView>(R.id.vRecyclerView).layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()
        mMainPresenter.trackerOpenActivity()
    }

    override fun onBeginLoadingDB() {
        mDialogManager.showProgressDialog(getString(R.string.login), getString(R.string.get_database))
    }

    override fun onDatabaseLoaded(passList: List<LoginEntity>) {

        mAdapter.setData(passList)
        mDialogManager.hideProgressDialog()
        if (!passList.isEmpty())
            find<ImageView>(R.id.vImageView).visibility = View.GONE

    }

    override fun onErrorLoadingDB() {
        mDialogManager.hideProgressDialog()
        mDialogManager.showInformationDialog(getString(R.string.oh_no), getString(R.string.unknow_error))

    }

    override fun onErrorCopyrateDB() {
        mDialogManager.hideProgressDialog()
        mDialogManager.showInformationDialog(getString(R.string.oh_no), getString(R.string.have_root))
    }


    /********************** MENU ***************************/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null) return true
                mMainPresenter.searchFromList(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null) return true
                mMainPresenter.searchFromList(newText)
                return false
            }

        })

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId

        if (id == R.id.action_settings) {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }



}
