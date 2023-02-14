package com.example.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import com.example.profile.databinding.ActivityEditBinding

class EditActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditBinding
    private var imgUri: Uri? = null

    private val getResult =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()) {
            if(it.resultCode == Activity.RESULT_OK){
                imgUri = it.data?.data

                imgUri?.let {
                    val contentResolver = applicationContext.contentResolver
                    val takeFlags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    contentResolver.takePersistableUriPermission(it, takeFlags)

                    updateImage()
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        with(binding) {
            intent.extras?.let {
                imgUri = Uri.parse(it.getString(getString(R.string.key_image)))
                updateImage()
                etName.setText(it.getString(getString(R.string.key_name)))
                etEmail.setText(it.getString(getString(R.string.key_email)))
                etWebsite.setText(it.getString(getString(R.string.key_website)))
                etPhone.setText(it.getString(getString(R.string.key_phone)))
                etLatitude.setText(it.getDouble(getString(R.string.key_latitude)).toString())
                etLongitude.setText(it.getDouble(getString(R.string.key_longitude)).toString())
            }

            etEmail.apply {
                setOnFocusChangeListener { view, isFocused ->
                    if (isFocused) text?.let { setSelection(it.length) }
                }
            }
//            etEmail.setOnFocusChangeListener { view, isFocused ->
//                if (isFocused) etEmail.text?.let { etEmail.setSelection(it.length) }
//            }
            etWebsite.apply {
                setOnFocusChangeListener { view, isFocused ->
                    if (isFocused) text?.let { setSelection(it.length) }
                }
            }
            etPhone.apply {
                setOnFocusChangeListener { view, isFocused ->
                    if (isFocused) text?.let { setSelection(it.length) }
                }
            }
            etLatitude.apply {
                setOnFocusChangeListener { view, isFocused ->
                    if (isFocused) text?.let { setSelection(it.length) }
                }
            }
            etLongitude.apply {
                setOnFocusChangeListener { view, isFocused ->
                    if (isFocused) text?.let { setSelection(it.length) }
                }
            }
            btnSelectPhoto.setOnClickListener {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                    addCategory(Intent.CATEGORY_OPENABLE)
                    type = "image/jpeg"
                }
                getResult.launch(intent)
            }

        }
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_edit, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressedDispatcher.onBackPressed()
            R.id.action_save -> sendData()
        }

        return super.onOptionsItemSelected(item)
    }

    private fun updateImage() {
        binding.imgProfile.setImageURI(imgUri)
    }

    private fun sendData(){
        val intent = Intent()
         with(binding) {
             intent.apply {
                putExtra(getString(R.string.key_image), imgUri.toString())
                putExtra(getString(R.string.key_name), etName.text.toString())
                putExtra(getString(R.string.key_email), etEmail.text.toString())
                putExtra(getString(R.string.key_website), etWebsite.text.toString())
                putExtra(getString(R.string.key_phone), etPhone.text.toString())
                putExtra(getString(R.string.key_latitude), etLatitude.text.toString().toDouble())
                putExtra(getString(R.string.key_longitude), etLongitude.text.toString().toDouble())
            }

            setResult(RESULT_OK, intent)
            finish()
        }
    }
}