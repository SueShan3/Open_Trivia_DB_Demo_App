package com.example.opentriviadbdemoapp.item

import com.example.opentriviadbdemoapp.R
import com.example.opentriviadbdemoapp.bindingModel.CatalogBindingModel
import com.example.opentriviadbdemoapp.databinding.ItemCatalogBinding
import com.example.opentriviadbdemoapp.model.Catalog
import com.xwray.groupie.databinding.BindableItem

class CatalogItem (private val catalog: Catalog): BindableItem<ItemCatalogBinding>(){

    override fun bind(viewBinding: ItemCatalogBinding, position: Int){
        viewBinding.data = CatalogBindingModel(catalog)
    }

    override fun getLayout() = R.layout.item_catalog
}