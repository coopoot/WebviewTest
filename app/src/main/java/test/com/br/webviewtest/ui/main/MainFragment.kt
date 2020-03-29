package test.com.br.webviewtest.ui.main

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.JavascriptInterface
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.main_fragment.*
import test.com.br.webviewtest.R

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
        private const val URL = "file:///android_asset/webviewtoandroid.html"
        private const val OBJ = "object"
        private const val JSFunction = "javascript: window.androidObj.textToAndroid = function(message) { object.setText(message) }"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        WebView.setWebContentsDebuggingEnabled(true)
        webView.settings.javaScriptEnabled = true
        webView.addJavascriptInterface(Interface(), OBJ)
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView, url: String) {
                injectJavaScriptFunction()
            }
        }

        webView!!.loadUrl(URL)

        btnSend.setOnClickListener {
            webView.evaluateJavascript(
                "javascript: updateTextToWeb(\"" + edText.text + "\")", null
            )
        }
    }

    private fun injectJavaScriptFunction() {
        webView.loadUrl(JSFunction)
    }

    private inner class Interface {
        @JavascriptInterface
        fun setText(text: String) {
            tvText.text = text
        }
    }
}
