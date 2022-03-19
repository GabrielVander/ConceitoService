package br.edu.ifsp.aluno.vander.gabriel.conceitoservice

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import br.edu.ifsp.aluno.vander.gabriel.conceitoservice.services.LifetimeStartedService
import br.edu.ifsp.aluno.vander.gabriel.conceitoservice.services.LifetimeStartedService.Companion.ACTION_RECEIVE_LIFETIME
import br.edu.ifsp.aluno.vander.gabriel.conceitoservice.services.LifetimeStartedService.Companion.LIFETIME_EXTRA
import br.edu.ifsp.aluno.vander.gabriel.conceitoservice.ui.components.ServiceDisplay
import br.edu.ifsp.aluno.vander.gabriel.conceitoservice.ui.theme.ConceitoServiceTheme
import br.edu.ifsp.aluno.vander.gabriel.conceitoservice.ui.view_models.ServiceLifetimeViewModel

class MainActivity : ComponentActivity() {
    private val lifetimeServiceIntent: Intent by lazy {
        Intent(this, LifetimeStartedService::class.java)
    }
    private val serviceLifetimeViewModel by viewModels<ServiceLifetimeViewModel>()

    private val lifetimeBroadcastReceiver: BroadcastReceiver by lazy {
        object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                intent?.getIntExtra(LIFETIME_EXTRA, 0)
                    .also { lifetime ->
                        if (lifetime != null)
                            serviceLifetimeViewModel.onLifetimeValueChanged(lifetime)
                    }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConceitoServiceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ServiceDisplay(
                        serviceLifetimeViewModel = serviceLifetimeViewModel,
                        onStartService = { startService(lifetimeServiceIntent) },
                        onStopService = { stopService(lifetimeServiceIntent) }
                    )
                }
            }
        }
    }

    override fun onStart() {
        super.onStart()
        registerReceiver(lifetimeBroadcastReceiver, IntentFilter(ACTION_RECEIVE_LIFETIME))
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(lifetimeBroadcastReceiver)
    }
}
