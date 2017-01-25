package ru.semper_viventem.chromeor.view.activity

import android.content.Intent
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import com.google.android.gms.analytics.HitBuilders
import com.google.android.gms.analytics.Tracker
import com.pawegio.kandroid.find
import ru.semper_viventem.chromeor.R
import ru.semper_viventem.chromeor.domain.CopyrateRootManager
import ru.semper_viventem.chromeor.domain.GetLoginFromDB
import ru.semper_viventem.chromeor.model.LoginEntity
import ru.semper_viventem.chromeor.util.AnalyticsApplication
import ru.semper_viventem.chromeor.view.adapter.LoginListAdapter
import ru.semper_viventem.chromeor.view.dialog.DialogManager
import rx.lang.kotlin.subscriber

class MainActivity : AppCompatActivity() {

    companion object {
        lateinit var mTracker: Tracker
    }

    interface SelectLoginModelListener {
        fun onLoginModelSelected(loginEntity: LoginEntity)
        fun onShareButtonClicked(loginEntity: LoginEntity)
    }

    lateinit var mAdapter: LoginListAdapter
    lateinit var mDialogManager: DialogManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById(R.id.toolbar) as Toolbar
        setSupportActionBar(toolbar)

        val fab = findViewById(R.id.fab) as FloatingActionButton
        fab.setOnClickListener { searchCP() }

        mDialogManager = DialogManager(this)

        val selectListener = object : SelectLoginModelListener {

            override fun onLoginModelSelected(loginEntity: LoginEntity) {
                mDialogManager.showLoginDetails(loginEntity)
            }

            override fun onShareButtonClicked(loginEntity: LoginEntity) {
                val intent = Intent(Intent.ACTION_SEND)
                intent.type = "text/plain"
                val textToSend = "action_url: ${loginEntity.actionUrl}\n" +
                        "origin_url: ${loginEntity.originUrl}\n" +
                        "username: ${loginEntity.usernameValue}\n" +
                        "password: ${loginEntity.passwordValue}"
                intent.putExtra(Intent.EXTRA_TEXT, textToSend)

                try {
                    startActivity(Intent.createChooser(intent, "Share"))
                } catch (ex: android.content.ActivityNotFoundException) {
                    //TODO обработка ошибок
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

        mTracker.setScreenName("MainActivity")
        mTracker.send(HitBuilders.ScreenViewBuilder().build())

    }

    fun searchCP() {

        mDialogManager.showProgressDialog(getString(R.string.login), getString(R.string.get_database))
        CopyrateRootManager(this).exequte(subscriber<Int>().onNext { exitCode ->
            readDB()
            mDialogManager.hideProgressDialog()
        }.onError {
            mDialogManager.hideProgressDialog()
            mDialogManager.showInformationDialog(getString(R.string.oh_no), getString(R.string.unknow_error))
        })
    }

    private fun readDB() {
        GetLoginFromDB(this).exequte(subscriber<List<LoginEntity>>().onNext { loginEntityList ->
            find<ImageView>(R.id.vImageView).visibility = View.GONE
            mAdapter.setData(loginEntityList)
        }.onError {
            mDialogManager.showInformationDialog(getString(R.string.oh_no), getString(R.string.unknow_error))
        })

        mTracker.send(HitBuilders.EventBuilder()
                .setCategory("getPass")
                .setAction("getting password list")
                .build())
    }


    /********************** MENU ***************************/
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        val id = item.itemId


        if (id == R.id.action_settings) {
            val intent = Intent(this, AboutActivity::class.java)
            startActivity(intent)
        }

        return super.onOptionsItemSelected(item)
    }



}
