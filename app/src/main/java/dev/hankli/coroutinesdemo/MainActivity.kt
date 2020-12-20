package dev.hankli.coroutinesdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "Main"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        view_button.setOnClickListener {
            view_text.text = null
            //doSyncWay()
            //doAsyncWay()
            doCoroutineWay()
        }
    }

    private fun doSyncWay() {
        showProgress()
        val result = slowFetch()
        show(result)
        hideProgress()
    }

    private fun doAsyncWay() {
        showProgress()
        slowFetchUsingCallback { result ->
            show(result)
            hideProgress()
        }
    }

    private fun doCoroutineWay() {
        lifecycleScope.launch {
            showProgress()
            val result = withContext(IO) { slowFetchBySuspend() }
            show(result)
            hideProgress()
        }
    }

    private fun slowFetch(): String {
        // Simulating a long running process
        Thread.sleep(1000)
        return "Important Data"
    }

    private fun slowFetchUsingCallback(callback: (result: String) -> Unit) {
        Thread {
            // Simulating a long running process
            Thread.sleep(1000)
            runOnUiThread {
                callback("Important Data")
            }
        }.start()
    }

    private suspend fun slowFetchBySuspend(): String {
        // Simulating a long running process
        Log.i(TAG, "Thread name: ${Thread.currentThread().name}")
        delay(1000)
        return "Important Data"
    }

    private fun show(text: String) {
        view_text.text = text
    }

    private fun showProgress() {
        view_progress.isVisible = true
    }

    private fun hideProgress() {
        view_progress.isVisible = false
    }
}
