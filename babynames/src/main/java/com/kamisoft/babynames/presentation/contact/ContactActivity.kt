package com.kamisoft.babynames.presentation.contact

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.Toolbar
import android.view.MenuItem
import com.kamisoft.babynames.BuildConfig
import com.kamisoft.babynames.R
import com.kamisoft.babynames.commons.extensions.gone
import com.kamisoft.babynames.tracking.TrackerConstants
import com.kamisoft.babynames.tracking.TrackerManager
import kotlinx.android.synthetic.main.activity_contact.*
import kotlinx.android.synthetic.main.toolbar.*

class ContactActivity : AppCompatActivity() {

    private val trackerManager by lazy { TrackerManager(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact)
        trackPage()
        initToolbar()
        initViews()
    }

    private fun initToolbar() {
        val toolbar = toolbar as Toolbar?
        setSupportActionBar(toolbar)

        supportActionBar?.title = null
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        appLogo.gone()
        toolbar?.setNavigationIcon(R.drawable.ic_close)
    }

    private fun initViews() {
        txtVersion.text = getString(R.string.app_version, BuildConfig.VERSION_NAME)

        txtEmail.setOnClickListener {
            trackEvent(TrackerConstants.Label.ContactScreen.EMAIL_SUPPORT)
            val email = getString(R.string.contact_email)
            val emailIntent = Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", email, null))
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.contact_email_subject))
            startActivity(Intent.createChooser(emailIntent, getString(R.string.contact_txt_dialog_email)))
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                finish()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun trackPage() {
        trackerManager.sendScreen(TrackerConstants.Section.CONTACT.value,
                TrackerConstants.Section.Contact.MAIN.value)
    }

    private fun trackEvent(label: TrackerConstants.Label.ContactScreen) {
        trackerManager.sendEvent(
                category = TrackerConstants.Section.ParentsSetup.MAIN.value,
                action = TrackerConstants.Action.CLICK.value,
                label = label.value)
    }

}
