package com.saturnnetwork.playlistmaker.medialibraries.ui.CreatePlaylist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.databinding.CreatePlaylistFragmentBinding
import com.saturnnetwork.playlistmaker.medialibraries.ui.MediaLibrariesViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.File
import java.io.FileOutputStream

class FragmentCreatePlaylist: Fragment() {

    /*
  Используем binding для доступа к View, а _binding только для:
  Инициализации в onCreateView()
  Обнуления в onDestroyView()
   */
    private var _binding: CreatePlaylistFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel: MediaLibrariesViewModel by viewModel()

    //регистрируем событие, которое вызывает photo picker
    val pickMedia =
        registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            //обрабатываем событие выбора пользователем фотографии
            if (uri != null) {
                //binding.addPicture.setImageURI(uri)
                viewModel.setCoverUri(saveImageToPrivateStorage(uri))
            } else {
                //println("No media selected")
            }
        }

    private fun saveImageToPrivateStorage(uri: Uri): Uri {
        //создаём экземпляр класса File, который указывает на нужный каталог
        val filePath =
            File(requireContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "cover")
        //создаем каталог, если он не создан
        if (!filePath.exists()){
            filePath.mkdirs()
        }
        val number = (100..999).random()
        //создаём экземпляр класса File, который указывает на файл внутри каталога
        val file = File(filePath, "cover_${number}.jpg")
        // создаём входящий поток байтов из выбранной картинки
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        // создаём исходящий поток байтов в созданный выше файл
        val outputStream = FileOutputStream(file)
        // записываем картинку с помощью BitmapFactory
        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)
        return Uri.fromFile(file)

    }



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CreatePlaylistFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val simpleTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                val text = s?.toString().orEmpty()
                viewModel.setPlaylistName(text)

            }
        }

        binding.playlistName.addTextChangedListener(simpleTextWatcher)

        viewModel.setDefaultCoverIfEmpty("android.resource://${requireContext().packageName}/${R.drawable.playlist_add_photo}".toUri())

        binding.addPicture.setOnClickListener {
            pickMedia.launch(
                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
            )
        }

        binding.btnBack.setOnClickListener {
            val isNameNotBlank = binding.playlistName.text?.toString()?.isNotBlank() ?: false
            val isDescriptionNotBlank = binding.playlistDescription.text?.toString()?.isNotBlank() ?: false

            if (binding.addPicture.tag != 0 || isNameNotBlank || isDescriptionNotBlank) {
                val confirmDialog = MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Завершить создание плейлиста?")
                    .setMessage("Все несохраненные данные будут потеряны")
                    .setNeutralButton("Отмена") { dialog, which ->
                        // ничего не делаем
                    }.setPositiveButton("Завершить") { dialog, which ->
                        // сохраняем изменения и выходим
                        // save()
                        findNavController().popBackStack()
                    }.show()
            } else {
                findNavController().popBackStack()
            }
        }

        binding.createPlaylistButton.setOnClickListener {
            val isNameNotBlank = binding.playlistName.text?.toString()?.isNotBlank() ?: false
            if (isNameNotBlank) {
                viewModel.writePlaylistToDatabase(
                    name = binding.playlistName.text.toString(),
                    description = binding.playlistDescription.text.toString()
                )
            }
        }

        lifecycleScope.launch {
            viewModel.cPViewState.collectLatest { state ->
                if (state.createPlaylist) {
                    Toast.makeText(requireContext(),
                        "Плейлист ${binding.playlistName.text} создан",
                        Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }

                binding.addPicture.tag = state.playListCoverTag
                binding.addPicture.setImageURI(state.playListCover)
                if (state.createBtnActive) {
                    binding.createPlaylistButton.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_create_playlist_active)
                    binding.createPlaylistButton.isEnabled = true
                } else {
                    binding.createPlaylistButton.background =
                        ContextCompat.getDrawable(requireContext(), R.drawable.bg_create_playlist)
                    binding.createPlaylistButton.isEnabled = false
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}