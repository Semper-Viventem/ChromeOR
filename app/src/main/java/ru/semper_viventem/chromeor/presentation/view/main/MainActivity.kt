package ru.semper_viventem.chromeor.presentation.view.main

import android.content.ActivityNotFoundException
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
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.pawegio.kandroid.find
import ru.semper_viventem.chromeor.R
import ru.semper_viventem.chromeor.model.LoginEntity
import ru.semper_viventem.chromeor.presentation.dialog.DialogManager
import ru.semper_viventem.chromeor.presentation.presenter.MainPresenter
import ru.semper_viventem.chromeor.presentation.view.about.AboutActivity
import ru.semper_viventem.chromeor.presentation.view.main.adapter.LoginListAdapter
import ru.semper_viventem.chromeor.util.AnalyticsApplication

class MainActivity : MvpAppCompatActivity(), MainView {

    companion object {
        lateinit var mTracker: Tracker
    }

    lateinit var mAdapter: LoginListAdapter
    lateinit var mDialogManager: DialogManager

    @InjectPresenter
    lateinit var mMainPresenter: MainPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        mDialogManager = DialogManager(this)

        val selectListener = object : LoginListAdapter.SelectLoginModelListener {

            override fun onLoginModelSelected(loginEntity: LoginEntity) {
                mDialogManager.showLoginDetails(loginEntity)
            }

            override fun onShareButtonClicked(loginEntity: LoginEntity) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                val textToSend =
                        "action_url: ${loginEntity.actionUrl}\n" +
                        "origin_url: ${loginEntity.originUrl}\n" +
                        "username: ${loginEntity.usernameValue}\n" +
                        "password: ${loginEntity.passwordValue}"
                intent.putExtra(Intent.EXTRA_TEXT, textToSend)

                try {
                    startActivity(Intent.createChooser(intent, "Share"))
                } catch (ex: ActivityNotFoundException) {
                    //TODO обработка ошибок
                    ex.printStackTrace()
                }
            }
        }

        mAdapter = LoginListAdapter(selectListener)
        find<RecyclerView>(R.id.vRecyclerView).adapter = mAdapter
        find<RecyclerView>(R.id.vRecyclerView).layoutManager = LinearLayoutManager(this)

        //google analytics
        val application = application as AnalyticsApplication
        mTracker = application.defaultTracker
    }

    override fun onResume() {
        super.onResume()

        mTracker.setScreenName(getString(R.string.tracker_activity_title))
        mTracker.send(HitBuilders.ScreenViewBuilder().build())


        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener {
            mMainPresenter.loadDatabase(application)
        }
    }


    override fun onDatabaseLoaded(passList: List<LoginEntity>) {
        mAdapter.setData(passList)
        mDialogManager.hideProgressDialog()
        find<ImageView>(R.id.vImageView).visibility = View.GONE

        mTracker.send(HitBuilders.EventBuilder()
                .setCategory(getString(R.string.tracker_onComplite_title))
                .setAction(getString(R.string.tracker_onComplite))
                .build())
    }

    override fun onBeginLoadingDB() {
        mDialogManager.showProgressDialog(getString(R.string.login), getString(R.string.get_database))
    }

    override fun onErrorLoadingDB() {
        mDialogManager.hideProgressDialog()
        mDialogManager.showInformationDialog(getString(R.string.oh_no), getString(R.string.unknow_error))

        mTracker.send(HitBuilders.EventBuilder()
                .setCategory(getString(R.string.tracker_onError_title))
                .setAction(getString(R.string.tracker_onError))
                .build())
    }

    override fun onErrorCopyrateDB() {
        mDialogManager.hideProgressDialog()
        mDialogManager.showInformationDialog(getString(R.string.oh_no), getString(R.string.have_root))

        mTracker.send(HitBuilders.EventBuilder()
                .setCategory(getString(R.string.tracker_onError_title))
                .setAction(getString(R.string.tracker_not_root))
                .build())
    }


    /********************** MENU ***************************/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val myActionMenuItem = menu.findItem(R.id.action_search)
        val searchView = myActionMenuItem.actionView as SearchView

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query == null) return true

                mAdapter.setSearchQuery(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText == null) return true

                mAdapter.setSearchQuery(newText)
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
