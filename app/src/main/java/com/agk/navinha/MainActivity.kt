package com.agk.navinha

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import android.view.View
import android.webkit.JavascriptInterface
import android.webkit.WebSettings
import android.media.MediaPlayer


import android.webkit.WebView
import android.webkit.WebViewClient
import com.agk.navinha.ui.theme.NavinhaTheme
import com.agk.navinha.PontuacaoDbHelper

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.MobileAds


class MainActivity : ComponentActivity() {

    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var mediaPlayer2: MediaPlayer
    private lateinit var webView: WebView
    lateinit var adView: AdView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        val webView = findViewById<WebView>(R.id.webview)
        val webSettings: WebSettings = webView.settings
        webSettings.javaScriptEnabled = true
        webView.webViewClient = WebViewClient()

        // Habilitar JavaScript, se necessário
        webView.settings.javaScriptEnabled = true



        //fazer ficar em tela cheia
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )

        // Inicializar o AdMob
        MobileAds.initialize(this){}
        // Banner AdView setup
        adView = findViewById(R.id.adView)
        val adRequest = AdRequest.Builder().build()
        // Carregar o anúncio, mas mantê-lo inicialmente invisível
        adView.loadAd(adRequest)
        adView.visibility = View.GONE


        // Adicionando a interface JavaScript para comunicação com Android
        webView.addJavascriptInterface(object {
            @JavascriptInterface
            fun savePontuacao(pontuacao: Int) {
                val dbHelper = PontuacaoDbHelper(this@MainActivity)
                dbHelper.addPontuacao(pontuacao)
            }

            @JavascriptInterface
            fun getHighestPontuacao(): Int {
                val dbHelper = PontuacaoDbHelper(this@MainActivity)
                return dbHelper.getHighestPontuacao()
            }

            @JavascriptInterface
            fun playExplosion() {
                mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.explosion2)
                mediaPlayer.start()
            }

            @JavascriptInterface
            fun playMoving() {
                mediaPlayer2 = MediaPlayer.create(this@MainActivity, R.raw.moving2)
                mediaPlayer2.start()
            }

            // Método para exibir o banner
            @JavascriptInterface
            fun showBanner() {
                runOnUiThread {
                    adView.visibility = View.VISIBLE
                }
            }

            // Método para esconder o banner
            @JavascriptInterface
            fun hideBanner() {
                runOnUiThread {
                    adView.visibility = View.GONE
                }
            }
        }, "Android")
        WebView.setWebContentsDebuggingEnabled(true)
        webView.loadUrl("file:///android_asset/index.html")
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::mediaPlayer.isInitialized) {
            mediaPlayer.release()
        }
        if (::mediaPlayer2.isInitialized) {
            mediaPlayer2.release()
        }
    }


}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    NavinhaTheme {
        Greeting("Android")
    }
}