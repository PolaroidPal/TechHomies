package com.example.stepcounterapp.home.presentation.components

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.util.DebugLogger
import com.example.stepcounterapp.R
import com.example.stepcounterapp.core.domain.models.UserProfile
import com.example.stepcounterapp.step_counter.presentation.components.TextComponent

@Composable
fun DrawerHeader(
    userProfile: UserProfile = UserProfile()
) {
    val context = LocalContext.current
    val imageLoader = ImageLoader.Builder(context)
        .logger(DebugLogger())
        .build()

    Log.i("profile", "DrawerHeader: $userProfile")
    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (userProfile.photoUrl != null) {
            AsyncImage(
                model = userProfile.photoUrl,
                contentDescription = "User profile picture",
                imageLoader = imageLoader,
                modifier = Modifier
                    .clip(CircleShape)
                    .size(150.dp),
                placeholder = painterResource(id = R.drawable.user_placeholder_image),
                error = painterResource(id = R.drawable.user_error_image)
            )
        }
        TextComponent(
            text = userProfile.displayName!!,
            fontWeight = FontWeight.Bold
        )
        TextComponent(
            text = userProfile.email!!,
            fontSize = 10.sp,
            fontWeight = FontWeight.Normal
        )
    }
}