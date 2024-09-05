/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.design_system.atom

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import androidx.compose.foundation.layout.size
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import org.a_cyb.sayitalarm.design_system.R
import org.a_cyb.sayitalarm.design_system.molecule.animateCircleBorder
import org.a_cyb.sayitalarm.design_system.token.Brush
import org.a_cyb.sayitalarm.design_system.token.Sizing

@Composable
fun TextButtonDelete(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleDangerMedium(text = stringResource(id = R.string.delete))
    }
}

@Composable
fun TextButtonDone(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.done))
    }
}

@Composable
fun TextButtonEdit(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.edit))
    }
}

@Composable
fun TextButtonSave(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.save))
    }
}

@Composable
fun TextButtonCancel(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleWarningMedium(text = stringResource(id = R.string.cancel))
    }
}

@Composable
fun TextButtonConfirm(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.confirm))
    }
}

@Composable
fun TextButtonClose(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.close))
    }
}

@Composable
fun TextButtonSnooze(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.snooze))
    }
}

@Composable
fun TextButtonRequestPermission(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.request_permission))
    }
}

@Composable
fun TextButtonCircleSayIt(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .size(Sizing.CircleButton.Large)
            .animateCircleBorder(Brush.sweepGradientRainbow)
    ) {
        TextTitleAttentionLarge(
            text = stringResource(id = R.string.say_it)
        )
    }
}

@Composable
fun TextButtonCircleStart(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .size(Sizing.CircleButton.Large)
            .animateCircleBorder(Brush.sweepGradientAttention)
    ) {
        TextTitleAttentionLarge(
            text = stringResource(id = R.string.start)
        )
    }
}

@Composable
fun TextButtonCircleTryAgain(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .size(Sizing.CircleButton.Large)
            .animateCircleBorder(Brush.sweepGradientDanger)
    ) {
        TextTitleWarningLarge(text = stringResource(id = R.string.try_again))
    }
}

@Composable
fun TextButtonCircleFinish(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .size(Sizing.CircleButton.Large)
            .animateCircleBorder(Brush.sweepGradientRainbow)
    ) {
        TextTitleAttentionLarge(
            text = stringResource(id = R.string.finish)
        )
    }
}

@Composable
fun TextButtonCircleExit(onClick: () -> Unit) {
    TextButton(
        onClick = onClick,
        modifier = Modifier
            .size(Sizing.CircleButton.Large)
            .animateCircleBorder(Brush.sweepGradientDanger)
    ) {
        TextTitleWarningLarge(
            text = stringResource(id = R.string.exit)
        )
    }
}

@Composable
fun TextButtonCopy(copyString: String) {
    val clipboardManager = LocalContext.current.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    val data = ClipData.newPlainText("sayItAlarmCopy", copyString)

    TextButton(onClick = { clipboardManager.setPrimaryClip(data) }) {
        TextBodyStandardSmallUnderline(text = stringResource(id = R.string.copy))
    }
}

@Composable
fun TextButtonEmail(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextBodySubtleMediumUnderline(text = stringResource(id = R.string.email))
    }
}

@Composable
fun TextButtonGooglePlay(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextBodySubtleMediumUnderline(text = stringResource(id = R.string.google_play))
    }
}

@Composable
fun TextButtonGitHub(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextBodySubtleMediumUnderline(text = stringResource(id = R.string.github))
    }
}

@Composable
fun TextButtonDownload(onClick: () -> Unit) {
    TextButton(onClick = onClick) {
        TextTitleAttentionMedium(text = stringResource(id = R.string.download))
    }
}

