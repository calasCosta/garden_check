package com.ams.gardencheck.ui.screens

import android.content.Context
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import com.ams.gardencheck.R
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.KeyboardType
import com.ams.gardencheck.data.entities.PlantDisease
import com.ams.gardencheck.ui.main.MainViewModel

/*@Composable
fun HomeScreen(modifier: Modifier = Modifier){
    Text(
        text = "Home",
        fontSize = 32.sp,
    )
}*/

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    mv: MainViewModel,
    nc: NavController
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF8F8F8)) // Cor de fundo leve para destacar os cards
    ) {
        Text(
            text = "Home",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 24.dp, bottom = 8.dp)
        )

        SearchBarComponent(mv = mv, nc = nc)

        // Usamos weight(1f) para a lista ocupar o espaço restante
        Box(modifier = Modifier.weight(1f)) {
            DisplayPlantDiseases(mv = mv, nc = nc)
        }
    }
}

@Composable
fun DisplayPlantDiseases(mv: MainViewModel, nc: NavController) {
    val state by mv.mainViewState.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(state.plantDiseases) { plantDisease ->
            // Card principal para dar o aspeto de elevação e bordas
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { mv.editPlantDisease(plantDisease) },
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(12.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // 1. IMAGEM ARREDONDADA
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.size(60.dp)
                    ) {
                        // Usa a propriedade imagePath da sua Entity
                        LoadImageFromString(imageName = plantDisease.imagePath)
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    // 2. TEXTO CENTRAL
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = plantDisease.diseaseTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            // Badge de Percentagem (Verde claro)
                            Surface(
                                color = Color(0xFFC1F8D1),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "${plantDisease.confidence}%",
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    fontSize = 12.sp,
                                    color = Color(0xFF1B5E20)
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Text(
                                text = plantDisease.createdDate, // Ex: "Today"
                                color = Color.Gray,
                                fontSize = 14.sp
                            )
                        }
                    }

                    // 3. BOTÃO DELETE CIRCULAR (Rosa claro)
                    IconButton(
                        onClick = { mv.deletePlantDisease(plantDisease) },
                        modifier = Modifier
                            .background(Color(0xFFFFEBEE), CircleShape)
                            .size(40.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Delete",
                            tint = Color(0xFFD32F2F),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBarComponent(mv: MainViewModel, nc: NavController) {
    // Observamos o estado vindo do ViewModel
    val state by mv.mainViewState.collectAsState()

    // Ferramentas para controlar o teclado e o cursor
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    TextField(
        value = state.searchText,
        onValueChange = { novoTexto ->
            // Atualiza o texto no ViewModel e dispara o filtro da lista
            mv.onSearchTextChanged(novoTexto)
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .height(56.dp),
        placeholder = {
            Text(
                text = stringResource(R.string.search_diseases),
                color = Color.Gray
            )
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Gray
            )
        },
        // Botão "X" para limpar a pesquisa (aparece apenas quando há texto)
        trailingIcon = {
            if (state.searchText.isNotEmpty()) {
                IconButton(onClick = {
                    mv.onSearchTextChanged("")
                    focusManager.clearFocus()
                }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Clear search",
                        tint = Color.Gray
                    )
                }
            }
        },
        shape = RoundedCornerShape(24.dp),
        colors = TextFieldDefaults.colors(
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            disabledIndicatorColor = Color.Transparent,
            focusedContainerColor = Color(0xFFE0E0E0),
            unfocusedContainerColor = Color(0xFFE0E0E0),
            cursorColor = Color.Black
        ),
        singleLine = true,
        // Configura o botão "Enter" do teclado como uma lupa de Pesquisa
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Search,
            keyboardType = KeyboardType.Text
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                // Ao clicar na lupa do teclado, escondemos o teclado e removemos o foco
                keyboardController?.hide()
                focusManager.clearFocus()
            }
        )
    )
}

@Composable
fun LoadImageFromString(imageName: String) {
    val context: Context = LocalContext.current
    val resourceId = context.resources.getIdentifier(
        imageName,
        "drawable",
        context.packageName)

    if (resourceId != 0) {
        Image(
            painter = painterResource(id = resourceId),
            contentDescription = null,
            modifier = Modifier.size(60.dp), // Ajustado para o tamanho do Card
            contentScale = ContentScale.Crop // Corta a imagem para preencher o quadrado
        )
    } else {
        Box(
            modifier = Modifier.size(60.dp).background(Color.LightGray),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Warning, contentDescription = null, tint = Color.White)
        }
    }
}