/*
 * Project: Shopme App
 * Author: Boys.mtv@gmail.com
 * File: HelpViewModel.kt
 *
 * Last modified by Dedy Wijaya on 22/02/26 10.26
 */

package com.mtv.app.shopme.feature.customer.presentation

import com.mtv.app.core.provider.based.BaseViewModel
import com.mtv.app.shopme.common.base.UiOwner
import com.mtv.app.shopme.feature.customer.contract.HelpDataListener
import com.mtv.app.shopme.feature.customer.contract.HelpFaq
import com.mtv.app.shopme.feature.customer.contract.HelpStateListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow

@HiltViewModel
class HelpViewModel @Inject constructor() :
    BaseViewModel(),
    UiOwner<HelpStateListener, HelpDataListener> {

    override val uiState = MutableStateFlow(HelpStateListener())

    override val uiData = MutableStateFlow(
        HelpDataListener(
            faq = listOf(
                HelpFaq(
                    "Bagaimana cara melacak pesanan?",
                    "Masuk ke menu Pesanan Saya → pilih pesanan → lihat status pengiriman."
                ),
                HelpFaq(
                    "Metode pembayaran apa saja?",
                    "Transfer bank, e-wallet, dan COD tersedia."
                ),
                HelpFaq(
                    "Bagaimana menghubungi support?",
                    "Gunakan menu Hubungi Kami atau Live Chat."
                )
            )
        )
    )

    fun onToggleFaq(target: HelpFaq) {
        val current = uiData.value

        uiData.value = current.copy(
            faq = current.faq.map {
                when {
                    it == target -> it.copy(expanded = !it.expanded)
                    else -> it.copy(expanded = false)
                }
            }
        )
    }
}