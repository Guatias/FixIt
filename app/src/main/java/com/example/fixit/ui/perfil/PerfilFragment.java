package com.example.fixit.ui.perfil;

import static android.app.Activity.RESULT_OK;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fixit.Cliente_Activity;
import com.example.fixit.UserHelperClass;
import com.example.fixit.databinding.FragmentPerfilBinding;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PerfilFragment extends Fragment {

    private CircleImageView foto_perfil;
    private MaterialButton savebtn;
    private EditText perfil_nome;
    private EditText perfil_sobrenome;
    private EditText perfil_celular;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;

    private Uri uri_imagem;
    private String minha_uri = "";
    private StorageTask uploadTask;
    private StorageReference storage_foto_perfil_reference;

    private PerfilViewModel perfilViewModel;
    private FragmentPerfilBinding binding;
    private UserHelperClass user;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference().child("usuarios");
        storage_foto_perfil_reference = FirebaseStorage.getInstance().getReference("fotos");
        user = ((Cliente_Activity) getActivity()).getUser();

        perfilViewModel =
                new ViewModelProvider(this).get(PerfilViewModel.class);

        binding = FragmentPerfilBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        foto_perfil = binding.profilePic;
        savebtn = binding.profileSaveButton;
        perfil_nome = binding.profileName;
        perfil_sobrenome = binding.profileSurname;
        perfil_celular = binding.profilePhone;

        SimpleMaskFormatter smf2 = new SimpleMaskFormatter("(NN)NNNNN-NNNN");
        MaskTextWatcher mtw2 = new MaskTextWatcher(perfil_celular, smf2);
        perfil_celular.addTextChangedListener(mtw2);

        perfil_nome.setText(user.getNome());
        perfil_sobrenome.setText(user.getSobrenome());
        perfil_celular.setText(user.getCelular());

        savebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String perfil_nome_string = perfil_nome.getText().toString();
                String perfil_sobrenome_string = perfil_sobrenome.getText().toString();
                String perfil_celular_string = perfil_celular.getText().toString();

                if (TextUtils.isEmpty(perfil_celular_string)) {
                    perfil_celular.setError("Preencha com o numero de seu celular");
                    return;
                } else if (perfil_celular_string.length() != 14) {
                    perfil_celular.setError("Número Inválido");
                    return;
                }

                if (TextUtils.isEmpty(perfil_nome_string)) {
                    perfil_nome.setError("Preencha com seu nome");
                    return;
                }

                if (TextUtils.isEmpty(perfil_sobrenome_string)) {
                    perfil_sobrenome.setError("Preencha com seu sobrenome");
                    return;
                }

                uploadDadosUsuario(perfil_nome_string, perfil_sobrenome_string, perfil_celular_string);
            }
        });

        foto_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = CropImage.activity()
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setAspectRatio(1,1)
                        .getIntent(getContext());

                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        if (!user.getFoto().equals("nenhuma")) {
            Picasso.get().load(user.getFoto()).into(foto_perfil);
        }

        perfil_nome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    perfil_nome.setHint("");
                else
                    perfil_nome.setHint("Nome");
            }
        });

        perfil_sobrenome.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    perfil_sobrenome.setHint("");
                else
                    perfil_sobrenome.setHint("Sobrenome");
            }
        });

        perfil_celular.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus)
                    perfil_celular.setHint("");
                else
                    perfil_celular.setHint("Celular com DDD");
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                uri_imagem = result.getUri();
                foto_perfil.setImageURI(uri_imagem);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    private void uploadDadosUsuario(String perfil_nome_string, String perfil_sobrenome_string, String perfil_celular_string) {

        final ProgressDialog progressDialog = new ProgressDialog(((Cliente_Activity) getContext()));
        progressDialog.setTitle("Atualizando seus Dados");
        progressDialog.setMessage("Aguarde enquanto seus dados são atualizados!");
        progressDialog.show();

        if (uri_imagem != null) {

            final StorageReference arquivoRef = storage_foto_perfil_reference.child(user.getCpf() + ".jpg");

            uploadTask = arquivoRef.putFile(uri_imagem);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {

                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }

                    return arquivoRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {

                    if (task.isSuccessful()) {

                        Uri downloadUrl = task.getResult();
                        minha_uri = downloadUrl.toString();

                        HashMap<String, Object> usermap = new HashMap<>();
                        usermap.put("foto", minha_uri);
                        usermap.put("nome", perfil_nome_string);
                        usermap.put("sobrenome", perfil_sobrenome_string);
                        usermap.put("celular", perfil_celular_string);

                        databaseReference.child(user.getCpf()).updateChildren(usermap);

                        atualizarUsuario(progressDialog);

                    }

                }
            });

        } else {
            HashMap<String, Object> usermap = new HashMap<>();
            usermap.put("nome", perfil_nome_string);
            usermap.put("sobrenome", perfil_sobrenome_string);
            usermap.put("celular", perfil_celular_string);

            databaseReference.child(user.getCpf()).updateChildren(usermap);

            atualizarUsuario(progressDialog);

        }

    }

    private void atualizarUsuario(ProgressDialog progressDialog) {

        try {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("usuarios");
            reference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user.retrieveUserData((Map<String, Object>) dataSnapshot.getValue(), user.getEmail());
                    ((Cliente_Activity) getActivity()).setUser(user);
                    ((Cliente_Activity) getActivity()).carregarFoto();
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        } catch (Exception ex) {
            ((Cliente_Activity) getActivity()).showToast("Ocorreu um erro: " + ex.getMessage());
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}