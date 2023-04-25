package com.ktknahmet.final_project.ui.pdf

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import com.crazylegend.kotlinextensions.views.onClick
import com.crazylegend.kotlinextensions.views.visible
import com.ktknahmet.final_project.databinding.FragmentPdfViewerBinding
import com.ktknahmet.final_project.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class PdfViewer:BaseFragment<FragmentPdfViewerBinding>(FragmentPdfViewerBinding::inflate){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        openPdfFile()
        binding.openPdf.onClick {
            openPdfFile()
        }

    }
    private fun openPdfFile(){
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
            type = "application/pdf"
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startActivityForResult(intent, 1001)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == 1001 && resultCode == Activity.RESULT_OK) {
            data?.data?.also { uri ->
                binding.pdfView.fromUri(uri).load()
            }
        }else{
               toastWarning("Hiçbir PDF seçilmedi.Pdf açmak için ikona tıklayınız")
               binding.openPdf.visible()
        }
    }

}