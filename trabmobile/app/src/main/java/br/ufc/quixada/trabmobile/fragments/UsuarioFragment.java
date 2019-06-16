package br.ufc.quixada.trabmobile.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;

import br.ufc.quixada.trabmobile.ActivityCallback;
import br.ufc.quixada.trabmobile.R;
import br.ufc.quixada.trabmobile.models.Usuario;
import br.ufc.quixada.trabmobile.utils.Utils;

import static android.app.Activity.RESULT_OK;

public class UsuarioFragment extends Fragment {
    private ActivityCallback mCallback;
    private FirebaseAuth mAuth;
    private DatabaseReference uReference;
    private Usuario usuario;
    public static UsuarioFragment newInstance() { return new UsuarioFragment(); }

    private TextView informacoes;

    private Button bTirarFoto;
    private ImageView imageView;
    private static final int CAMERA_REQUEST_CODE = 1;

    private StorageReference mStorage;
    private ProgressDialog mProgress;
    private Context context;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        View root = inflater.inflate(R.layout.fragment_usuario, container, false);
        context = root.getContext();
        mStorage = FirebaseStorage.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        informacoes = root.findViewById(R.id.tv_informacoes);
        Button b = root.findViewById(R.id.b_mapa);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallback.abrirMapa();
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        uReference = database.getReference("usuario");
        uReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String mUserId = Utils.getLocalUserId(getContext());
                for(DataSnapshot i : dataSnapshot.getChildren()){
                    Usuario u = i.getValue(Usuario.class);
                    if(u.getId().equals(mUserId)){
                        usuario = u;
                        informacoes.setText("Nome: "+usuario.getNome()+"\n"+"E-mail: "+usuario.getEmail());
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        bTirarFoto = root.findViewById(R.id.tirarFoto);
        imageView = root.findViewById(R.id.verImagem);
        mProgress = new ProgressDialog(context);
        bTirarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, CAMERA_REQUEST_CODE);
            }
        });
        carregarImagem();
        return root;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mCallback = (ActivityCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mCallback = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        menu.findItem(R.id.action_logout).setVisible(false);
        menu.findItem(R.id.action_perfil).setVisible(false);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.action_voltar) {
            mCallback.abrirPrincipal();
        }

        return super.onOptionsItemSelected(item);
    }

    public void carregarImagem(){
        mProgress.setMessage("Carregando imagem...");
        mProgress.show();
        StorageReference storageReference = FirebaseStorage.getInstance().getReference();
        StorageReference photoReference= storageReference.child("Fotos/"+Utils.getLocalEmail(context));
        final long ONE_MEGABYTE = 1024 * 1024;
        photoReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                imageView.setImageBitmap(bmp);
                mProgress.dismiss();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Toast.makeText(context.getApplicationContext(), "Sem imagem de Perfil!!", Toast.LENGTH_LONG).show();
                mProgress.dismiss();
            }
        });
    }

    public Uri getImageUri(Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CAMERA_REQUEST_CODE && resultCode == RESULT_OK){
            mProgress.setMessage("Fazendo upload");
            mProgress.show();

            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            // imageBitmap = (Bitmap) data.getExtras().get("data");
            imageView.setImageBitmap(imageBitmap);

            Uri uri = getImageUri(imageBitmap);
            StorageReference filepath = mStorage.child("Fotos").child(Utils.getLocalEmail(context));
            filepath.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    mProgress.dismiss();
                    Toast.makeText(context, "upload feito", Toast.LENGTH_LONG).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    Toast.makeText(context.getApplicationContext(), "Falha ao carregar a imagem!!", Toast.LENGTH_LONG).show();
                    mProgress.dismiss();
                }
            });
        }
    }
}
