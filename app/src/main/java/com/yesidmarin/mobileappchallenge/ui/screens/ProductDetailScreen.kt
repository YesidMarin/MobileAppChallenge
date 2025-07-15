package com.yesidmarin.mobileappchallenge.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerDefaults
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade
import com.yesidmarin.mobileappchallenge.R
import com.yesidmarin.mobileappchallenge.ui.components.AttributeTable
import com.yesidmarin.mobileappchallenge.ui.components.ErrorView
import com.yesidmarin.mobileappchallenge.ui.components.HorizontalDivider
import com.yesidmarin.mobileappchallenge.ui.components.LoadingIndicator
import com.yesidmarin.mobileappchallenge.ui.components.MainFeaturesList
import com.yesidmarin.mobileappchallenge.ui.components.TitleSectionDetailText
import com.yesidmarin.mobileappchallenge.ui.viewmodel.ProductDetailViewModel
import java.lang.Thread.sleep

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(navController: NavController, viewModel: ProductDetailViewModel = hiltViewModel(), id: String) {
    // Collecting state from the ViewModel
    val productDetail by viewModel.productDetail.collectAsState()
    val errorState by viewModel.errorState.collectAsState()
    val productId by viewModel.productId.collectAsState()
    val colorPicker by viewModel.colorPicker.collectAsState()
    val productImages by viewModel.images.collectAsState()
    val productName by viewModel.productName.collectAsState()
    val pickerColorName by viewModel.colorNameSelect.collectAsState()

    val pagerState = rememberPagerState(
        initialPage = 0,
        pageCount = { productImages.size }
    )

    //  Trigger initial details when the screen is launched
    LaunchedEffect(Unit) {
        sleep(100)
        viewModel.getProductDetails(id)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de producto") },
                navigationIcon = {
                    IconButton(onClick = {navController.navigateUp()}) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.onBackground
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            // Show loading indicator when productDetail is null and errorState is null
            productDetail == null && errorState == null  -> {
                LoadingIndicator()
            }
            // Show error view if there is an error
            errorState != null -> {
                ErrorView(errorState ?: "Ocurrió un error inesperado", "Regresar") {
                    navController.navigateUp()
                }
            }
            // Show product details when productDetail is available
            else -> {
                Column(
                    modifier = Modifier.padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .animateContentSize()
                ) {
                    // Display product name
                    Text(modifier = Modifier
                        .padding(16.dp), text = productName ?: "",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Light)

                    // Display product images in a horizontal pager
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp),
                        flingBehavior = PagerDefaults.flingBehavior(state = pagerState, snapAnimationSpec= spring(stiffness = 300f, dampingRatio = 0.8f))
                    ) { page ->

                        val imageUrl = productImages.getOrNull(page)

                        AsyncImage(
                            model = ImageRequest.Builder(LocalContext.current)
                                .data(imageUrl)
                                .crossfade(true)
                                .build(),
                            contentDescription = "Product Image",
                            modifier = Modifier.fillMaxWidth(),
                            error = painterResource(R.drawable.placeholder_image),
                        )
                    }

                    // Display image indicators
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        productImages.let { images ->
                            repeat(images.size) { index ->
                                val isSelected = pagerState.currentPage == index
                                val isVisible = images.size <= 3 || (index >= pagerState.currentPage - 1 && index <= pagerState.currentPage + 1)

                                AnimatedVisibility(visible = isVisible) {
                                    Box(
                                        modifier = Modifier
                                            .size(if (isSelected) 15.dp else 12.dp)
                                            .padding(4.dp)
                                            .background(
                                                color = if (isSelected) Color(0xFF3483FA) else Color(0xff736c28),
                                                shape = CircleShape
                                            )
                                    )
                                }
                            }
                        }
                    }

                    // Display color picker information
                    colorPicker?.products?.let { product ->
                        Row(
                            modifier = Modifier.padding(start = 8.dp)
                        ) {
                            Text(
                                (colorPicker?.pickerName + ": "),
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Normal)
                            Text(
                                (pickerColorName ?: ""),
                                style = MaterialTheme.typography.labelLarge)
                        }

                        LazyRow(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(product.size) { index ->
                                val borderColor = if ((product[index].productId ?: "") == productId) Color(0xFF3483FA) else Color(0x8DE2E2E2)

                                Card(
                                    modifier = Modifier
                                        .size(width = 80.dp, height = 100.dp)
                                        .animateItem()
                                        .clickable {
                                            viewModel.getProductDetails(product[index].productId)
                                        }
                                        .border(1.dp, borderColor, RoundedCornerShape(5.dp)),
                                    shape = RoundedCornerShape(5.dp)
                                ) {
                                    Column(
                                        Modifier
                                            .fillMaxSize()
                                            .padding(6.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                    ) {
                                        AsyncImage(
                                            model = ImageRequest.Builder(LocalContext.current)
                                                .data(product[index].thumbnail)
                                                .crossfade(true)
                                                .build(),
                                            contentDescription = null,
                                            modifier = Modifier
                                                .size(56.dp)
                                                .clip(RoundedCornerShape(4.dp)),
                                            error = painterResource(R.drawable.placeholder_image)
                                        )

                                        Text(
                                            text = product[index].pickerLabel ?: "",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = Color.Gray,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.fillMaxWidth()
                                        )
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider()

                    // Display product attributes
                    productDetail?.attributes?.let {
                        if (it.isNotEmpty()) {
                            TitleSectionDetailText("Características del producto")
                            AttributeTable(it, modifier = Modifier.padding(horizontal = 8.dp))
                        }
                    }

                    // Display main features of the product
                    productDetail?.mainFeatures?.map{ it.text }?.let {
                        if (it.isNotEmpty()) {
                            HorizontalDivider()
                            TitleSectionDetailText("Lo que tienes que saber de este producto")
                            Column(modifier = Modifier.padding(vertical = 10.dp)) {
                                MainFeaturesList(it)
                            }
                        }
                    }

                    // Display short description of the product
                    productDetail?.shortDescription?.text.let { description ->
                        if (!description.isNullOrEmpty()) {
                            HorizontalDivider()
                            TitleSectionDetailText("Descripción")
                            Text(text = description,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .padding(vertical = 1.dp)
                                    .fillMaxWidth(),
                                style = MaterialTheme.typography.bodyMedium,
                            )
                        }
                    }
                }
            }
        }
    }
}
