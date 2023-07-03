package com.example.jalan_tuban_mobile.ui

import android.app.Activity
import android.content.ContentProviderClient
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.jalan_tuban_mobile.Application.RoadApp
import com.example.jalan_tuban_mobile.R
import com.example.jalan_tuban_mobile.databinding.FragmentSecondBinding
import com.example.jalan_tuban_mobile.model.Road
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions

class SecondFragment : Fragment(), OnMapReadyCallback, GoogleMap.OnMarkerDragListener {

    private var _binding: FragmentSecondBinding? = null
    private val binding get() = _binding!!
    private lateinit var applicationContext: Context
    private val roadViewModel: RoadViewModel by viewModels {
        RoadViewModelFactory((applicationContext as RoadApp).repository)
    }
    private val args : SecondFragmentArgs by navArgs()
    private var road: Road? = null
    private lateinit var mMap: GoogleMap
    private var currentLatLang: LatLng? = null
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val cameraRequestCode = 2

    override fun onAttach(context: Context) {
        super.onAttach(context)
        applicationContext = requireContext().applicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentSecondBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        road = args.road
        // kita cek jika road null maka tampilan default nambah jalan rusak
        // jika road tidak null tampilan sedikit berubah ada tombol hapus dan ubah

        if (road != null){
            binding.deleteButton.visibility = View.VISIBLE
            binding.saveButton.text = "Ubah"
            binding.nameeditText.setText(road?.name)
            binding.addreseditText.setText(road?.address)
        }

        //binding google map
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        checkCameraPermission()

        val name = binding.nameeditText.text
        val address = binding.addreseditText.text
        binding.saveButton.setOnClickListener {
            if (name.isEmpty()) {
                Toast.makeText(context, "Nama tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else if (address.isEmpty()){
                Toast.makeText(context,"Alamat tidak boleh kosong", Toast.LENGTH_SHORT).show()
            }else{
                if (road == null){
                    val road = Road(0, name.toString(), address.toString())
                    roadViewModel.insert(road)
                }else{
                    val road = Road(road?.id!!, name.toString(), address.toString())
                    roadViewModel.update(road)
                }
                findNavController().popBackStack()//untukdismiss halaman ini
            }
        }

        binding.deleteButton.setOnClickListener{
            road?.let { roadViewModel.delete(it) }
            findNavController().popBackStack()
        }

        binding.cameraButton.setOnClickListener{
            checkCameraPermission()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        val uiSettings = mMap.uiSettings
        uiSettings.isZoomControlsEnabled = true
        mMap.setOnMarkerDragListener(this)
    }

    override fun onMarkerDrag(p0: Marker) {}

    override fun onMarkerDragEnd(marker: Marker) {
        val newPosition = marker.position
        currentLatLang = LatLng(newPosition.latitude, newPosition.longitude)
        Toast.makeText(context, currentLatLang.toString(), Toast.LENGTH_SHORT).show()
    }

    override fun onMarkerDragStart(p0: Marker) {}

    private fun checkPermission(){
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(applicationContext)
        if (ContextCompat.checkSelfPermission(
            applicationContext,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED)
        {
            getCurrentLocation()
        }else{
            Toast.makeText(applicationContext, "Akses lokasi ditolak", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getCurrentLocation(){
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED)
        {
            return
        }
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null){
                    var latlang = LatLng(location.latitude, location.longitude)
                    currentLatLang = latlang
                    var title = "Marker"

                    val markerOption = MarkerOptions()
                        .position(latlang)
                        .title(title)
                        .draggable(true)
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.ill_construction))
                    mMap.addMarker(markerOption)
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlang, 15f))
                }
            }
    }

    private fun checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(
                applicationContext,
                    android.Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED){
            activity?.let {
                ActivityCompat.requestPermissions(
                    it,
                    arrayOf(android.Manifest.permission.CAMERA),
                    cameraRequestCode
                )
            }

        } else{
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(cameraIntent, cameraRequestCode)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == cameraRequestCode){
            val photo: Bitmap = data?.extras?.get("data") as Bitmap
            binding.photoImageView.setImageBitmap(photo)
        }
    }
}