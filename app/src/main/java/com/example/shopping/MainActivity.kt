package com.example.shopping

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Size
import com.example.shopping.data.ProductsRepositoryImp
import com.example.shopping.data.model.Product
import com.example.shopping.presentation.viemodel.ProductsViewModel
import com.example.shopping.ui.theme.ShoppingTheme
import kotlinx.coroutines.flow.collectLatest

class MainActivity : ComponentActivity() {
    private val viewModel by viewModels<ProductsViewModel>(factoryProducer = {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                return ProductsViewModel(ProductsRepositoryImp(RetrofitInstance.api))
                        as T
            }
        }
    })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ShoppingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val productList = viewModel.products.collectAsState().value
                    val context = LocalContext.current
                    LaunchedEffect(key1 = viewModel.showErrorToastChannel) {
                        viewModel.showErrorToastChannel.collectLatest { show ->
                            if (show) {
                                Toast.makeText(context, "Error", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                    if (productList.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                        }
                    } else {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            contentPadding = PaddingValues(16.dp)
                        )
                        {
                            items(productList.size) { index ->
                                ProductItem(product = productList[index])
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                    }
                }
            }
        }
    }
}

@Composable
fun ProductItem(product: Product)
{
    val imageState=rememberAsyncImagePainter(model = ImageRequest.Builder(LocalContext.current).
    data(product.thumbnail).size(Size.ORIGINAL).build()).state

    Column(modifier = Modifier
        .clip(RoundedCornerShape(20.dp))
        .height(300.dp)
        .fillMaxWidth()
        .padding(16.dp)
        .background(color = MaterialTheme.colorScheme.primaryContainer))
    {
        if(imageState is AsyncImagePainter.State.Error)
        {
            Box(modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
        if(imageState is AsyncImagePainter.State.Success)
        {
            Image(painter = imageState.painter, contentDescription = product.title,
              modifier = Modifier
                  .height(200.dp)
                  .fillMaxWidth(), contentScale = ContentScale.Crop )

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = "${product.title}--Price: ${product.price}$",
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 17.sp, fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(text = product.description,
                modifier = Modifier.padding(horizontal = 16.dp),
                fontSize = 13.sp
            )
        }

    }
}

