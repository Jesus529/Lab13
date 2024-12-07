package com.vargas.kevin.lab13

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Camera
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    private lateinit var cameraExecutor: ExecutorService
    private lateinit var previewView: PreviewView
    private lateinit var imageCapture: ImageCapture
    private var lensFacing = CameraSelector.LENS_FACING_BACK // Variable para alternar entre la cámara frontal y trasera

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Inicializamos la vista de la cámara
        previewView = findViewById(R.id.preview)

        // Botón para abrir la galería
        val galleryButton: Button = findViewById(R.id.gallery_btn)
        galleryButton.setOnClickListener {
            // Navegar a GalleryActivity
            val intent = Intent(this, GalleryActivity::class.java)
            startActivity(intent)
        }

        // Botón para cambiar la cámara
        val switchButton: Button = findViewById(R.id.switch_btn)
        switchButton.setOnClickListener {
            toggleCamera() // Alternar entre la cámara frontal y trasera
        }

        // Botón para capturar una foto
        val captureButton: Button = findViewById(R.id.img_capture_btn)
        captureButton.setOnClickListener {
            captureImage()
        }

        // Configuración de la cámara
        startCamera()

        // Inicializamos el ejecutor para la cámara
        cameraExecutor = Executors.newSingleThreadExecutor()
    }

    // Iniciar la cámara y mostrar la vista previa
    private fun startCamera() {
        // Asegúrate de tener los permisos de cámara
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CAMERA
            ) == android.content.pm.PackageManager.PERMISSION_GRANTED
        ) {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(this)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()

                // Configura el selector de la cámara (trasera o frontal)
                val cameraSelector = CameraSelector.Builder()
                    .requireLensFacing(lensFacing) // Usamos la variable para elegir la cámara
                    .build()

                // Previsualización de la cámara
                val preview = Preview.Builder()
                    .build()
                    .also {
                        it.surfaceProvider = previewView.surfaceProvider
                    }

                // Inicialización de ImageCapture para tomar fotos
                imageCapture = ImageCapture.Builder().build()

                // Vinculamos el ciclo de vida de la cámara con la actividad
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        this as LifecycleOwner, cameraSelector, preview, imageCapture
                    )
                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }, ContextCompat.getMainExecutor(this))
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(android.Manifest.permission.CAMERA),
                REQUEST_CODE_CAMERA_PERMISSION
            )
        }
    }

    // Método para capturar una imagen
    private fun captureImage() {
        // Configuración para capturar una imagen con ImageCapture
        val outputOptions = ImageCapture.OutputFileOptions.Builder(createFile()).build()

        imageCapture.takePicture(
            outputOptions, cameraExecutor,
            object : ImageCapture.OnImageSavedCallback {
                override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                    // Imagen capturada con éxito
                    val msg = "Imagen guardada en: ${outputFileResults.savedUri}"
                    Toast.makeText(applicationContext, msg, Toast.LENGTH_SHORT).show()
                }

                override fun onError(exception: ImageCaptureException) {
                    // En caso de error al capturar la imagen
                    Toast.makeText(applicationContext, "Error al capturar la imagen", Toast.LENGTH_SHORT).show()
                }
            })
    }

    // Alternar entre la cámara trasera y la cámara frontal
    private fun toggleCamera() {
        lensFacing = if (lensFacing == CameraSelector.LENS_FACING_BACK) {
            CameraSelector.LENS_FACING_FRONT
        } else {
            CameraSelector.LENS_FACING_BACK
        }
        startCamera() // Reiniciar la cámara con la cámara seleccionada
    }

    override fun onDestroy() {
        super.onDestroy()
        cameraExecutor.shutdown()
    }

    companion object {
        private const val REQUEST_CODE_CAMERA_PERMISSION = 10
    }

    // Crear un archivo para guardar la imagen capturada
    private fun createFile(): java.io.File {
        val fileName = "photo_${System.currentTimeMillis()}.jpg"
        val storageDir = getExternalFilesDir(null)
        return java.io.File(storageDir, fileName)
    }
}
