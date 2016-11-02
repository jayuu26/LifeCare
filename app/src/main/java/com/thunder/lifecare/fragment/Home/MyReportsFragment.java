package com.thunder.lifecare.fragment.Home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.thunder.lifecare.Listener.PageSelectedListener;
import com.thunder.lifecare.R;
import com.thunder.lifecare.adapter.ViewPagerAdapter;
import com.thunder.lifecare.camera.DgCamActivity;
import com.thunder.lifecare.constant.AppConstants;
import com.thunder.lifecare.constant.PhotoConstant;
import com.thunder.lifecare.customlayout.SlidingTabLayout;
import com.thunder.lifecare.fragment.BaseHomeFragment;
import com.thunder.lifecare.fragment.SubCategory.MyReport.AddRecordFragment;
import com.thunder.lifecare.fragment.SubCategory.MyReport.AddReminderFragment;
import com.thunder.lifecare.fragment.SubCategory.MyReport.ReminderFragment;
import com.thunder.lifecare.fragment.SubCategory.MyReport.ReportsFragment;
import com.thunder.lifecare.util.AppLog;
import com.thunder.lifecare.util.AppUtills;
import com.thunder.lifecare.util.DateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

/**
 * Created by ist-150 on 15/10/16.
 */
public class MyReportsFragment extends Fragment implements View.OnClickListener{

    private String TAG = this.getClass().getName();
    private View mainView;
    private ViewPager viewPager;
    private SlidingTabLayout tabLayout;
//    private FloatingActionButton fab_button;
//    private FloatingActionButton fab1, fab2, fab3;
    private ImageView down_button;
    private Context mContext;
    private Animation show_fab_1, hide_fab_1;
    private Animation show_fab_2, hide_fab_2;
    private Animation show_fab_3, hide_fab_3;
    private Animation record_dialog_anim;
    private RelativeLayout record_dialog;
    private FrameLayout record_frame;
    private ImageView open_camera, open_galley, upload_file;
    private ArrayList<String> docPaths = new ArrayList<>();
    private static final int PICK_Camera_IMAGE = 2;
    public static final int REQUEST_GALLERY_PHOTO = 0x479;
    private File destination;

    FloatingActionMenu materialDesignFAM;
    com.github.clans.fab.FloatingActionButton floatingActionButton1, floatingActionButton2, floatingActionButton3;


    //Save the FAB's active status
    //false -> fab = close
    //true -> fab = open
    private boolean FAB_Status = false;

    public enum Single {
        INSTANCE;
        MyReportsFragment s = new MyReportsFragment();

        public MyReportsFragment getInstance() {
            if (s == null)
                return new MyReportsFragment();
            else return s;
        }
    }


    @Override
    public void setMenuVisibility(boolean menuVisible) {
        super.setMenuVisibility(menuVisible);

        AppLog.i(TAG," setMenuVisibility "+menuVisible);
        try {
            if(menuVisible && getActivity()!=null && ((AppCompatActivity) getActivity()).getSupportActionBar()!=null)
                AppUtills.setActionBarTitle("Reports","Sub-Home", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        mainView = (ViewGroup) inflater.inflate(R.layout.my_reports_layout, container, false);
//        BaseHomeFragment.Single.INSTANCE.getInstance().initListner(this);
        initView(mainView);

        return mainView;
    }


    private void initActionButtonView(View view){

        materialDesignFAM = (FloatingActionMenu) view.findViewById(R.id.material_design_android_floating_action_menu);
        floatingActionButton1 = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item1);
        floatingActionButton2 = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item2);
        floatingActionButton3 = (com.github.clans.fab.FloatingActionButton) view.findViewById(R.id.material_design_floating_action_menu_item3);

        floatingActionButton1.setColorNormalResId(R.color.colorPrimary);
        floatingActionButton2.setColorNormalResId(R.color.colorPrimary);
        floatingActionButton3.setColorNormalResId(R.color.colorPrimary);

        floatingActionButton1.setOnClickListener(this);
        floatingActionButton2.setOnClickListener(this);
        floatingActionButton3.setOnClickListener(this);
    }
    private void initView(View mainView) {

        viewPager = (ViewPager) mainView.findViewById(R.id.tabanim_viewpager);
        setupViewPager(viewPager);
        tabLayout = (SlidingTabLayout) mainView.findViewById(R.id.tabanim_tabs);
        tabLayout.setViewPager(viewPager);

        initActionButtonView(mainView);

//        fab_button = (FloatingActionButton) mainView.findViewById(R.id.fab_add_reports);
//        fab1 = (FloatingActionButton) mainView.findViewById(R.id.fab_1);
//        fab2 = (FloatingActionButton) mainView.findViewById(R.id.fab_2);
//        fab3 = (FloatingActionButton) mainView.findViewById(R.id.fab_3);

        record_frame = (FrameLayout) mainView.findViewById(R.id.record_frame);
        record_dialog = (RelativeLayout) mainView.findViewById(R.id.record_dialog);
        down_button = (ImageView) mainView.findViewById(R.id.down_button);
        open_camera = (ImageView) mainView.findViewById(R.id.open_camera);
        open_galley = (ImageView) mainView.findViewById(R.id.open_galley);
        upload_file = (ImageView) mainView.findViewById(R.id.upload_file);

        //Animations
        record_dialog_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.move);
        show_fab_1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab1_show);
        hide_fab_1 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab1_hide);
        show_fab_2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab2_show);
        hide_fab_2 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab2_hide);
        show_fab_3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab3_show);
        hide_fab_3 = AnimationUtils.loadAnimation(getActivity(), R.anim.fab3_hide);

        record_frame.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (record_dialog.getVisibility() == View.VISIBLE) {
                    record_dialog.setVisibility(View.GONE);
//                    fab_button.setVisibility(View.VISIBLE);
                }
                return false;
            }
        });

//        fab_button.setOnClickListener(this);
//        fab1.setOnClickListener(this);
//        fab2.setOnClickListener(this);
//        fab3.setOnClickListener(this);
        down_button.setOnClickListener(this);
        open_camera.setOnClickListener(this);
        open_galley.setOnClickListener(this);
        upload_file.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.material_design_floating_action_menu_item1:
                AppUtills.loadFragment(AddReminderFragment.Single.INSTANCE.getInstance(), getActivity(), R.id.container);
                break;
            case R.id.material_design_floating_action_menu_item2:
                record_dialog.setVisibility(View.VISIBLE);
                break;

//            case R.id.fab_add_reports:
//                if (FAB_Status == false) {
//                    //Display FAB menu
//                    displayFloatingActionButton();
//                    FAB_Status = true;
//                } else {
//                    //Close FAB menu
//                    hideFloatingActionButton();
//                    FAB_Status = false;
//                }
//                break;
            case R.id.fab_1:
                if (record_dialog.getVisibility() == View.GONE) {
                    record_dialog.setVisibility(View.VISIBLE);
//                    record_dialog.startAnimation(record_dialog_anim);
                }
                hideFloatingActionButton();
                FAB_Status = false;
//                fab_button.setVisibility(View.GONE);
                break;
            case R.id.fab_2:
                AppUtills.loadFragment(AddReminderFragment.Single.INSTANCE.getInstance(), getActivity(), R.id.container);
                break;
            case R.id.fab_3:
                Toast.makeText(getActivity(), "Under Construction", Toast.LENGTH_SHORT).show();
                break;
            case R.id.down_button:
                if (record_dialog.getVisibility() == View.VISIBLE) {
                    record_dialog.setVisibility(View.GONE);
//                    fab_button.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.open_camera:
                record_dialog.setVisibility(View.GONE);
//                fab_button.setVisibility(View.VISIBLE);
                boolean hasCameraPermission = (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED);
                if (!hasCameraPermission) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.CAMERA},
                            111);
                } else {
                    openCamera();
                }
                break;
            case R.id.open_galley:
                onGalleryBtnClicked();
                break;
            case R.id.upload_file:
                boolean hasPermission = (ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
                if (!hasPermission) {
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                            111);
                } else {
                    record_dialog.setVisibility(View.GONE);
//                    fab_button.setVisibility(View.VISIBLE);
                    FilePickerBuilder.getInstance().setMaxCount(1)
                            .setActivityTheme(R.style.AppThemeForFileSelector)
                            .pickDocument(this);
                }
                break;
        }

    }

    public void onPageSelected(int position) {
        AppUtills.setActionBarTitle("Reports","Sub - My Report", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), false);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());
        adapter.addFrag(ReportsFragment.Single.INSTANCE.getInstance(), "Reports");
        adapter.addFrag(ReminderFragment.Single.INSTANCE.getInstance(), "Reminders");
        viewPager.setAdapter(adapter);
    }

    private void displayFloatingActionButton() {
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
//        layoutParams.rightMargin += (int) (fab1.getWidth() * 1.7);
//        layoutParams.bottomMargin += (int) (fab1.getHeight() * 0.25);
//        fab1.setLayoutParams(layoutParams);
//        fab1.startAnimation(show_fab_1);
//        fab1.setClickable(true);
//
//        //Floating Action Button 2
//        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
//        layoutParams2.rightMargin += (int) (fab2.getWidth() * 1.5);
//        layoutParams2.bottomMargin += (int) (fab2.getHeight() * 1.5);
//        fab2.setLayoutParams(layoutParams2);
//        fab2.startAnimation(show_fab_2);
//        fab2.setClickable(true);
//
//        //Floating Action Button 3
//        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
//        layoutParams3.rightMargin += (int) (fab3.getWidth() * 0.25);
//        layoutParams3.bottomMargin += (int) (fab3.getHeight() * 1.7);
//        fab3.setLayoutParams(layoutParams3);
//        fab3.startAnimation(show_fab_3);
//        fab3.setClickable(true);
    }

    private void hideFloatingActionButton() {
//        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) fab1.getLayoutParams();
//        layoutParams.rightMargin -= (int) (fab1.getWidth() * 1.7);
//        layoutParams.bottomMargin -= (int) (fab1.getHeight() * 0.25);
//        fab1.setLayoutParams(layoutParams);
//        fab1.startAnimation(hide_fab_1);
//        fab1.setClickable(false);
//
//        //Floating Action Button 2
//        FrameLayout.LayoutParams layoutParams2 = (FrameLayout.LayoutParams) fab2.getLayoutParams();
//        layoutParams2.rightMargin -= (int) (fab2.getWidth() * 1.5);
//        layoutParams2.bottomMargin -= (int) (fab2.getHeight() * 1.5);
//        fab2.setLayoutParams(layoutParams2);
//        fab2.startAnimation(hide_fab_2);
//        fab2.setClickable(false);
//
//        //Floating Action Button 3
//        FrameLayout.LayoutParams layoutParams3 = (FrameLayout.LayoutParams) fab3.getLayoutParams();
//        layoutParams3.rightMargin -= (int) (fab3.getWidth() * 0.25);
//        layoutParams3.bottomMargin -= (int) (fab3.getHeight() * 1.7);
//        fab3.setLayoutParams(layoutParams3);
//        fab3.startAnimation(hide_fab_3);
//        fab3.setClickable(false);
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

    private void onGalleryBtnClicked() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_GALLERY_PHOTO);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        Log.d("MyReport", "resultcode: " + resultCode + "  requestcode: " + requestCode);
        if (requestCode == FilePickerConst.REQUEST_CODE_DOC) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                docPaths = new ArrayList<>();
                docPaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_DOCS));
                Toast.makeText(getActivity(), "Num of files selected: " + docPaths.get(0), Toast.LENGTH_SHORT).show();
                AddRecordFragment myFrag = AddRecordFragment.Single.INSTANCE.getInstance();
                Bundle args = new Bundle();
                args.putString("FilePath", docPaths.get(0));
                myFrag.setArguments(args);

                AppUtills.loadFragment(myFrag, getActivity(), R.id.container);
            }
        }
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

                    AddRecordFragment myFrag = AddRecordFragment.Single.INSTANCE.getInstance();
                    Bundle args = new Bundle();
                    args.putString("ImageFilePath", filePath);
                    myFrag.setArguments(args);

                    AppUtills.loadFragment(myFrag, getActivity(), R.id.container);
                }
            } else if (requestCode == REQUEST_GALLERY_PHOTO && data != null && data.getData() != null) {
                Uri _uri = data.getData();

                // User had pick an image.
                Cursor cursor = null;
                try {
                    cursor = mContext.getContentResolver().query(_uri, new String[] { MediaStore.MediaColumns.DATA }, null, null, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (cursor != null) {
                    cursor.moveToFirst();
                    final String galleryFilePath = cursor.getString(0);
                    cursor.close();

                    // copy gallery image file to our application image folder
                    if (galleryFilePath != null) {
                        try {
                            double galleryImageSize = new FileInputStream(galleryFilePath).available();
                            if (galleryImageSize > AppConstants.max_image_size) {
                                Toast.makeText(mContext, "Image size is too large", Toast.LENGTH_LONG).show();
                                return;
                            }
                            String imageName = (DateUtils.dateToString(new Date(), PhotoConstant.IMAGE_NAME_FORMAT)) + PhotoConstant.IMAGE_FORMAT;
                            File filePath1 = new File(PhotoConstant.IMAGES_DEFAULT_PATH, PhotoConstant.IMAGE_DIR);
                            if (!filePath1.exists()) {
                                filePath1.mkdir();
                            }
                            File imageFile = new File(PhotoConstant.IMAGES_DEFAULT_PATH + File.separator + PhotoConstant.IMAGE_DIR, imageName);
                            if (imageFile != null) {
                                boolean imageSaved = AppUtills.copyStream(new FileInputStream(galleryFilePath), new FileOutputStream(imageFile), imageFile.getAbsolutePath(), mContext);
                                if (imageSaved) {
                                    AddRecordFragment myFrag = AddRecordFragment.Single.INSTANCE.getInstance();
                                    Bundle args = new Bundle();
                                    args.putString("ImageFilePath", imageFile.toString());
                                    myFrag.setArguments(args);

                                    AppUtills.loadFragment(myFrag, getActivity(), R.id.container);
                                } else {
                                    Toast.makeText(mContext, "Insufficient Storage space", Toast.LENGTH_SHORT).show();
                                }
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(mContext, "Unsupported file", Toast.LENGTH_SHORT).show();
                }
            }
        }
//        addThemToView(docPaths);
    }

//    private void addThemToView(ArrayList<String> docPaths) {
//        ArrayList<String> filePaths = new ArrayList<>();
//
//        if(docPaths!=null)
//            filePaths.addAll(docPaths);
//
////        if(my_reports_recycler_view!=null) {
////            StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, OrientationHelper.VERTICAL);
////            layoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
////            my_reports_recycler_view.setLayoutManager(layoutManager);
////            my_reports_recycler_view.setItemAnimator(new DefaultItemAnimator());
////        }
//
//        Toast.makeText(getActivity(), "Num of files selected: "+ filePaths.get(0), Toast.LENGTH_SHORT).show();
//    }


}
