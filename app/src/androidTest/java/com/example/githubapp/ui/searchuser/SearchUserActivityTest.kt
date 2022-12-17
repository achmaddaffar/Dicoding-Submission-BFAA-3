package com.example.githubapp.ui.searchuser

import androidx.test.core.app.ActivityScenario
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4ClassRunner::class)
class SearchUserActivityTest {
    private val queryText = "damieroliverroot"

    @Before
    fun setup() {
        ActivityScenario.launch(SearchUserActivity::class.java)
    }
}