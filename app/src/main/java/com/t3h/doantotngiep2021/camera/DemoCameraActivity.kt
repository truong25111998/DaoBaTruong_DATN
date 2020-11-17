package com.t3h.doantotngiep2021.camera

import android.Manifest
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.graphics.SurfaceTexture
import android.hardware.Camera
import android.os.Bundle
import android.os.Environment
import android.view.TextureView
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.t3h.doantotngiep2021.R
import kotlinx.android.synthetic.main.activity_demo_camera.*
import java.io.File
import java.io.FileOutputStream


class DemoCameraActivity : AppCompatActivity(), TextureView.SurfaceTextureListener,
    View.OnClickListener {
    private var surface: SurfaceTexture? = null
    private var camera: Camera? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_demo_camera)

        tt_view.surfaceTextureListener = this

        btn_capture.setOnClickListener(this)
    }


    override fun onSurfaceTextureSizeChanged(p0: SurfaceTexture, width: Int, height: Int) {

    }

    override fun onSurfaceTextureUpdated(p0: SurfaceTexture) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
        return true
    }


    override fun onSurfaceTextureAvailable(
        surface: SurfaceTexture,
        width: Int, height: Int
    ) {
        this.surface = surface
        initCamera()
    }


    private fun checkAllPermissionCamera(): Boolean {
        val isPass = PermissionUtils.checkPermission(
            this,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if (isPass) {
            return true
        }
        val passAll = PermissionUtils.requestPermission(
            this, 1,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
        if (passAll) {
            AlertDialog.Builder(this)
                .setTitle("Confirm permission")
                .setMessage("If you use this function, you must go to setting app to grant some permissions")
                .setPositiveButton("Ok", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface, which: Int) {
                        startActivityForResult(
                            Intent(android.provider.Settings.ACTION_SETTINGS),
                            0
                        );
                    }
                })
                .setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {

                    }
                })
        }
        return false
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        for (grantResult in grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return
            }
        }
        initCamera()
    }

    private fun initCamera() {
        if (!checkAllPermissionCamera()) {
            return
        }

        camera =
            Camera.open(Camera.CameraInfo.CAMERA_FACING_BACK)
        //thong tin cua do phan gian anh
        val info = Camera.CameraInfo()


        //thong tin do phan gian hien thi
        val p = camera?.parameters
        p?.setPreviewSize(
            camera?.parameters?.supportedPreviewSizes?.get(1)!!.width,
            camera?.parameters?.supportedPreviewSizes?.get(1)!!.height
        )
        camera?.setDisplayOrientation(90)
        p?.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)


        p?.setPictureSize(
            camera?.parameters?.supportedPictureSizes?.get(0)?.width!!,
            camera?.parameters?.supportedPictureSizes?.get(0)?.height!!
        )


        camera?.parameters = p


        camera?.setPreviewTexture(surface)
        camera?.startPreview()
    }


    override fun onResume() {
        super.onResume()
        if (camera != null) {
            initCamera()
        }
    }

    override fun onPause() {
        camera?.stopPreview()
        camera?.release()
        super.onPause()
    }

    private fun rotateImage(source: Bitmap, angle: Float): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(angle)
        return Bitmap.createBitmap(
            source, 0, 0, source.width, source.height, matrix,
            true
        )
    }

    override fun onClick(v: View) {
        camera?.takePicture(null, null, object : Camera.PictureCallback {
            override fun onPictureTaken(data: ByteArray, camera: Camera?) {
                //chuyen mang byte thanh anh (Bitmap)
                var bitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
                bitmap = rotateImage(bitmap, 90f)

                val path = Environment.getExternalStorageDirectory().path + "/newImagwe.jpg"
                val out = FileOutputStream(path)
                //dua bitmap vao trong outputstream
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out)
                out.flush()
                out.close()

                //update go quay cho anh


                Toast.makeText(
                    this@DemoCameraActivity, "Finish capture",
                    Toast.LENGTH_SHORT
                ).show()


                //load image
                Glide.with(iv_img)
                    .load(File(path))
                    .into(iv_img)

                camera?.release()
                initCamera()
            }
        })
    }

}