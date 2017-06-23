package ru.semper_viventem.chromeor.presentation.view.about

import android.os.Bundle
import com.arellomobile.mvp.MvpAppCompatActivity
import com.arellomobile.mvp.presenter.InjectPresenter
import ru.semper_viventem.chromeor.R
import ru.semper_viventem.chromeor.presentation.presenter.AboutPresenter

class AboutActivity : MvpAppCompatActivity(), AboutView {

    @InjectPresenter
    lateinit var mAboutPresenter: AboutPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        mAboutPresenter.trackerOpenActivity()
    }
}
