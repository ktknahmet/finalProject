package com.ktknahmet.final_project.utils

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

    val PROFILE_TYPE = arrayOf(
        ProfileDetails(R.drawable.iv_tahsilat,"Harcamalarım"),
        ProfileDetails(R.drawable.iv_notification,"Yaklaşan Ödemelerim"),
        ProfileDetails(R.drawable.iv_logs,"Bireysel Ödeme"),
        ProfileDetails(R.drawable.iv_kullanicilar,"Arkadaş Ekle"),
        ProfileDetails(R.drawable.iv_pozisyon,"Arkadaşlarım"),
        ProfileDetails(R.drawable.iv_report,"Grafikler"),
        ProfileDetails(R.drawable.iv_pdf,"Pdflerim")
    )
}
