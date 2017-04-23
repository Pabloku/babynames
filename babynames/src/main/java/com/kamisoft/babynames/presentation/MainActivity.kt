package com.kamisoft.babynames.presentation

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.kamisoft.babyname.R
import com.kamisoft.babynames.presentation.chooseGenre.ChooseGenreFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val transition = supportFragmentManager.beginTransaction()
        transition.replace(R.id.contentView, ChooseGenreFragment.createInstance())
        transition.commit()
    }
}
