package br.edu.ifsp.aluno.vander.gabriel.conceitoservice

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.ui.Modifier
import br.edu.ifsp.aluno.vander.gabriel.conceitoservice.services.LifetimeBoundService
import br.edu.ifsp.aluno.vander.gabriel.conceitoservice.ui.components.ServiceDisplay
import br.edu.ifsp.aluno.vander.gabriel.conceitoservice.ui.theme.ConceitoServiceTheme
import br.edu.ifsp.aluno.vander.gabriel.conceitoservice.ui.view_models.ServiceLifetimeViewModel

class MainActivity : ComponentActivity() {
    private val lifetimeServiceIntent: Intent by lazy {
        Intent(this, LifetimeBoundService::class.java)
    }
    private lateinit var lifetimeBoundService: LifetimeBoundService
    private var connected = false
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, binder: IBinder?) {
            lifetimeBoundService =
                (binder as LifetimeBoundService.LifetimeBoundServiceBinder).getService()
            connected = true
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            connected = false
        }

    }
    private val serviceLifetimeViewModel by viewModels<ServiceLifetimeViewModel>()

    private lateinit var lifetimeServiceHandler: LifetimeServiceHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HandlerThread("LifetimeHandlerThread").apply {
            start()
            lifetimeServiceHandler = LifetimeServiceHandler(looper)
        }

        setContent {
            ConceitoServiceTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    ServiceDisplay(
                        serviceLifetimeViewModel = serviceLifetimeViewModel,
                        onStartService = {
                            bindService(
                                lifetimeServiceIntent,
                                serviceConnection,
                                Context.BIND_AUTO_CREATE
                            )
                            lifetimeServiceHandler.obtainMessage().also {
                                lifetimeServiceHandler.sendMessageDelayed(it, 1000)
                            }
                        },
                        onStopService = {
                            unbindService(serviceConnection)
                            connected = false
                        }
                    )
                }
            }
        }
    }

    private inner class LifetimeServiceHandler(lifetimeServiceLooper: Looper) :
        Handler(lifetimeServiceLooper) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            if (connected) {
                serviceLifetimeViewModel.onLifetimeValueChanged(lifetimeBoundService.lifetime)
                obtainMessage().also {
                    sendMessageDelayed(it, 1000)
                }
            }
        }
    }

}
