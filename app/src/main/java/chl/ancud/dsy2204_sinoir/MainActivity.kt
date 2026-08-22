package chl.ancud.dsy2204_sinoir

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import chl.ancud.dsy2204_sinoir.navegacion.AppNavegacion
import chl.ancud.dsy2204_sinoir.ui.theme.DSY2204_SinOirTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DSY2204_SinOirTheme {
                AppNavegacion()
            }
        }
    }
}

