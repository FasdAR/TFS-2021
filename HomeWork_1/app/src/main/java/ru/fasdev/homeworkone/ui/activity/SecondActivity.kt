package ru.fasdev.homeworkone.ui.activity

import android.Manifest
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import ru.fasdev.homeworkone.background.broadcast.SyncContactsBroadcastReceiver
import ru.fasdev.homeworkone.background.service.SyncContactsService
import ru.fasdev.homeworkone.databinding.ActivitySecondBinding

class SecondActivity : AppCompatActivity() {
    companion object {
        const val KEY_CONTACTS = SyncContactsBroadcastReceiver.KEY_CONTACTS
    }

    private lateinit var binding: ActivitySecondBinding

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
        if (isGranted) {
            startSyncContacts()
        }
        else {
            toRequestPermissionState()
        }
    }

    private val localBroadcastReceiver = SyncContactsBroadcastReceiver(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecondBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backBtn.setOnClickListener { finish() }
        binding.repeatRequestBtn.setOnClickListener { requestPermission() }

        requestPermission()
    }

    override fun onResume() {
        super.onResume()

        //#region Register Local Receiver
        val intentFilter = IntentFilter(SyncContactsBroadcastReceiver.ACTION_CONTACTS)
        LocalBroadcastManager.getInstance(this)
                .registerReceiver(localBroadcastReceiver, intentFilter)
        //#endregion
    }

    override fun onPause() {
        super.onPause()

        LocalBroadcastManager.getInstance(this).unregisterReceiver(localBroadcastReceiver)
    }

    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS)
                == PackageManager.PERMISSION_GRANTED)
            startSyncContacts()
        else
            requestPermissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }

    private fun startSyncContacts() {
        toLoadingState()

        val intentService = Intent(applicationContext, SyncContactsService::class.java)
        startService(intentService)
    }

    //#region Ui State
    private fun toLoadingState() {
        binding.permission.isVisible = false
        binding.loading.isVisible = true
    }

    private fun toRequestPermissionState() {
        binding.permission.isVisible = true
        binding.loading.isVisible = false
    }
    //endregion
}