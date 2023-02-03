package com.nima.mymood.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.nima.mymood.R
import com.nima.mymood.components.EffectsListItem
import com.nima.mymood.viewmodels.SadEffectsViewModel

@Composable
fun SadEffectsScreen(
    navController: NavController,
    viewModel: SadEffectsViewModel
) {

    val sadEffects = viewModel.getSadMood().collectAsState(initial = emptyList())

    if (sadEffects.value.isEmpty()){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 64.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(painter = painterResource(id = R.drawable.ic_outline_sentiment_very_satisfied_24),
                contentDescription = null,
                tint = Color.LightGray,
                modifier = Modifier
                    .size(96.dp)
                    .padding(bottom = 8.dp)
            )
            Text(text = "No Sad Effects Yet!",
                style = MaterialTheme.typography.bodySmall,
                fontWeight = FontWeight.Light,
                color = Color.LightGray,
                modifier = Modifier.padding(bottom = 8.dp)
            )
        }
    }else{
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 32.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            items(items = sadEffects.value){
                EffectsListItem(
                    it.rate,
                    it.description
                )
            }
        }
    }
}