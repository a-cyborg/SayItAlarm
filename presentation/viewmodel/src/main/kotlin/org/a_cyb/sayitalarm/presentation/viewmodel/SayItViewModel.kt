/*
 * Copyright (c) 2024 Mima Kang / All rights reserved.
 *
 * Use of this source code is governed by Apache v2.0
 */

package org.a_cyb.sayitalarm.presentation.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.Count
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.ProgressStatus
import org.a_cyb.sayitalarm.domain.interactor.InteractorContract.SayItInteractor.SayItState
import org.a_cyb.sayitalarm.entity.SayIt
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUIInfo
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUiState
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUiState.Finished
import org.a_cyb.sayitalarm.presentation.contracts.SayItContract.SayItUiState.Initial
import org.a_cyb.sayitalarm.presentation.contracts.command.CommandContract
import org.a_cyb.sayitalarm.util.sound_effect_player.SoundEffectPlayerContract

class SayItViewModel(
    private val sayItInteractor: SayItInteractor,
    private val soundEffectPlayer: SoundEffectPlayerContract,
) : SayItContract.SayItViewModel, ViewModel() {

    private val _state: MutableStateFlow<SayItUiState> = MutableStateFlow(Initial)
    override val state: StateFlow<SayItUiState> = _state.asStateFlow()

    init {
        sayItInteractor.sayItState
            .onEach(::handleInteractorState)
            .launchIn(scope)
    }

    override fun processScript() {
        sayItInteractor.startListening()
    }

    override fun finish() {
        soundEffectPlayer.stopPlayer()
        sayItInteractor.shutdown()
    }

    private fun handleInteractorState(state: SayItState) {
        when (state) {
            is SayItState.Initial -> handleInitialState()
            is SayItState.Ready -> handleReadyState()
            is SayItState.InProgress -> handleInProgressState(state.status, state.sayIt, state.count)
            is SayItState.Completed -> handleCompletedState()
            is SayItState.Error -> handleErrorState(state.error.name)
        }
    }

    private fun handleInitialState() {
        sayItInteractor.startSayIt(scope)
    }

    private fun handleReadyState() {
        sayItInteractor.startListening()
    }

    private fun handleInProgressState(status: ProgressStatus, sayIt: SayIt, count: Count) {
        val info = SayItUIInfo(sayIt.script, sayIt.transcript, count.current, count.total)

        when (status) {
            ProgressStatus.IN_PROGRESS -> {
                SayItUiState.Listening(info).update()
            }

            ProgressStatus.SUCCESS -> {
                soundEffectPlayer.playSuccessSoundEffect()
                SayItUiState.Success(info).update()
                sayItInteractor.startListening()
            }

            ProgressStatus.FAILED -> {
                soundEffectPlayer.playFailureSoundEffect()
                SayItUiState.Failed(info).update()
                sayItInteractor.startListening()
            }
        }
    }

    private fun handleCompletedState() {
        soundEffectPlayer.playSuccessSoundEffect()
        Finished.update()
    }

    private fun handleErrorState(message: String) {
        SayItUiState.Error(message).update()
    }

    private fun SayItUiState.update() {
        _state.update { this }
    }

    override fun <T : CommandContract.CommandReceiver> runCommand(command: CommandContract.Command<T>) {
        @Suppress("UNCHECKED_CAST")
        command.execute(this as T)
    }
}
