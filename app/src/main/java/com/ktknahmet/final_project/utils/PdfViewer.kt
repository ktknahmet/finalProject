package com.ktknahmet.final_project.utils

import android.app.Activity
import android.content.Context
import android.os.Environment
import android.view.Gravity.CENTER_HORIZONTAL
import com.itextpdf.text.*
import com.itextpdf.text.pdf.PdfPCell
import com.itextpdf.text.pdf.PdfPTable
import com.itextpdf.text.pdf.PdfWriter
import com.ktknahmet.final_project.model.AddPayment
import com.ktknahmet.final_project.utils.Constant.ALACAK
import com.ktknahmet.final_project.utils.Constant.BORC
import com.ktknahmet.final_project.utils.Constant.ODENDI
import com.ktknahmet.final_project.utils.Constant.ODENECEK
import com.ktknahmet.final_project.utils.Constant.TAKSITLIODEME
import com.ktknahmet.final_project.utils.Constant.TLICON
import com.ktknahmet.final_project.utils.Constant.sdf2
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*



fun createPdf(list: ArrayList<AddPayment>, context: Context) {
    val mDoc = Document()
    val mFileName =
        SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(System.currentTimeMillis())
    val mPdfPath = Environment.getExternalStorageDirectory().toString() + "/" + list[0].EMAIL+ mFileName + ".pdf"
    try {
        PdfWriter.getInstance(mDoc, FileOutputStream(mPdfPath))
        mDoc.open()

        //pdfin genişliğini ayarlar
        val width = floatArrayOf(300f,300f)
        val table = PdfPTable(width)
        table.horizontalAlignment = CENTER_HORIZONTAL


        val cell0 =PdfPCell(Phrase(""))
        cell0.borderColor = BaseColor.WHITE
        cell0.minimumHeight = 5f
        var cell1: PdfPCell
        var cell2: PdfPCell
        var cell3: PdfPCell
        var cell4: PdfPCell
        var cell5: PdfPCell
        var cell6: PdfPCell
        var cell7: PdfPCell
        var cell8: PdfPCell


        for (i in list.indices) {
            when (list[i].ODEMETIP) {
                ODENDI -> { cell0.backgroundColor = BaseColor.GREEN }
                ODENECEK -> { cell0.backgroundColor = BaseColor.ORANGE }
                TAKSITLIODEME -> { cell0.backgroundColor = BaseColor.GRAY }
                BORC -> { cell0.backgroundColor = BaseColor.RED }
                ALACAK -> { cell0.backgroundColor = BaseColor.BLUE }
            }

            cell1 = PdfPCell(Phrase("Tarih"))
            cell1.borderColor = BaseColor.WHITE
            table.addCell(cell1)

            val dateShow = Date(list[i].TARIH!!)
            cell2 = PdfPCell(Phrase(sdf2.format(dateShow)))
            cell2.borderColor = BaseColor.WHITE
            table.addCell(cell2)

            cell3 = PdfPCell(Phrase("Fatura Tipi"))
            cell3.borderColor = BaseColor.WHITE
            table.addCell(cell3)

            cell4 = PdfPCell(Phrase("${list[i].FATURATIP}"))
            cell4.borderColor = BaseColor.WHITE
            table.addCell(cell4)

            cell5 = PdfPCell(Phrase("Ödeme Tipi"))
            cell5.borderColor = BaseColor.WHITE
            table.addCell(cell5)

            cell6 = PdfPCell(Phrase("${list[i].ODEMETIP}"))
            cell6.borderColor = BaseColor.WHITE
            table.addCell(cell6)

            cell7 = PdfPCell(Phrase("Bütce"))
            cell7.borderColor = BaseColor.WHITE
            cell7.fixedHeight = 50f
            table.addCell(cell7)

            cell8 = PdfPCell(Phrase("${list[i].BUTCE} $TLICON"))
            cell8.borderColor = BaseColor.WHITE
            cell8.fixedHeight = 30f
            table.addCell(cell8)
        }

        mDoc.add(table)


        mDoc.close()
        ToastMessage.createColorToast(
            context as Activity,
            "Pdf Oluşturuldu",
            ToastMessage.TOAST_SUCCESS,
            ToastMessage.GRAVITY_TOP,
            ToastMessage.LONG_DURATION
        )


    } catch (e: Exception) {
        ToastMessage.createColorToast(
            context as Activity,
            e.message.toString(),
            ToastMessage.TOAST_ERROR,
            ToastMessage.GRAVITY_TOP,
            ToastMessage.LONG_DURATION
        )
    }

}


