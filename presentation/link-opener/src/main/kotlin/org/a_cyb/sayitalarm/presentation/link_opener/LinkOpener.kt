/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.link_opener

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import android.widget.Toast

class LinkOpener(private val context: Context) : LinkOpenerContract {
    /**
     * Do not use this function directly with links provided by user input.
     * If this function is used with a dynamic link in the future, it must implement proper URL validation.
     */
    override fun openBrowserLink(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
            .addFlags(FLAG_ACTIVITY_NEW_TASK)

        tryAndCatchActivityNotFoundException { context.startActivity(intent) }
    }

    override fun openEmail(address: String, subject: String?) {
        val intent = Intent(Intent.ACTION_SENDTO)
            .setData(Uri.parse("mailto:"))
            .putExtra(Intent.EXTRA_EMAIL, arrayOf(address))
            .putExtra(Intent.EXTRA_SUBJECT, subject ?: "")
        val chooser = Intent.createChooser(intent, context.getString(R.string.activity_chooser_title_email))
            .addFlags(FLAG_ACTIVITY_NEW_TASK)

        tryAndCatchActivityNotFoundException { context.startActivity(chooser) }
    }

    override fun openGooglePlay() {
        val packageName: String = context.packageName
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
            .addFlags(FLAG_ACTIVITY_NEW_TASK)

        try {
            context.startActivity(intent)
        } catch (e: ActivityNotFoundException) {
            tryAndCatchActivityNotFoundException {
                openBrowserLink("https://play.google.com/store/apps/details?id=$packageName")
            }
        }
    }

    private fun tryAndCatchActivityNotFoundException(action: () -> Unit) {
        try {
            action()
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(context, context.getString(R.string.info_activity_not_found_exception), Toast.LENGTH_SHORT)
                .show()
        }
    }
}