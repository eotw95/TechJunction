package com.example.techjunction.constants

import com.example.techjunction.network.QiitaApi

const val APP_NAME = "TechJunction"

const val QIITA = "Qiita"
const val ZENN = "Zenn"
const val HATENA = "Hatena"

internal val services = listOf(
    QIITA,
    ZENN,
    HATENA
)

const val CHANNEL_URL_HATENA = "https://b.hatena.ne.jp/hotentry/it.rss"
const val CHANNEL_URL_ZENN = "https://zenn.dev/topics/android/feed"
const val CHANNEL_URL_QIITA = QiitaApi.BASE_URL