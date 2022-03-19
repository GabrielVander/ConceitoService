package br.edu.ifsp.aluno.vander.gabriel.conceitoservice.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.sp
import br.edu.ifsp.aluno.vander.gabriel.conceitoservice.ui.view_models.ServiceLifetimeViewModel

@Composable
fun ServiceDisplay(
    serviceLifetimeViewModel: ServiceLifetimeViewModel,
    onStartService: () -> Unit,
    onStopService: () -> Unit,
) {
    val lifetime: Int by serviceLifetimeViewModel.lifetime.observeAsState(0)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(text = "Service lifetime")
        Text(
            fontSize = 26.sp,
            text = "$lifetime"
        )
        Button(onClick = onStartService) {
            Text(text = "Start service")
        }
        Button(onClick = onStopService) {
            Text(text = "Stop service")
        }
    }

}