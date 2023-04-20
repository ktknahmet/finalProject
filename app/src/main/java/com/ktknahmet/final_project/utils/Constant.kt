package com.ktknahmet.final_project.utils

import android.graphics.Color
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.model.ProfileDetails


object Constant {
    const val DISABLE = 0
    const val ENABLE = 1
    const val LIGHT = 0
    const val DARK = 1
    const val SINGLE_TAP_UP = 1203
    const val DOUBLE_TAP = 1204
    const val LONG_PRESS = 1205
    const val ODENDI = "Ödendi"
    const val ODENECEK ="Ödenecek"
    const val TAKSITLIODEME="Taksitli Ödeme"
    const val BORC = "Borç"
    const val ALACAK ="Alacak"
    const val HEPSI ="Hepsi"

    const val TLICON = "\u20BA"
    const val EURICON = "\u20AC"
    const val USDICON = "\u0024"
    const val POUNDICON ="\u00a3"

    const val CHANNEL_ID = "channelID"
    const val CHANNEL_NAME = "channelName"
    const val NOTIF_ID = 0
    val BORCTIP = arrayOf("Elektrik Faturası",
        "Su Faturası",
        "Doğalgaz",
        "Kurum Borcu",
        "İnternet",
        "Alışveriş",
        "Aidat",
        "Kredi Borcu",
        "Mutfak",
        "Eğitim/Kitap",
        "Kişisel Bakım",
        "Sigorta",
        "Seyahat",
        "Kira",
        "Diğer")

    val ODEMETIPI = arrayOf(
        ODENDI,
        ODENECEK,
        TAKSITLIODEME,
        BORC,
        ALACAK
    )

    val LISTCOLOR = intArrayOf(
        Color.rgb(32, 161, 218),
        Color.rgb(217, 125, 33),
        Color.rgb(102, 120, 128),
        Color.rgb(217, 87, 87),
        Color.rgb(23, 153, 110),
        Color.rgb(51, 87, 102),
        Color.rgb(217, 155, 33)
    )


    val PROFILE_TYPE = arrayOf(
        ProfileDetails(R.drawable.iv_tahsilat,"Harcamalarım"),
        ProfileDetails(R.drawable.iv_notification,"Yaklaşan Ödemelerim"),
        ProfileDetails(R.drawable.iv_logs,"Bireysel Ödeme"),
        ProfileDetails(R.drawable.iv_kullanicilar,"Arkadaş Ekle"),
        ProfileDetails(R.drawable.iv_pozisyon,"Arkadaşlarım"),
        ProfileDetails(R.drawable.iv_report,"Grafikler"),
        ProfileDetails(R.drawable.iv_pdf,"Pdflerim"),
        ProfileDetails(R.drawable.ic_borc_durum,"Toplu Borç Durum")
    )
}
