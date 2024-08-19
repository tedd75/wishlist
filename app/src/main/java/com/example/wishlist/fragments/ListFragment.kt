package com.example.wishlist.fragments

import android.content.ContentValues
import android.content.Intent

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wishlist.Navigable
import com.example.wishlist.adapters.ProductsAdapter
import com.example.wishlist.adapters.SwipeToRemove
import com.example.wishlist.data.ProductDataBase
import com.example.wishlist.databinding.FragmentListBinding
import com.example.wishlist.model.Products
import kotlin.concurrent.thread


class ListFragment : Fragment() {

    //renderowanie widoku

    private lateinit var binding: FragmentListBinding
    private var adapter: ProductsAdapter? = null
    private lateinit var db: ProductDataBase

    private lateinit var cameraLauncher: ActivityResultLauncher<Uri>
    private var imageUri: Uri? = null

    private val onTakePhoto: (Boolean) -> Unit = { photography: Boolean ->
        if (!photography) {
            imageUri?.let {
                requireContext().contentResolver
                    .delete(it, null, null)
            }
        }
    }


override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    db = ProductDataBase.open(requireContext())

    cameraLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture(),
        onTakePhoto
    )


}


override fun onCreateView(
    inflater: LayoutInflater, container: ViewGroup?,
    savedInstanceState: Bundle?
): View? {

    return FragmentListBinding.inflate(
        inflater, container, false
    ).also {
        binding = it
    }.root
}

override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)
    adapter = ProductsAdapter().apply {
        onItemClick = {
            (activity as? Navigable)?.navigate(Navigable.Destination.Edit, it)
        }
    }

    loadData()

    //usuniecie
    binding.list.let {
        it.adapter = adapter
        it.layoutManager = LinearLayoutManager(requireContext())
        ItemTouchHelper(
            SwipeToRemove {
                adapter?.removeItem(it)?.let {
                    thread {
                        db.product.remove(it.id)
                    }
                }
            }
        ).attachToRecyclerView(it)
    }

    binding.btAdd.setOnClickListener {
        (activity as? Navigable)?.navigate(Navigable.Destination.Add)

    }
    binding.btSort.setOnClickListener {
        adapter?.sort()
    }

    binding.btCamera.setOnClickListener {
        createPicture()

    }


}

    private fun createPicture() {
        val imagesUri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        } else {
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        }


        val ct = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME, "photo.jpg")
            put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
        }
        imageUri = requireContext().contentResolver.insert(imagesUri, ct)
        if(imageUri !=null) {
            cameraLauncher.launch(imageUri)
        }
    }




//czytanie z bazy danych
fun loadData() = thread {
    val products = db.product.getAll().map { entity ->
        Products(
            entity.id,
            entity.name,
            entity.address.split("\n"),
            resources.getIdentifier(entity.icon, "drawable", requireContext().packageName)
        )
    }
    requireActivity().runOnUiThread {
        adapter?.replace(products)
    }
}

override fun onStart() {
    super.onStart()
    loadData()
}

override fun onDestroy() {
    db.close()
    super.onDestroy()
}
}

private fun <I> ActivityResultLauncher<I>.launch(cameraIntent: Intent) {

}
