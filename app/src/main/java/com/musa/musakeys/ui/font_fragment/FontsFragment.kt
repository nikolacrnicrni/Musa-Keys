package com.musa.musakeys.ui.font_fragment

import android.Manifest
import android.app.DownloadManager
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.musa.musakeys.ui.NavigationActivity
import com.musa.musakeys.R
import com.musa.musakeys.asyncTasks.NetworkHelper
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.ui.font_fragment.adapter.MyFontsAdapter
import com.musa.musakeys.utility.MySharedPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class FontsFragment : Fragment() {
    private var mParam1: String? = null
    private var mParam2: String? = null

    private lateinit var myFontsAdapter: MyFontsAdapter
    private lateinit var mySharedPreference: MySharedPreference
    private lateinit var recyclerView: RecyclerView
    private lateinit var install: TextView
    private lateinit var update: TextView
    private lateinit var progressBar: ProgressBar
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val PERMISSION_NUM = 100

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        private var networkHelper: NetworkHelper? = null
        private var progressBar: ProgressBar? = null

        fun updateFonts(context: Context) {
            if (networkHelper?.isNetworkConnected == true) {
                progressBar?.visibility = View.VISIBLE
                MusaConstants.mapOfFontUris.keys.forEach { fontName ->
                    val file = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "$fontName.otf"
                    )
                    if (file.exists()) file.delete()
                    if (fontName != null) {
                        downloadFile(
                            context,
                            Uri.parse(MusaConstants.mapOfFontUris[fontName]),
                            fontName
                        )
                    }
                }
                progressBar?.visibility = View.GONE
            } else {
                Toast.makeText(context, "Please connect to the internet", Toast.LENGTH_SHORT).show()
            }
        }

        private fun downloadFile(context: Context, uri: Uri?, fontName: String) {
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            uri?.let {
                val file = File(context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS), "$fontName.otf")
                if (file.exists()) {
                    file.delete()
                }
                val request = DownloadManager.Request(it).apply {
                    setTitle(fontName)
                    setDescription("Downloading $fontName")
                    setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    setDestinationUri(Uri.fromFile(file))
                }
                downloadManager.enqueue(request)
            }
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mParam1 = it.getString(ARG_PARAM1)
            mParam2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fonts, container, false)
        mySharedPreference = MySharedPreference(requireContext())
        update = view.findViewById(R.id.updateButton)
        install = view.findViewById(R.id.installButton)
        recyclerView = view.findViewById(R.id.fontsRecycleView)
        networkHelper = NetworkHelper(requireContext())
        myFontsAdapter = MyFontsAdapter(
            requireContext(),
            MusaConstants.mapOfFontUris,
            object : MyFontsAdapter.ItemClickListener {
                override fun onItemClick(textView: TextView?, textView2: TextView?, position: Int) {
                    lambdaOnCreateView0()
                }
            })
        progressBar = view.findViewById(R.id.progressBar)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = myFontsAdapter
        install.setOnClickListener { lambdaOnCreateView1() }
        update.setOnClickListener { lambdaOnCreateView2() }
        return view
    }

    private fun lambdaOnCreateView0() {
        (activity as? NavigationActivity)?.setMusaTitle(
            (requireActivity().findViewById(R.id.toolbar) as Toolbar).findViewById(
                R.id.musa_title
            )
        )
    }

    private fun lambdaOnCreateView1() {
        if (networkHelper?.isNetworkConnected == true) {
            progressBar.visibility = View.VISIBLE
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_IMAGES) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_MEDIA_VIDEO) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_MEDIA_AUDIO, Manifest.permission.READ_MEDIA_VIDEO),
                        PERMISSION_NUM
                    )
                } else {
                    updateFonts(requireContext())
                }
            } else
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(
                        arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        PERMISSION_NUM
                    )
                } else {
                    updateFonts(requireContext())
                }


            coroutineScope.launch {
                delay(3000)
                MyFontsAdapter.setDownloaded(true)
                mySharedPreference.setPreferences(MusaConstants.FONTS_DOWNLOADED, "true")
                myFontsAdapter.notifyDataSetChanged()
                progressBar.visibility = View.GONE
            }
        } else {
            Toast.makeText(requireContext(), "Please connect to the internet", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
    }

    private fun lambdaOnCreateView2() {
        if (networkHelper?.isNetworkConnected == true) {
            lambdaOnCreateView1()
        } else {
            Toast.makeText(requireContext(), "Please connect to the internet", Toast.LENGTH_SHORT)
                .show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_NUM) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                updateFonts(requireContext())
            } else {
                Toast.makeText(requireContext(), "Permission denied!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }
}