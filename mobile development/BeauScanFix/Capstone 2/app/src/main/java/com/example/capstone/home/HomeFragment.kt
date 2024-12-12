package com.example.capstone.home

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.capstone.R
import com.example.capstone.databinding.FragmentHomeBinding
import android.Manifest
import android.app.Activity
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.capstone.ViewModelFactory
import com.example.capstone.result.ResultActivity
import com.yalantis.ucrop.UCrop
import java.io.File

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private var imageUri: Uri? = null
    private lateinit var homeViewModel: HomeViewModel

    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { granted: Boolean ->
            if (granted) {
                Toast.makeText(requireContext(), R.string.permission_request_granted, Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(requireContext(), R.string.permission_request_denied, Toast.LENGTH_LONG).show()
            }
        }

    private fun hasCameraPermission(): Boolean =
        ContextCompat.checkSelfPermission(
            requireContext(),
            CAMERA_PERMISSION
        ) == PackageManager.PERMISSION_GRANTED

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory = ViewModelFactory.getInstance(requireContext())
        homeViewModel = ViewModelProvider(this, factory).get(HomeViewModel::class.java)

        homeViewModel.results.observe(viewLifecycleOwner, Observer { skincareResponse ->
            skincareResponse?.let {
                it.ingredients.forEach { result ->
                    Log.d("HomeFragment", "Predicted Rating: ${result.predicted_rating} for ${result.name}")
                }
            }
        })

        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                binding.progressIndicator.visibility = View.VISIBLE
            } else {
                binding.progressIndicator.visibility = View.GONE
            }
        })

        binding.galleryButton.setOnClickListener { openGallery() }
        binding.cameraButton.setOnClickListener { openCamera() }

        binding.analyzeButton.setOnClickListener {
            imageUri?.let { uri ->
                val filePath = getFilePathFromUri(uri)
                if (filePath != null) {
                    homeViewModel.analyzeImage(filePath)
                } else {
                    Toast.makeText(requireContext(), "Invalid image URI", Toast.LENGTH_SHORT).show()
                }
            } ?: run {
                Toast.makeText(requireContext(), "No image selected", Toast.LENGTH_SHORT).show()
            }
        }

        homeViewModel.results.observe(viewLifecycleOwner, Observer { skincareResponse ->
            skincareResponse?.let {
                val ingredientsList = it.ingredients.map { result -> result.name }
                val functionList = it.ingredients.map { result -> result.function }
                val ratings = it.ingredients.map { result -> result.predicted_rating }
                val benefits = it.predicted_skincare_categories

                val intent = Intent(requireContext(), ResultActivity::class.java)
                intent.putStringArrayListExtra("ingredients_list", ArrayList(ingredientsList))
                intent.putStringArrayListExtra("function_list", ArrayList(functionList))
                intent.putStringArrayListExtra("rating_list", ArrayList(ratings))
                intent.putStringArrayListExtra("benefits_list", ArrayList(benefits))
                startActivity(intent)
            } ?: run {
                Toast.makeText(requireContext(), "No result from API", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getFilePathFromUri(uri: Uri): String? {
        return try {
            val inputStream = requireContext().contentResolver.openInputStream(uri)
            val file = createTemporaryFile(requireContext())
            inputStream?.use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            Log.e("getFilePathFromUri", "Error: ${e.message}")
            null
        }
    }

    private fun openGallery() {
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            launchCrop(uri)
        } else {
            Log.d("Photo Picker", "No media selected")
        }
    }

    private fun openCamera() {
        if (!hasCameraPermission()) {
            permissionLauncher.launch(CAMERA_PERMISSION)
        } else {
            val cameraIntent = Intent(requireContext(), CameraActivity::class.java)
            cameraActivityLauncher.launch(cameraIntent)
        }
    }

    private val cameraActivityLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == CAMERAX_RESULT) {
            val uri = result.data?.getStringExtra(CameraActivity.EXTRA_CAMERAX_IMAGE)?.toUri()
            if (uri != null) {
                launchCrop(uri)
            }
        }
    }

    private fun launchCrop(uri: Uri) {
        val destinationUri = Uri.fromFile(File.createTempFile("cropped_", ".jpg", requireActivity().cacheDir))
        cropLauncher.launch(
            UCrop.of(uri, destinationUri)
                .getIntent(requireContext())
        )
    }

    private val cropLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val croppedUri = UCrop.getOutput(result.data!!)
            croppedUri?.let {
                imageUri = it
                displayImage()
            }
        }

    }

    private fun displayImage() {
        imageUri?.let {
            Log.d("Image URI", "displayImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val CAMERA_PERMISSION = Manifest.permission.CAMERA
        private const val CAMERAX_RESULT = 200
    }
}
