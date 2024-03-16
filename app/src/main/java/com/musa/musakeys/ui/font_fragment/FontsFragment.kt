package com.musa.musakeys.ui.font_fragment

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.musa.musakeys.R
import com.musa.musakeys.asyncTasks.NetworkHelper
import com.musa.musakeys.constants.MusaConstants
import com.musa.musakeys.ui.NavigationActivity
import com.musa.musakeys.ui.font_fragment.adapter.MyFontsAdapter
import com.musa.musakeys.utility.MySharedPreference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

class FontsFragment : Fragment() {

    private lateinit var myFontsAdapter: MyFontsAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var install: TextView
    private lateinit var update: TextView
    private lateinit var progressBar: ProgressBar
    private val coroutineScope = CoroutineScope(Dispatchers.Main)

    private val mySharedPreference by lazy { MySharedPreference(requireContext()) }
    private val networkHelper by lazy { NetworkHelper(requireContext()) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_fonts, container, false)
        setupViews(view)
        myFontsAdapter = MyFontsAdapter(
            requireContext(),
            MusaConstants.mapOfFontUris,
            object : MyFontsAdapter.ItemClickListener {
                override fun onItemClick(textView: TextView?, textView2: TextView?, i: Int) {
                    (activity as? NavigationActivity)?.setMusaTitle(
                        (requireActivity().findViewById<Toolbar>(R.id.toolbar)!!).findViewById(
                            R.id.musa_title
                        )
                    )
                }
            })

        recyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = myFontsAdapter
        }

        install.setOnClickListener { fontDownloads() }
        update.setOnClickListener { fontDownloads() }
        return view
    }

    private fun setupViews(view: View) {
        update = view.findViewById(R.id.updateButton)
        install = view.findViewById(R.id.installButton)
        recyclerView = view.findViewById(R.id.fontsRecycleView)
        progressBar = view.findViewById(R.id.progressBar)
    }

    private fun updateFonts(context: Context) {
        if (networkHelper.isNetworkConnected) {
            progressBar.visibility = View.VISIBLE
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
            progressBar.visibility = View.GONE
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

    private fun fontDownloads() {
        if (networkHelper.isNetworkConnected) {
            progressBar.visibility = View.VISIBLE

            updateFonts(requireContext())
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
}