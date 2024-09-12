package com.kiamba.myfirebasemvvm.ui.theme.screens.products

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.kiamba.myfirebasemvvm.data.productviewmodel
import com.kiamba.myfirebasemvvm.model.Product
import com.kiamba.myfirebasemvvm.navigation.ROUTE_UPDATE_PRODUCT

@Composable
fun ViewProductsScreen(navController: NavHostController) {
    val context = LocalContext.current
    val productRepository = productviewmodel(navController, context)
    val emptyProductState = remember { mutableStateOf(Product("", "", "", "")) }
    val emptyProductsListState = remember { mutableStateListOf<Product>() }

    val products by remember {
        mutableStateOf(
            productRepository.viewProducts(emptyProductState, emptyProductsListState)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "All Products",
            fontSize = 30.sp,
            fontFamily = FontFamily.Cursive,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 20.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(products) { product ->
                ProductItem(
                    name = product.name,
                    quantity = product.quantity,
                    price = product.price,
                    id = product.id,
                    navController = navController,
                    productRepository = productRepository
                )
            }
        }
    }
}

@Composable
fun ProductItem(
    name: String,
    quantity: String,
    price: String,
    id: String,
    navController: NavHostController,
    productRepository: productviewmodel
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .border(1.dp, Color.Gray)
            .padding(16.dp)
    ) {
        Text(
            text = "Name: $name",
            fontSize = 18.sp,
            fontFamily = FontFamily.SansSerif,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Quantity: $quantity",
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Price: $price",
            fontSize = 16.sp,
            fontFamily = FontFamily.SansSerif
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            OutlinedButton(onClick = {
                productRepository.deleteProduct(
                    id,
                    onSuccess = TODO()
                )
            }) {
                Text(text = "Delete")
            }

            Button(onClick = {
                navController.navigate("$ROUTE_UPDATE_PRODUCT/$id")
            }) {
                Text(text = "Update")
            }
        }
    }
}

@Preview
@Composable
fun ViewProductsScreenPreview() {
    ViewProductsScreen(rememberNavController())
}
