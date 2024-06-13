package org.d3if3008.assessment3.ui.screen

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import org.d3if3008.assessment3.R
import org.d3if3008.assessment3.database.TravelDb
import org.d3if3008.assessment3.ui.theme.Assessment3Theme
import org.d3if3008.assessment3.util.ViewModelFactory

const val KEY_ID_TRAVEL = "idTravel"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TravelScreen(navController: NavHostController, id: Long? = null) {
    val context = LocalContext.current
    val db = TravelDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    var nama by remember { mutableStateOf("") }
    var keberangkatan by remember { mutableStateOf("") }
    var tujuan by remember { mutableStateOf("") }
    var jam by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }

    LaunchedEffect(true) {

        if (id == null) return@LaunchedEffect
        val data = viewModel.getTravel(id) ?: return@LaunchedEffect
        nama = data.nama
        keberangkatan = data.keberangkatan
        tujuan = data.tujuan
        jam = data.jam
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.kembali),
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                title = {
                    if (id == null)
                        Text(text = stringResource(id = R.string.tambah_keberangkatan))
                    else
                        Text(text = stringResource(id = R.string.ubah_keberangkatan))
                },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary
                ),
                actions = {
                    if (id != null) {
                        DeleteAction { showDialog = true }
                        DisplayAlertDialog(
                            openDialog = showDialog,
                            onDismissRequest = { showDialog = false }) {
                            showDialog = false
                            viewModel.delete(id)
                            navController.popBackStack()
                        }
                    }
                }
            )
        }
    ) { paddingValues ->
        FormTravel(
            nama = nama,
            onNamaChange = { nama = it },
            keberangkatan = keberangkatan,
            onKeberangkatanChange = { keberangkatan = it },
            tujuan = tujuan,
            onTujuanChange = { tujuan = it },
            jam = jam,
            onJamChange = { jam = it },
            modifier = Modifier.padding(paddingValues), navController = navController
        )
    }
}


@Composable
fun FormTravel(
    navController: NavHostController,
    nama: String,
    onNamaChange: (String) -> Unit,
    keberangkatan: String,
    onKeberangkatanChange: (String) -> Unit,
    tujuan: String,
    onTujuanChange: (String) -> Unit,
    jam: String,
    onJamChange: (String) -> Unit,
    modifier: Modifier,
    id: Long? = null
) {
    val context = LocalContext.current
    val db = TravelDb.getInstance(context)
    val factory = ViewModelFactory(db.dao)
    val viewModel: DetailViewModel = viewModel(factory = factory)

    val optionsKeberangkatan = listOf(
        "Buah Batu",
        "Moch. Toha",
        "Bojong Soang",
        "Cihampelas",
        "Kopo"

    )
    val optionsTujuan = listOf(
        "Jakarta",
        "Bogor",
        "Depok",
        "Tangerang",
        "Bekasi"
    )
    val optionsJam = listOf(
        "5:00",
        "9:00",
        "13:00",
        "17:00",
        "21:00"
    )
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        OutlinedTextField(
            value = nama,
            onValueChange = { onNamaChange(it) },
            label = { Text(text = stringResource(R.string.nama)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.Words,
                imeAction = ImeAction.Next
            ),
            modifier = Modifier.fillMaxWidth()
        )
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            Text(
                text = "Keberangkatan",
                modifier = Modifier.padding(16.dp)
            )
            optionsKeberangkatan.forEach { text ->
                ClassOption(
                    label = text,
                    isSelected = keberangkatan == text,
                    onOptionSelected = { onKeberangkatanChange(it) },
                    modifier = Modifier
                        .selectable(
                            selected = keberangkatan == text,
                            onClick = { onKeberangkatanChange(text) },
                            role = Role.RadioButton
                        )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            Text(
                text = "Tujuan",
                modifier = Modifier.padding(16.dp)
            )
            optionsTujuan.forEach { text ->
                ClassOption(
                    label = text,
                    isSelected = tujuan == text,
                    onOptionSelected = { onTujuanChange(it) },
                    modifier = Modifier
                        .selectable(
                            selected = tujuan == text,
                            onClick = { onTujuanChange(text) },
                            role = Role.RadioButton
                        )
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 6.dp)
                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
        ) {
            Text(
                text = "Jam keberangkatan",
                modifier = Modifier.padding(16.dp)
            )
            optionsJam.forEach { text ->
                ClassOption(
                    label = text,
                    isSelected = jam == text,
                    onOptionSelected = { onJamChange(it) },
                    modifier = Modifier
                        .selectable(
                            selected = jam == text,
                            onClick = { onJamChange(text) },
                            role = Role.RadioButton
                        )
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = {
                    if (nama == "" || keberangkatan == "" || tujuan == "" || jam == "") {
                        Toast.makeText(context, R.string.invalid, Toast.LENGTH_LONG).show()
                        return@Button
                    }
                    if (id == null) {
                        viewModel.insert(nama, keberangkatan, tujuan, jam)
                    } else {
                        viewModel.update(id, nama, keberangkatan, tujuan, jam)
                    }
                    navController.popBackStack()
                },
                colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.primaryContainer),
            ) {
                Text(
                    text = stringResource(id = R.string.simpan),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun ClassOption(
    label: String,
    isSelected: Boolean,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(selected = isSelected, onClick = { onOptionSelected(label) })
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(start = 1.dp)
        )
    }
}

@Composable
fun DeleteAction(delete: () -> Unit) {
    IconButton(onClick = { delete() }) {
        Icon(
            imageVector = Icons.Filled.Delete,
            contentDescription = stringResource(R.string.hapus),
            tint = MaterialTheme.colorScheme.primary
        )
    }
}

@Preview(showBackground = true)
@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES, showBackground = true)
@Composable
fun TravelScreenPreview() {
    Assessment3Theme {
        TravelScreen(rememberNavController())
    }
}