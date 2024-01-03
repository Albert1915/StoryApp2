package com.alz19.storyapp2.view.login

import androidx.test.espresso.Espresso
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.intent.Intents
import androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.alz19.storyapp2.JsonConverter
import com.alz19.storyapp2.R
import com.alz19.storyapp2.helper.utils.EspressoIdlingResource
import com.alz19.storyapp2.provider.config.ApiConfig
import com.alz19.storyapp2.view.main.MainActivity
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@MediumTest
class LoginActivityTest{

    private val mockWebServer = MockWebServer()
    private val dummyEmail = "mabaranjay@gmail.com"
    private val dummyPass = "123456789"

    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)

    @Before
    fun setUp() {
        mockWebServer.start(8080)
        ApiConfig.BASE_URL = "http://127.0.0.1:8080/"
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }

    @After
    fun tearDown() {
        mockWebServer.shutdown()
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }

    @Test
    fun loginSuccess(){
        Intents.init()
        Espresso.onView(withId(R.id.edit_login_email)).perform(ViewActions.replaceText(dummyEmail))
        Espresso.onView(withId(R.id.edit_login_password))
            .perform(ViewActions.replaceText(dummyPass))
        Espresso.onView(withId(R.id.button_login)).perform(ViewActions.click())
        val mockResponse = MockResponse()
            .setResponseCode(201)
            .setBody(JsonConverter.readStringFromFile("success_login.json"))
        mockWebServer.enqueue(mockResponse)

        Thread.sleep(1000)

        Intents.intended(hasComponent(MainActivity::class.java.name))
    }

    @Test
    fun loginFailed (){

        Espresso.onView(withId(R.id.edit_login_email)).perform(ViewActions.replaceText(dummyEmail))
        Espresso.onView(withId(R.id.edit_login_password))
            .perform(ViewActions.replaceText(dummyPass))
        Espresso.onView(withId(R.id.button_login)).perform(ViewActions.click())

        val mockResponse = MockResponse()
            .setResponseCode(401)
            .setBody(JsonConverter.readStringFromFile("failed_login.json"))
        mockWebServer.enqueue(mockResponse)

        Thread.sleep(1000)

        Espresso.onView(ViewMatchers.withText("Error"))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }
}