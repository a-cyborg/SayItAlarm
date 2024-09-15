/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.toArgb
import org.a_cyb.sayitalarm.design_system.token.Color
import org.a_cyb.sayitalarm.navigation.SiaNavHost

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        setContent {
            when (isSystemInDarkTheme()) {
                true -> Color.useDarkTheme()
                false -> Color.useLightTheme()
            }
            window.statusBarColor = Color.surface.standard.toArgb()
            window.navigationBarColor = Color.surface.standard.toArgb()

            Surface {
                SiaNavHost()
            }
        }
    }
}
