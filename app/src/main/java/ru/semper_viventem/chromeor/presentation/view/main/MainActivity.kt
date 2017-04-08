package ru.semper_viventem.chromeor.presentation.view.main

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.SearchView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import com.pawegio.kandroid.startActivity
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
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
        setSupportActionBar(vToolBar)

        vFabChrome.setOnClickListener {
            mMainPresenter.loadChromeLoginList()
        }

        vFabChromeBeta.setOnClickListener {
            mMainPresenter.loadChromeBetaLoginList()
        }

        mDialogManager = DialogManager(this)

        mAdapter = LoginListAdapter(object : LoginListAdapter.SelectLoginModelListener {

            override fun onItemSelected(loginEntity: LoginEntity) {
                mDialogManager.showLoginDetails(loginEntity)
            }

            override fun onShareButtonClicked(loginEntity: LoginEntity) {
                mMainPresenter.shareLoginData(loginEntity)
            }
        })

        vRecyclerView.adapter = mAdapter
        vRecyclerView.layoutManager = LinearLayoutManager(this)
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

        if (passList.isNotEmpty())
            vRecyclerView.visibility = View.GONE

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
            startActivity<AboutActivity>()
        }

        return super.onOptionsItemSelected(item)
    }

}
