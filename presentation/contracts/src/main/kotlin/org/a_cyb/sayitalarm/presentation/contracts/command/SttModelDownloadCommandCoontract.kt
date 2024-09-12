/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.contracts.command

fun interface DownloadRecognizerModel : CommandContract.CommandReceiver {
    fun downloadRecognizerModel()
}

data object DownloadRecognizerModelCommand :
    CommandContract.Command<DownloadRecognizerModel> {
    override fun execute(receiver: DownloadRecognizerModel) {
        receiver.downloadRecognizerModel()
    }
}
