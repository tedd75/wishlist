package com.example.wishlist.fragments

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wishlist.adapters.ProductImagesAdapter
import com.example.wishlist.data.ProductDataBase
import com.example.wishlist.data.model.ProductEntity
import com.example.wishlist.databinding.FragmentEditBinding
import kotlin.concurrent.thread

const val ARG_EDIT_ID = "edit_id"

class EditFragment : Fragment() {

    private lateinit var binding: FragmentEditBinding
    private lateinit var adapter: ProductImagesAdapter
    private lateinit var db: ProductDataBase
    private var product: ProductEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentEditBinding.inflate(inflater, container, false).also {
            binding = it

        }.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        adapter = ProductImagesAdapter()

        val id = arguments?.getLong(ARG_EDIT_ID, -1) ?: -1
//        val id = requireArguments().getLong(ARG_EDIT_ID, -1)
        db = ProductDataBase.open(requireContext())
        if (id != -1L) {
            thread {
                product = db.product.getProduct(id)
                requireActivity().runOnUiThread {
                   // binding.address.setText(product?.address ?: "")
                    //    adapter.setSelection(product?.icon)
                }
            }
        }


        binding.images.apply {
            adapter = this@EditFragment.adapter
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        }

        binding.save.setOnClickListener {
            val product = product?.copy(
                name = binding.productName.text.toString(),
                address = binding.address.text.toString(),
                icon = resources.getResourceEntryName(adapter.selectedIdRes)
            ) ?: ProductEntity(
                name = binding.productName.text.toString(),
                address = binding.address.text.toString(),
                icon = resources.getResourceEntryName(adapter.selectedIdRes)

            )
            this.product = product
            thread {
                db.product.addProduct(product)
//                (activity as? Navigable)?.navigate(Navigable.Destination.List)
                parentFragmentManager?.popBackStack()
            }

        }
    }


    override fun onDestroy() {
        db.close()
        super.onDestroy()
    }
}