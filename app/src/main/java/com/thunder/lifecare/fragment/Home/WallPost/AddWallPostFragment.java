package com.thunder.lifecare.fragment.Home.WallPost;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ImageView;
import android.widget.Toast;
import com.thunder.lifecare.GreenDao.daomodel.Category;
import com.thunder.lifecare.R;
import com.thunder.lifecare.adapter.WallAdapter.PostWallAdapter;
import com.thunder.lifecare.util.AppLog;
import com.thunder.lifecare.util.AppUtills;
import com.thunder.lifecare.util.DateUtils;
import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.thunder.lifecare.R;
import com.thunder.lifecare.camera.DgCamActivity;
import com.thunder.lifecare.constant.PhotoConstant;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import android.util.Log;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

/**
 * Created by ist-150 on 15/10/16.
 */
public class AddWallPostFragment extends Fragment implements View.OnClickListener {

    private String TAG = "WallPostHomeFragment";
    private View mainView;
    private Context mContext;
    private ImageView attach, camera, post, attach_image;
    private Uri fileUri;
    private static final int PICK_Camera_IMAGE = 5;
    private File destination;
    private ArrayList<String> docPaths = new ArrayList<>();
    private static String current_selected = "";

    public enum Single {
        INSTANCE;
        AddWallPostFragment s = new AddWallPostFragment();

        public AddWallPostFragment getInstance() {
            if (s == null)
                return new AddWallPostFragment();
            else return s;
        }
    }

    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        AppLog.i(TAG," setMenuVisibility "+menuVisible);
        try {
            if(menuVisible && getActivity()!=null && ((AppCompatActivity) getActivity()).getSupportActionBar()!=null)
                AppUtills.setActionBarTitle("Add Post","Sub-Home", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = (ViewGroup) inflater.inflate(R.layout.post_create_layout, container, false);
        mContext =getActivity();
        AppUtills.setActionBarTitle("Add Post","Sub-Home", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), true);
        initView(mainView);
        return mainView;
    }

    private void initView(View mainView) {
        attach = (ImageView) mainView.findViewById(R.id.attach);
        camera = (ImageView) mainView.findViewById(R.id.camera);
        post = (ImageView) mainView.findViewById(R.id.post);
        attach_image = (ImageView) mainView.findViewById(R.id.attach_image);

        attach.setOnClickListener(this);
        camera.setOnClickListener(this);
        post.setOnClickListener(this);
        attach_image.setOnClickListener(this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.attach:
                current_selected = "attach";
                boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            111);
                } else {
                    FilePickerBuilder.getInstance().setMaxCount(1)
                            .setActivityTheme(R.style.AppThemeForFileSelector)
                            .pickDocument(this);
                }
                break;
            case R.id.camera:
                current_selected = "camera";
                openCamera();
                break;
            case R.id.post:
                break;
            case R.id.attach_image:
                if (current_selected.equalsIgnoreCase("attach")){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(fileUri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (current_selected.equalsIgnoreCase("camera")){
                    AppUtills.showPhoto(getActivity(), fileUri);
                }
                break;
        }
    }

    private void openCamera() {
        String imageName = (DateUtils.dateToString(new Date(), PhotoConstant.IMAGE_NAME_FORMAT)) + PhotoConstant.IMAGE_FORMAT;
        File filePath1 = new File(PhotoConstant.IMAGES_DEFAULT_PATH, PhotoConstant.IMAGE_DIR);
        if (!filePath1.exists()) {
            filePath1.mkdir();
        }
        destination = new File(PhotoConstant.IMAGES_DEFAULT_PATH + File.separator + PhotoConstant.IMAGE_DIR, imageName);

        Intent cameraIntent = new Intent(getActivity(), DgCamActivity.class);
        Log.d("MyReport", destination.toString());
        cameraIntent.putExtra("destination", destination.toString());
        startActivityForResult(cameraIntent, PICK_Camera_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("MyReport", "resultcode: " + resultCode + "  requestcode: " + requestCode);
        if (resultCode == Activity.RESULT_OK) {
            String filePath = "";
            if (requestCode == PICK_Camera_IMAGE) {
                if (destination != null) {
                    filePath = destination.toString();
                    AppUtills.compressImage(getActivity(), filePath);
                    Bitmap bitmap = AppUtills.getBitmapFromFile(filePath, 400, 400);
                    try {
                        FileOutputStream fos = new FileOutputStream(filePath);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, fos);
                        fos.close();
                    } catch (NullPointerException e) {
                        Log.e("Photo Capture", "Error:", e);
                    } catch (FileNotFoundException e) {
                        Log.e("Photo Capture", "Error:", e);
                    } catch (IOException e) {
                        Log.e("Photo Capture", "Error:", e);
                    } catch (Exception e) {
                        System.out.println("Error in capturing" + e);
                        Log.e("Photo Capture", "Error:", e);
                    }
                    Bitmap myBitmap = BitmapFactory.decodeFile(destination.getAbsolutePath());
                    attach_image.setImageBitmap(myBitmap);
                    fileUri = Uri.fromFile(destination);
                }
            }
            Log.d("MyReport", "resultcode: " + resultCode + "  requestcode: " + requestCode);
            if (requestCode == FilePickerConst.REQUEST_CODE_DOC) {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    docPaths = new ArrayList<>();
                    docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                    Toast.makeText(getActivity(), "Num of files selected: " + docPaths.get(0), Toast.LENGTH_SHORT).show();
                    fileUri = Uri.fromFile(new File(docPaths.get(0)));
                    generateImageFromPdf(fileUri);
                }
            }
        }
    }

    private void generateImageFromPdf(Uri pdfUri) {
        int pageNumber = 0;
        PdfiumCore pdfiumCore = new PdfiumCore(getActivity());
        try {
            ParcelFileDescriptor fd = getActivity().getContentResolver().openFileDescriptor(pdfUri, "r");
            PdfDocument pdfDocument = pdfiumCore.newDocument(fd);
            pdfiumCore.openPage(pdfDocument, pageNumber);
            int width = pdfiumCore.getPageWidthPoint(pdfDocument, pageNumber);
            int height = pdfiumCore.getPageHeightPoint(pdfDocument, pageNumber);
            Bitmap bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            pdfiumCore.renderPageBitmap(pdfDocument, bmp, pageNumber, 0, 0, width, height);
            attach_image.setImageBitmap(bmp);
            pdfiumCore.closeDocument(pdfDocument);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
