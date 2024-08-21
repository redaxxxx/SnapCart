package com.android.developer.prof.reda.snapcart.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.android.developer.prof.reda.snapcart.model.BrandModel
import com.android.developer.prof.reda.snapcart.model.PopularItemModel
import com.android.developer.prof.reda.snapcart.model.SliderModel
import com.android.developer.prof.reda.snapcart.util.Resource
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class ExploreViewModel @Inject constructor(
    private val firebaseDatabase: FirebaseDatabase
) : ViewModel(){

    private val _banners = MutableStateFlow<Resource<List<SliderModel>>>(Resource.Unspecified())

    val banners : StateFlow<Resource<List<SliderModel>>>
        get() = _banners

    private val _brands = MutableStateFlow<Resource<List<BrandModel>>>(Resource.Unspecified())

    val brands : StateFlow<Resource<List<BrandModel>>>
        get() = _brands

    private val _popularItems = MutableStateFlow<Resource<List<PopularItemModel>>>(Resource.Unspecified())

    val popularItems: StateFlow<Resource<List<PopularItemModel>>>
        get() = _popularItems
    init {
        loadBanner()
        loadOfficialBrands()
        loadPopularItems()
    }

    private fun loadPopularItems(){
        runBlocking {
            _popularItems.emit(Resource.Loading())
        }

        val ref = firebaseDatabase.getReference("Items")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val listPopularItems = mutableListOf<PopularItemModel>()

                for (childSnapshot in snapshot.children){
                    val popularItem = childSnapshot.getValue(PopularItemModel::class.java)

                    if (popularItem != null){
                        listPopularItems.add(popularItem)
                    }
                }

                viewModelScope.launch {
                    _popularItems.emit(Resource.Success(listPopularItems))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                viewModelScope.launch {
                    _popularItems.emit(Resource.Failed(error.message))
                }
            }

        })
    }
    private fun loadOfficialBrands(){
        runBlocking {
            _banners.emit(Resource.Loading())
        }

        val ref = firebaseDatabase.getReference("Category")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val listBrand = mutableListOf<BrandModel>()

                for (childSnapshot in snapshot.children){
                    val brandModel = childSnapshot.getValue(BrandModel::class.java)
                    if (brandModel != null){
                        listBrand.add(brandModel)
                    }
                }

                viewModelScope.launch {
                    _brands.emit(Resource.Success(listBrand))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                viewModelScope.launch {
                    _brands.emit(Resource.Failed(error.message))
                }
            }

        })
    }
    private fun loadBanner(){

        runBlocking {
            _banners.emit(Resource.Loading())
        }

        val ref = firebaseDatabase.getReference("Banner")

        ref.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val bannerLists = mutableListOf<SliderModel>()

                for (childSnapshot in snapshot.children){
                    val sliderModel = childSnapshot.getValue(SliderModel::class.java)
                    if (sliderModel != null){
                        bannerLists.add(sliderModel)
                    }
                }

                viewModelScope.launch {
                    _banners.emit(Resource.Success(bannerLists))
                }
            }

            override fun onCancelled(error: DatabaseError) {
                viewModelScope.launch {
                    _banners.emit(Resource.Failed(error.toException().message.toString()))
                }
            }

        })
    }
}