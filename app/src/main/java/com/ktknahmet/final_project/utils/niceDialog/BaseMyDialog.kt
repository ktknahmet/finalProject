package com.ktknahmet.final_project.utils.niceDialog


import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.annotation.StyleRes
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.ktknahmet.final_project.R
import com.ktknahmet.final_project.ui.base.RoundedBottomSheetDialogFragment

@Suppress("unused")
abstract class BaseMyDialog<VB : ViewBinding> : RoundedBottomSheetDialogFragment() {
    companion object {
        private const val MARGIN = "margin"
        private const val WIDTH = "width"
        private const val HEIGHT = "height"
        private const val DIM = "dim_amount"
        private const val GRAVITY = "gravity"
        private const val CANCEL = "out_cancel"
        private const val THEME = "theme"
        private const val ANIM = "anim_style"

        private fun getScreenWidth(context: Context): Int =
            context.resources.displayMetrics.widthPixels

        private fun dp2px(context: Context, dpValue: Float): Int {
            val scale = context.resources.displayMetrics.density
            return (dpValue * scale + 0.5f).toInt()
        }
    }

    private lateinit var mHostContext: Context

    private var dismissCallback: DismissCallback? = null

    protected var dismissManually = true

    private var isPrepared = false
    private var isInVisible = false

    private var isDialogShowing = false

    fun isShowing(): Boolean = isDialogShowing

    private var margin = 20
    protected var width = 0
    protected var height = 0
    private var dimAmount = 0.5f
    protected var gravity = Gravity.BOTTOM
    private var outCancel = true

    @StyleRes
    protected var dialogTheme = R.style.NiceDialogStyle

    @StyleRes
    protected var animStyle = 0

    private var _binding: VB? = null

    abstract fun provideViewBinding(inflater: LayoutInflater, container: ViewGroup?): VB?

    abstract fun convertView(vb: VB, dialog: BaseMyDialog<VB>)

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mHostContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, dialogTheme)

        if (savedInstanceState != null) {
            margin = savedInstanceState.getInt(MARGIN)
            width = savedInstanceState.getInt(WIDTH)
            height = savedInstanceState.getInt(HEIGHT)
            dimAmount = savedInstanceState.getFloat(DIM)
            gravity = savedInstanceState.getInt(GRAVITY)
            outCancel = savedInstanceState.getBoolean(CANCEL)
            dialogTheme = savedInstanceState.getInt(THEME)
            animStyle = savedInstanceState.getInt(ANIM)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = provideViewBinding(inflater, container)
        if (_binding == null) throw IllegalArgumentException("ViewBinding MUST BE NULL")
        convertView(_binding!!, this)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (!isPrepared && userVisibleHint) {
            onDialogVisibleChange(true)
            isInVisible = true
        }
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        isPrepared = true
        if (isVisibleToUser) {
            onDialogVisibleChange(true)
            isInVisible = true
            return
        }
        if (isInVisible) {
            onDialogVisibleChange(false)
            isInVisible = false
        }
    }

    open fun onDialogVisibleChange(isVisible: Boolean) {
        isDialogShowing = isVisible
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        dismissCallback?.onDismiss(this, dismissManually)
    }

    /**
     * dialog dismissed by user
     */
    fun dismissManually(manually: Boolean) {
        dismissManually = manually
        super.dismissAllowingStateLoss()
    }

    override fun dismiss() {
        dismissAllowingStateLoss()
    }

    override fun dismissAllowingStateLoss() {
        dismissManually = true
        super.dismissAllowingStateLoss()
    }

    override fun onStart() {
        super.onStart()
        initParams()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(MARGIN, margin)
        outState.putInt(WIDTH, width)
        outState.putInt(HEIGHT, height)
        outState.putFloat(DIM, dimAmount)
        outState.putInt(GRAVITY, gravity)
        outState.putBoolean(CANCEL, outCancel)
        outState.putInt(THEME, theme)
        outState.putInt(ANIM, animStyle)
    }

    private fun initParams() {
        val nonNullDialog = dialog ?: return
        val window = nonNullDialog.window
        if (window != null) {
            val lp = window.attributes

            lp.dimAmount = dimAmount
            if (gravity != 0) {
                lp.gravity = gravity
            }

           /* when (width) {
                0 -> {
                    lp.width = getScreenWidth(mHostContext) - 2 * dp2px(mHostContext, margin.toFloat())
                }
                -1 -> {
                    lp.width = WindowManager.LayoutParams.WRAP_CONTENT
                }
                else -> {
                    lp.width = dp2px(mHostContext, width.toFloat())
                }
            }

            if (height == 0) {
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT
            } else {
                lp.height = dp2px(mHostContext, height.toFloat())
            }*/
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT

            if (animStyle != 0) {
                window.setWindowAnimations(animStyle)
            }
            window.attributes = lp
        }
        isCancelable = outCancel
    }

    open fun setDialogTheme(@StyleRes theme: Int): BaseMyDialog<*> {
        this.dialogTheme = theme
        return this
    }

    open fun setMargin(margin: Int): BaseMyDialog<*> {
        this.margin = margin
        return this
    }

    open fun setWidth(width: Int): BaseMyDialog<*> {
        this.width = width
        return this
    }

    open fun setHeight(height: Int): BaseMyDialog<*> {
        this.height = height
        return this
    }

    open fun setDimAmount(dimAmount: Float): BaseMyDialog<*> {
        this.dimAmount = dimAmount
        return this
    }

    open fun setGravity(gravity: Int): BaseMyDialog<*> {
        this.gravity = gravity
        return this
    }

    open fun setOutCancel(outCancel: Boolean): BaseMyDialog<*> {
        this.outCancel = outCancel
        return this
    }

    open fun setAnimStyle(@StyleRes animStyle: Int): BaseMyDialog<*> {
        this.animStyle = animStyle
        return this
    }

    open fun show(manager: FragmentManager): BaseMyDialog<*> {
        val ft = manager.beginTransaction()
        if (this.isAdded) {
            ft.remove(this).commit()
        }
        ft.add(this, System.currentTimeMillis().toString())
        ft.commitAllowingStateLoss()

        return this
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
