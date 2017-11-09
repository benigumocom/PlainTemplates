package com.example.plaintemplates

import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class AdmobAdsActivity : AppCompatActivity() {
  private var mLevel: Int = 0
  private var mNextLevelButton: Button? = null
  private var mInterstitialAd: InterstitialAd? = null
  private var mLevelTextView: TextView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_admob_ads)

    // Create the next level button, which tries to show an interstitial when clicked.
    mNextLevelButton = findViewById<View>(R.id.next_level_button) as Button
    mNextLevelButton!!.isEnabled = false
    mNextLevelButton!!.setOnClickListener { showInterstitial() }

    // Create the text view to show the level number.
    mLevelTextView = findViewById<View>(R.id.level) as TextView
    mLevel = START_LEVEL

    // Create the InterstitialAd and set the adUnitId (defined in values/strings.xml).
    mInterstitialAd = newInterstitialAd()
    loadInterstitial()

    // Toasts the test ad message on the screen. Remove this after defining your own ad unit ID.
    Toast.makeText(this, TOAST_TEXT, Toast.LENGTH_LONG).show()
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    // Inflate the menu; this adds items to the action bar if it is present.
    menuInflater.inflate(R.menu.menu_admob_ads, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    // Handle action bar item clicks here. The action bar will
    // automatically handle clicks on the Home/Up button, so long
    // as you specify a parent activity in AndroidManifest.xml.
    val id = item.itemId

    return if (id == R.id.action_settings) {
      true
    } else super.onOptionsItemSelected(item)

  }

  private fun newInterstitialAd(): InterstitialAd {
    val interstitialAd = InterstitialAd(this)
    interstitialAd.setAdUnitId(getString(R.string.interstitial_ad_unit_id))
    interstitialAd.setAdListener(object : AdListener() {
      fun onAdLoaded() {
        mNextLevelButton!!.isEnabled = true
      }

      fun onAdFailedToLoad(errorCode: Int) {
        mNextLevelButton!!.isEnabled = true
      }

      fun onAdClosed() {
        // Proceed to the next level.
        goToNextLevel()
      }
    })
    return interstitialAd
  }

  private fun showInterstitial() {
    // Show the ad if it's ready. Otherwise toast and reload the ad.
    if (mInterstitialAd != null && mInterstitialAd!!.isLoaded()) {
      mInterstitialAd!!.show()
    } else {
      Toast.makeText(this, "Ad did not load", Toast.LENGTH_SHORT).show()
      goToNextLevel()
    }
  }

  private fun loadInterstitial() {
    // Disable the next level button and load the ad.
    mNextLevelButton!!.isEnabled = false
    val adRequest = AdRequest.Builder().setRequestAgent("android_studio:ad_template").build()
    mInterstitialAd!!.loadAd(adRequest)
  }

  private fun goToNextLevel() {
    // Show the next level and reload the ad to prepare for the level after.
    mLevelTextView!!.text = "Level " + ++mLevel
    mInterstitialAd = newInterstitialAd()
    loadInterstitial()
  }

  companion object {
    // Remove the below line after defining your own ad unit ID.
    private val TOAST_TEXT = "Test ads are being shown. " + "To show live ads, replace the ad unit ID in res/values/strings.xml with your own ad unit ID."

    private val START_LEVEL = 1
  }
}
