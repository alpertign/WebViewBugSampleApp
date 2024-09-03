package com.example.webviewsampleapp

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.viewinterop.AndroidView
import com.example.webviewsampleapp.ui.theme.WebViewSampleAppTheme
import kotlin.math.roundToInt
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WebViewSampleAppTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    val lazyListState = rememberLazyListState()
                    LazyColumn(
                        modifier = Modifier.padding(innerPadding),
                        state = lazyListState
                    ) {
                        items(50) {
                            Text(text = "Item $it", modifier = Modifier.fillMaxWidth())
                        }
                        item {
                            WebViewWrapper(webContent = Data.customWebContent, modifier = Modifier.fillMaxWidth())
                        }
                        item {
                            Text(text = "LAST Item ", modifier = Modifier.fillMaxWidth().padding(20.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun WebViewWrapper(
    webContent : String,
    modifier: Modifier = Modifier
) {

    var redirectUri by rememberSaveable { mutableStateOf<Uri?>(null) }

    AndroidView(
        modifier = modifier
            .alpha(0.99f)
        ,
        factory = { context ->
            WebView(context).apply {
                webViewClient = object : WebViewClient() {
                    override fun shouldOverrideUrlLoading(
                        view: WebView?,
                        request: WebResourceRequest?
                    ): Boolean {
                        request?.url?.let { uri ->
                            redirectUri = uri
                        }
                        return true
                    }
                }

                @SuppressLint("SetJavaScriptEnabled")
                this.settings.javaScriptEnabled = true
            }
        },
        update = { webView ->
            webView.loadDataWithBaseURL(null, webContent, "text/html; charset=utf-8", "UTF-8", null)
        }
    )
}
