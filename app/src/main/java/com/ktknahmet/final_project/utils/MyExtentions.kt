package com.ktknahmet.final_project.utils

import android.app.Activity
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBinding
import com.ktknahmet.final_project.utils.niceDialog.MyDialog
import com.ktknahmet.final_project.utils.niceDialog.ViewConvertListener
import com.crazylegend.kotlinextensions.fragments.show
import com.crazylegend.kotlinextensions.isNull
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.databinding.DialogConfirmationItemBinding
import com.ktknahmet.final_project.databinding.DialogDeleteItemBinding
import com.ktknahmet.final_project.sevices.NetworkResult
import com.ktknahmet.final_project.utils.ToastMessage.Companion.GRAVITY_TOP
import com.ktknahmet.final_project.utils.generalUtils.str
import com.ktknahmet.final_project.utils.niceDialog.BaseMyDialog
import com.ktknahmet.final_project.utils.sharedPreferences.MyPref
import com.skydoves.whatif.whatIfNotNull
import okhttp3.ResponseBody
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

val toast = ToastMessage
fun Fragment.showConfirmDialog(
    title: String,
    confirm: () -> Unit = {},
    cancel: () -> Unit = {},
    buttonPositiveText: String = str(R.string.tamam_caps),
    buttonNegativeText: String = str(R.string.iptal_et_caps),
    image :Int = R.drawable.iv_question_boy
) {
    MyDialog<DialogConfirmationItemBinding>()
        .setListener(object : ViewConvertListener<DialogConfirmationItemBinding> {
            override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): DialogConfirmationItemBinding =
                DialogConfirmationItemBinding.inflate(inflater, container, false)

            override fun convertView(vb: DialogConfirmationItemBinding, dialog: BaseMyDialog<DialogConfirmationItemBinding>) {

                vb.tvTitle.text = title
                vb.btnOK.text = buttonPositiveText
                vb.btnCancel.text = buttonNegativeText
                vb.ivImage.setImageResource(image)
                vb.btnOK.setOnClickListener {
                    confirm()
                    dialog.dismiss()
                }
                vb.btnCancel.setOnClickListener {
                    cancel()
                    dialog.dismiss()
                }
            }
        })
        .show(this.requireActivity().supportFragmentManager)
}
fun Activity.showConfirmDialog(
    title: String,
    confirm: () -> Unit = {},
    cancel: () -> Unit = {},
    buttonPositiveText: String = str(R.string.tamam_caps),
    buttonNegativeText: String = str(R.string.iptal_et_caps),
    image :Int = R.drawable.iv_question_boy
) {
    MyDialog<DialogConfirmationItemBinding>()
        .setListener(object : ViewConvertListener<DialogConfirmationItemBinding> {
            override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): DialogConfirmationItemBinding =
                DialogConfirmationItemBinding.inflate(inflater, container, false)

            override fun convertView(vb: DialogConfirmationItemBinding, dialog: BaseMyDialog<DialogConfirmationItemBinding>) {

                vb.tvTitle.text = title
                vb.btnOK.text = buttonPositiveText
                vb.btnCancel.text = buttonNegativeText
                vb.ivImage.setImageResource(image)
                vb.btnOK.setOnClickListener {
                    confirm()
                    dialog.dismiss()
                }
                vb.btnCancel.setOnClickListener {
                    cancel()
                    dialog.dismiss()
                }
            }
        })


}

fun saveImage(imageUri: Uri?,context:Context,name:String){
    val pref = MainSharedPreferences(context, MyPref.bilgiler)

    val storage = Firebase.storage
    val reference = storage.reference
    val uid = UUID.randomUUID()

    val imageReference = reference.child("$name/$uid.jpg").putFile(imageUri!!)

    imageReference.addOnSuccessListener {
       pref.storeString(MyPref.photoUrl,imageUri.toString())

        toast.createColorToast(
            context as Activity,R.string.fotograf_eklendi.toString(),
            ToastMessage.TOAST_SUCCESS,GRAVITY_TOP,5
        )
    }.addOnFailureListener {
        toast.createColorToast(
            context as Activity, it.localizedMessage!!.toString(),
            ToastMessage.TOAST_ERROR,GRAVITY_TOP,5
        )
    }
}




fun Fragment.showDeleteDialog(
    title: String = str(R.string.question_delete),
    confirm: () -> Unit = {},
    cancel: () -> Unit = {},
    buttonPositiveText: String = str(R.string.sil_caps),
    buttonNegativeText: String = str(R.string.iptal_et_caps)
) {
    MyDialog<DialogDeleteItemBinding>()
        .setListener(object : ViewConvertListener<DialogDeleteItemBinding> {
            override fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): DialogDeleteItemBinding =
                DialogDeleteItemBinding.inflate(inflater, container, false)

            override fun convertView(vb: DialogDeleteItemBinding, dialog: BaseMyDialog<DialogDeleteItemBinding>) {
                vb.tvTitle.text = title
                vb.btnOK.text = buttonPositiveText
                vb.btnCancel.text = buttonNegativeText
                vb.btnOK.setOnClickListener {
                    confirm()
                    dialog.dismiss()
                }
                vb.btnCancel.setOnClickListener {
                    cancel()
                    dialog.dismiss()
                }
            }
        })
        .show(this.requireActivity().supportFragmentManager)
}



fun Fragment.navigate(id: Int, args: Bundle? = null) {
    this.findNavController().navigate(id, args)
}

fun Fragment.popBackStack() {
    this.findNavController().popBackStack()
}

fun CompoundButton.onCheck(onChecked: (Boolean) -> Unit) {
    setOnCheckedChangeListener { _, isChecked ->
        onChecked(isChecked)
    }
}

fun <VB : ViewBinding> Fragment.showDialog(
    view: ViewConvertListener<VB>
) {
    MyDialog<VB>()
        .setListener(view)
        .show(this.requireActivity().supportFragmentManager)
}

fun <T> SingleLiveEvent<NetworkResult<T>>.myObserve(
    viewLifecycleOwner: LifecycleOwner,
    success: (T) -> Unit,
    empty: () -> Unit = {},
    error: (msg: String) -> Unit
) {
    this.observe(
        viewLifecycleOwner
    ) {
        it?.let {
            when (it) {
                is NetworkResult.Success -> {
                    success(it.data)
                }
                is NetworkResult.Empty -> {
                    empty()
                }
                is NetworkResult.Error -> {
                    error(it.message)
                }
            }
        }
    }
}

fun <T> MutableLiveData<NetworkResult<T>>.myObserve(
    viewLifecycleOwner: LifecycleOwner,
    success: (T) -> Unit,
    empty: () -> Unit = {},
    error: (msg: String) -> Unit
) {
    this.observe(
        viewLifecycleOwner
    ) {
        it?.let {
            when (it) {
                is NetworkResult.Success -> success(it.data)
                is NetworkResult.Empty -> empty()
                is NetworkResult.Error -> error(it.message)
            }
        }
    }
}

fun MutableLiveData<Boolean>.observeProgress(
    viewLifecycleOwner: LifecycleOwner,
    pgBar: View
) {
    this.observe(
        viewLifecycleOwner
    ) {
        pgBar.visibility = if (it) View.VISIBLE else View.INVISIBLE
    }
}

fun <T> Response<T>.getData(dataLoading: MutableLiveData<Boolean>, result: (NetworkResult<T>) -> Unit) {
    try {
        dataLoading.value = false
        if (this.isSuccessful) {
            val data = this.body()
            data.whatIfNotNull {
                when (it) {
                    is List<*> -> if (it.isEmpty()) result(NetworkResult.Empty()) else result(NetworkResult.Success(it))
                    is ArrayList<*> -> if (it.isEmpty()) {
                        result(NetworkResult.Empty())
                    } else {
                        result(NetworkResult.Success(it))
                    }
                    is String -> if (it.isEmpty()) {
                        result(NetworkResult.Empty())
                    } else {
                        result(NetworkResult.Success(it))
                    }
                    is Int -> result(NetworkResult.Success(it))
                    else -> if (it.isNull) {
                        result(NetworkResult.Empty())
                    } else {
                        result(NetworkResult.Success(it))
                    }
                }
            }
        } else {
            var message = this.message()
            this.errorBody().whatIfNotNull {
                message = it.string()
            }

        }
    } catch (e: Exception) {
        Log.e("gelen hata ", e.stackTraceToString())
        Log.e("gelen hata ", e.toString())
        dataLoading.value = false

    }
}

fun Response<ResponseBody>.postData(
    dataLoading: MutableLiveData<Boolean>,
    successText: String,
    result: (NetworkResult<String>) -> Unit
) {
    try {
        dataLoading.value = false
        println("postData xxxx")
        println(this)
        if (this.isSuccessful) {
            val statusCode: Int = this.code()
            statusCode.whatIfNotNull {
                if (statusCode == 200) {
                    println("successText")
                    println(successText)
                    result(NetworkResult.Success(successText))
                } else {
                    result(NetworkResult.Error("${Errors.HayAksi} Hata Kodu: $statusCode"))
                }
            }
        } else {
            var message = this.message()
            this.errorBody().whatIfNotNull {
                message = it.string()
            }

        }
    } catch (e: Exception) {
        dataLoading.value = false
        println("hataaa :${e}")

    }
}
