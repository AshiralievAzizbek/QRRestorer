package uz.owapps.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import uz.owapps.PERMISSION_REQUEST_CODE
import uz.owapps.R
import uz.owapps.databinding.FragmentScannerBinding
import uz.owapps.utils.CodeScanner
import uz.owapps.utils.DecodeCallback
import uz.owapps.utils.ScannerHelper

class ScannerFragment : Fragment(), ScannerHelper {


    private lateinit var _binding: FragmentScannerBinding
    private val mBinding: FragmentScannerBinding get() = _binding

    private lateinit var codeScanner: CodeScanner

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentScannerBinding.inflate(layoutInflater, container, false)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (checkPermission()) {
            initQRScanner()
        } else requestPermission()
    }

    private fun checkPermission(): Boolean {
        return ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }


    private fun requestPermission() {
        ActivityCompat.requestPermissions(
            requireActivity(), arrayOf(Manifest.permission.CAMERA),
            PERMISSION_REQUEST_CODE
        )
    }

    private fun initQRScanner() {
        val scannerView = mBinding.scannerView
        val activity = requireActivity()
        codeScanner = CodeScanner(activity, scannerView)
        codeScanner.decodeCallback = DecodeCallback {
            replaceFragment(it.text)
        }
    }

    private fun replaceFragment(text: String) {
        val args = Bundle()
        args.putString("decoded", text)
        val fragment = GeneratedCodeFragment(this)
        fragment.arguments = args
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(
                R.id.fragment_container,
                fragment,
                fragment::javaClass.name
            ).addToBackStack(fragment::javaClass.name).commit()

    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
    }

    override fun onPause() {
        codeScanner.releaseResources()
        super.onPause()
    }

    override fun releasePreview() {
        codeScanner.startPreview()
    }
}