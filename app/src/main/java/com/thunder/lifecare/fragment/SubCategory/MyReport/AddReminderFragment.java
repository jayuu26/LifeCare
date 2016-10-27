package com.thunder.lifecare.fragment.SubCategory.MyReport;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatSpinner;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.thunder.lifecare.R;
import com.thunder.lifecare.camera.DgCamActivity;
import com.thunder.lifecare.constant.AppConstants;
import com.thunder.lifecare.constant.PhotoConstant;
import com.thunder.lifecare.util.AppUtills;
import com.thunder.lifecare.util.DateUtils;
import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ist-150 on 20/10/16.
 */
public class AddReminderFragment extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener{

    private Context mContext;
    private View mainView;
    private TextInputLayout medicine_name_layout;
    private EditText medicine_name;
    private ImageView medicine_image,add, minus;
    private AppCompatSpinner duration_spinner;
    private TextView quantity, morning_time, afternoon_time, evening_time;
    private String selectedTime = "";
    private Button reminder_done;
    private Dialog imageSelectDialog;
    private ImageView cameraBtn, galleryBtn, cancleBtnDlg;
    private Uri fileUri;
    private static final int PICK_Camera_IMAGE = 5;
    public static final int REQUEST_GALLERY_PHOTO = 0x480;
    private File destination;

    final Calendar now = Calendar.getInstance();

    public enum Single {
        INSTANCE;
        AddReminderFragment s = new AddReminderFragment();

        public AddReminderFragment getInstance() {
            if (s == null)
                return new AddReminderFragment();
            else return s;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        mainView = (ViewGroup)inflater.inflate(R.layout.reminder_frag, container, false);
        AppUtills.setActionBarTitle("Add Reminder","", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), true);

        initView(mainView);

        return  mainView;
    }

    private void initView(View mainView) {
        medicine_name_layout = (TextInputLayout) mainView.findViewById(R.id.medicine_name_layout);
        medicine_name = (EditText) mainView.findViewById(R.id.medicine_name);
        medicine_image = (ImageView) mainView.findViewById(R.id.medicine_image);
        add = (ImageView) mainView.findViewById(R.id.add);
        minus = (ImageView) mainView.findViewById(R.id.minus);
        duration_spinner = (AppCompatSpinner) mainView.findViewById(R.id.duration_spinner);
        quantity = (TextView) mainView.findViewById(R.id.quantity);
        morning_time = (TextView) mainView.findViewById(R.id.morning_time);
        afternoon_time = (TextView) mainView.findViewById(R.id.afternoon_time);
        evening_time = (TextView) mainView.findViewById(R.id.evening_time);
        reminder_done = (Button) mainView.findViewById(R.id.reminder_done);

        medicine_image.setOnClickListener(this);
        add.setOnClickListener(this);
        minus.setOnClickListener(this);
        morning_time.setOnClickListener(this);
        afternoon_time.setOnClickListener(this);
        evening_time.setOnClickListener(this);
        reminder_done.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.reminder_done:
                break;
            case R.id.medicine_image:
                if (fileUri == null) {
                    openPicDialog();
                } else {
                    AppUtills.showPhoto(getActivity(), fileUri);
                }
                break;
            case R.id.imageView_camera_dlg:
                openCamera();
                imageSelectDialog.dismiss();
                break;
            case R.id.imageView_gallery_dlg:
                onGalleryBtnClicked();
                imageSelectDialog.dismiss();
                break;
            case R.id.cancelImgDlg:
                imageSelectDialog.dismiss();
                break;
            case R.id.add:
                double qua = Double.parseDouble(quantity.getText().toString());
                qua = qua + 0.25;
                quantity.setText(qua+"");
                break;
            case R.id.minus:
                double qua2 = Double.parseDouble(quantity.getText().toString());
                if(qua2 >= 0.25) {
                    qua2 = qua2 - 0.25;
                }
                quantity.setText(qua2+"");
                break;
            case R.id.morning_time:
                selectedTime = "morningTime";
                openTimeDialog(morning_time);
                break;
            case R.id.afternoon_time:
                selectedTime = "afternoonTime";
                openTimeDialog(evening_time);
                break;
            case R.id.evening_time:
                selectedTime = "eveningTime";
                openTimeDialog(evening_time);
                break;
        }
    }

    private void openPicDialog() {
        imageSelectDialog = new Dialog(mContext, R.style.MyDialogTheme);
        imageSelectDialog.setCancelable(true);
//        imageSelectDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        imageSelectDialog.setTitle("Select Option");
        imageSelectDialog.setContentView(R.layout.image_option_dialog);
        cameraBtn = (ImageView) imageSelectDialog.findViewById(R.id.imageView_camera_dlg);
        galleryBtn = (ImageView) imageSelectDialog.findViewById(R.id.imageView_gallery_dlg);
        cancleBtnDlg = (ImageView) imageSelectDialog.findViewById(R.id.cancelImgDlg);
        cancleBtnDlg.setOnClickListener(this);
        cameraBtn.setOnClickListener(this);
        galleryBtn.setOnClickListener(this);
        imageSelectDialog.show();
    }

    private void openTimeDialog(final TextView txtTime) {
        int minutes, hours;
        if (!"".equals(txtTime.getText().toString().trim())) {
            String time = txtTime.getText().toString();
            int indexA = time.indexOf(":");
//			int indexB = time.indexOf(" ");
            hours = Integer.parseInt(time.substring(0, indexA));
            minutes = Integer.parseInt(time.substring(indexA + 1, time.length()));
        } else {
            hours = now.get(Calendar.HOUR);
            minutes = now.get(Calendar.MINUTE);
        }
        TimePickerDialog tpd = TimePickerDialog.newInstance(AddReminderFragment.this,
                hours,
                minutes,
                true);
        tpd.show(getActivity().getFragmentManager(), "TimePickerDialog");
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {
        String time = hourOfDay + ":" + minute;
        if (selectedTime.equalsIgnoreCase("morningTime")){
            morning_time.setText(time);
        } else if (selectedTime.equalsIgnoreCase("afternoonTime")) {
            afternoon_time.setText(time);
        } else if (selectedTime.equalsIgnoreCase("eveningTime")) {
            evening_time.setText(time);
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

    private void onGalleryBtnClicked() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, REQUEST_GALLERY_PHOTO);
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
                    medicine_image.setImageBitmap(myBitmap);
                    fileUri = Uri.fromFile(destination);
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
                                    Bitmap myBitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
                                    medicine_image.setImageBitmap(myBitmap);
                                    fileUri = Uri.fromFile(imageFile);
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
    }

}
