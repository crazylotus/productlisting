package com.example.codegamatask

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.compose.ui.graphics.vector.addPathNodes
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.codegamatask.databinding.ActivityMainBinding
import com.example.codegamatask.datamodal.Category
import com.example.codegamatask.datamodal.Product

class MainActivity : AppCompatActivity(),MainAdapter.OnItemClickListerner {

    private lateinit var binding : ActivityMainBinding

    lateinit var viewModel: MainViewModel
    lateinit var adapter: MainAdapter
    lateinit var productAdapter : ProductAdapter

    var categoriesList : ArrayList<Category> = ArrayList()
    var productList : ArrayList<Product> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val apiSerive = ApiSerive.getInstance()
        val mainRepository = Repository(apiSerive)
        adapter = MainAdapter(this,this)
        productAdapter = ProductAdapter(this)

        var layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        var layoutManager2 = LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false)


        binding.rvCategorylist.layoutManager = layoutManager
        binding.rvCategorylist.adapter = adapter

        binding.rvProductList.layoutManager = layoutManager2
        binding.rvProductList.adapter = productAdapter

        viewModel = ViewModelProvider(this, MyViewModelFactory(mainRepository))[MainViewModel::class.java]


        viewModel.categoriesList.observe(this) {

            categoriesList.clear()
            for (item in it) {
                var categoryitem = item.capitalize().replace("-"," ")
                val category = Category(categoryitem,item)
                categoriesList.add(category)
            }
            categoriesList[0].selected=true
            adapter.setMovieList(categoriesList)

            viewModel.getProducts(categoriesList[0].key)
            Log.e("MainActivity","category fetched")
        }

        viewModel.productList.observe(this){

            productList.clear()

            productList.addAll(it.products)

            productAdapter.setProduct(productList)
            Log.e("MainActivity","product fetched")
            Log.e("MainActivity","product items count ${productList.size}")



        }
        viewModel.errorMessage.observe(this) {
            Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
        }
        viewModel.loading.observe(this) {
            if (it) {
                binding.progressDialog.visibility = View.VISIBLE
            } else {
                binding.progressDialog.visibility = View.GONE
            }
        }
        viewModel.getAllCategories()
    }

    override fun onProductClicked(position: Int) {
        viewModel.getProducts(categoriesList[position].key)
    }
}