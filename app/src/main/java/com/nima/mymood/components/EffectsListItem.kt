package com.nima.mymood.components

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nima.mymood.R
import com.nima.mymood.ui.theme.*

@SuppressLint("DefaultLocale")
@Composable
fun EffectsListItem(
    effectRate: Int = 0,
    effectDescription: String = "",
    effectDate: String? = null,
    effectHour: String,
    effectMinute: String,
    onDeleteClicked: () -> Unit,
    onEditClicked: () -> Unit
) {
    OutlinedCard (
        shape = RoundedCornerShape(10.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        elevation = CardDefaults.elevatedCardElevation(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter =
                    painterResource(
                        id =
                        when (effectRate) {
                            0 -> R.drawable.ic_outline_sentiment_very_satisfied_24
                            1 -> R.drawable.ic_outline_sentiment_satisfied_alt_24
                            2 -> R.drawable.ic_outline_sentiment_neutral_24
                            3 -> R.drawable.ic_outline_sentiment_dissatisfied_24
                            else -> R.drawable.ic_outline_sentiment_very_dissatisfied_24
                        }
                    ),
                    contentDescription = null,
                    tint = when (effectRate) {
                        0 -> very_satisfied
                        1 -> satisfied
                        2 -> neutral
                        3 -> dissatisfied
                        else -> very_dissatisfied
                    },
                    modifier = Modifier.size(40.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    if (!effectDate.isNullOrBlank()) {
                        Text(
                            text = effectDate,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    if (effectHour.isNotBlank() && effectMinute.isNotBlank()) {
                        Text(
                            text = String.format(
                                "%02d:%02d",
                                effectHour.toInt(),
                                effectMinute.toInt()
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 5.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                SelectionContainer {
                    Text(
                        text = effectDescription,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, bottom = 8.dp, top = 5.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = {
                        onDeleteClicked()
                    },
                    modifier = Modifier.padding(horizontal = 5.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.errorContainer,
                    ),
                    border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.errorContainer),
                ) {
                    Icon(imageVector = Icons.Outlined.Delete, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("Delete", modifier = Modifier.padding(start = 5.dp))
                }
                OutlinedButton(
                    onClick = {
                        onEditClicked()
                    },
                    modifier = Modifier.padding(horizontal = 5.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    border = null,
                ) {
                    Icon(imageVector = Icons.Outlined.Edit, contentDescription = null, modifier = Modifier.size(18.dp))
                    Text("Edit", modifier = Modifier.padding(start = 5.dp))
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
fun EffectsListItem(
    modifier: Modifier = Modifier,
    effectRate: Int = 0,
    effectDescription: String = "",
    effectDate: String? = null,
    effectHour: String,
    effectMinute: String,
    onEditClicked: () -> Unit
) {
    OutlinedCard(
        shape = RoundedCornerShape(10.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(bottom = 32.dp),
        elevation = CardDefaults.elevatedCardElevation(15.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 15.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    painter =
                    painterResource(
                        id =
                        when (effectRate) {
                            0 -> R.drawable.ic_outline_sentiment_very_satisfied_24
                            1 -> R.drawable.ic_outline_sentiment_satisfied_alt_24
                            2 -> R.drawable.ic_outline_sentiment_neutral_24
                            3 -> R.drawable.ic_outline_sentiment_dissatisfied_24
                            else -> R.drawable.ic_outline_sentiment_very_dissatisfied_24
                        }
                    ),
                    contentDescription = null,
                    tint = when (effectRate) {
                        0 -> very_satisfied
                        1 -> satisfied
                        2 -> neutral
                        3 -> dissatisfied
                        else -> very_dissatisfied
                    },
                    modifier = Modifier.size(40.dp)
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier.padding(horizontal = 10.dp)
                ) {
                    if (!effectDate.isNullOrBlank()) {
                        Text(
                            text = effectDate,
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                        )
                    }
                    if (effectHour.isNotBlank() && effectMinute.isNotBlank()) {
                        Text(
                            text = String.format(
                                "%02d:%02d",
                                effectHour.toInt(),
                                effectMinute.toInt()
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.padding(end = 5.dp)
                        )
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 5.dp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Start
            ) {
                SelectionContainer {
                    Text(
                        text = effectDescription,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, bottom = 8.dp, top = 5.dp, end = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.End
            ) {
                OutlinedButton(
                    onClick = {
                        onEditClicked()
                    },
                    modifier = Modifier.padding(horizontal = 5.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    ),
                    border = null,
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Text("Edit", modifier = Modifier.padding(start = 5.dp))
                }
            }
        }
    }
}