package uz.owapps.ui

import android.graphics.Bitmap
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.activity.OnBackPressedDispatcher
import com.google.zxing.BarcodeFormat
import com.google.zxing.common.BitMatrix
import com.google.zxing.qrcode.QRCodeWriter
import uz.owapps.R
import uz.owapps.databinding.FragmentGeneratedCodeBinding
import uz.owapps.utils.ScannerHelper

class GeneratedCodeFragment(val scannerHelper: ScannerHelper) : Fragment() {

    private lateinit var _binding: FragmentGeneratedCodeBinding
    private val mBinding: FragmentGeneratedCodeBinding get() = _binding
    private val qrCodeWriter: QRCodeWriter = QRCodeWriter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGeneratedCodeBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val bm: Bitmap = convertToBitmap(
            qrCodeWriter.encode(
                requireArguments().getString("decoded"),
                BarcodeFormat.QR_CODE,
                300,
                300
            )
        )

        mBinding.btnBack.setOnClickListener { requireActivity().onBackPressed() }
        mBinding.ivQrImage.setImageBitmap(bm)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                scannerHelper.releasePreview()
                if (isEnabled) {
                    isEnabled = false
                    requireActivity().onBackPressed()
                }
            }

        })
    }


    private fun convertToBitmap(bitMatrix: BitMatrix): Bitmap {

        // change the values to your needs

        // change the values to your needs
        val requestedWidth = 300
        val requestedHeight = 300

        val width: Int = bitMatrix.width
        val height: Int = bitMatrix.height

        // calculating the scaling factor

        // calculating the scaling factor
        var pixelsize = requestedWidth / width
        if (pixelsize > requestedHeight / height) {
            pixelsize = requestedHeight / height
        }

        val pixels = IntArray(requestedWidth * requestedHeight)
        // All are 0, or black, by default
        // All are 0, or black, by default
        for (y in 0 until height) {
            var offset = y * requestedWidth * pixelsize

            // scaling pixel height
            var pixelsizeHeight = 0
            while (pixelsizeHeight < pixelsize) {
                for (x in 0 until width) {
                    val color = if (bitMatrix.get(
                            x,
                            y
                        )
                    ) resources.getColor(R.color.black) else resources.getColor(R.color.white)

                    // scaling pixel width
                    for (pixelsizeWidth in 0 until pixelsize) {
                        pixels[offset + x * pixelsize + pixelsizeWidth] = color
                    }
                }
                pixelsizeHeight++
                offset += requestedWidth
            }
        }
        val bitmap = Bitmap.createBitmap(requestedWidth, requestedHeight, Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, requestedWidth, 0, 0, requestedWidth, requestedHeight)

        return bitmap
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(requireArguments())
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        val bm: Bitmap = convertToBitmap(
            qrCodeWriter.encode(
                requireArguments().getString("decoded"),
                BarcodeFormat.QR_CODE,
                300,
                300
            )
        )
        mBinding.ivQrImage.setImageBitmap(bm)

    }
}