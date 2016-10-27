package com.thunder.lifecare.fragment.SubCategory.MyReport;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shockwave.pdfium.PdfDocument;
import com.shockwave.pdfium.PdfiumCore;
import com.thunder.lifecare.R;
import com.thunder.lifecare.util.AppUtills;
import com.thunder.lifecare.util.DateUtils;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ist-150 on 20/10/16.
 */
public class AddRecordFragment extends Fragment implements View.OnClickListener, com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener{

    private View mainView;
    private Context mContext;
    private String filePath, imageFilePath;
    private ImageView pdf_image, image_type;
    private ImageView invoice_image, prescription_image, report_image;
    private TextView invoice_text, prescription_text, report_text;
    private TextView pdf_image_text, file_name, report_created_on;
    private Button record_done;
    private Uri fileUri;
    private RelativeLayout invoice_layout, prescription_layout, report_layout;
    private TextInputLayout record_name_layout;
    private EditText record_name;
    private Boolean report_layout_clicked = false, prescription_layout_clicked = false, invoice_layout_clicked = false;
    final Calendar now = Calendar.getInstance();

    private ArrayList<String> docPaths = new ArrayList<>();

    public enum Single {
        INSTANCE;
        AddRecordFragment s = new AddRecordFragment();

        public AddRecordFragment getInstance() {
            if (s == null)
                return new AddRecordFragment();
            else return s;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();

        mainView = (ViewGroup)inflater.inflate(R.layout.add_record_fragment, container, false);
        AppUtills.setActionBarTitle("Add Reminder","", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), true);

        filePath = getArguments().getString("FilePath");
        imageFilePath = getArguments().getString("ImageFilePath");
        initView(mainView);

        return  mainView;
    }

    private void initView(View view) {

        pdf_image_text = (TextView) view.findViewById(R.id.pdf_image_text);
        pdf_image = (ImageView) view.findViewById(R.id.pdf_image);
        image_type = (ImageView) view.findViewById(R.id.image_type);
        file_name = (TextView) view.findViewById(R.id.file_name);
        report_created_on = (TextView) view.findViewById(R.id.report_created_on);

        invoice_layout = (RelativeLayout) view.findViewById(R.id.invoice_layout);
        prescription_layout = (RelativeLayout) view.findViewById(R.id.prescription_layout);
        report_layout = (RelativeLayout) view.findViewById(R.id.report_layout);
        record_name_layout = (TextInputLayout) view.findViewById(R.id.record_name_layout);;
        record_name = (EditText) view.findViewById(R.id.record_name);
        record_done = (Button) view.findViewById(R.id.record_done);

        invoice_image = (ImageView) view.findViewById(R.id.invoice_image);
        prescription_image = (ImageView) view.findViewById(R.id.prescription_image);
        report_image = (ImageView) view.findViewById(R.id.report_image);
        invoice_text = (TextView) view.findViewById(R.id.invoice_text);
        prescription_text = (TextView) view.findViewById(R.id.prescription_text);
        report_text = (TextView) view.findViewById(R.id.report_text);

        pdf_image.setOnClickListener(this);
        record_done.setOnClickListener(this);
        report_created_on.setOnClickListener(this);
        report_layout.setOnClickListener(this);
        prescription_layout.setOnClickListener(this);
        invoice_layout.setOnClickListener(this);

        report_created_on.setText(new SimpleDateFormat("dd/MM/yyyy").format(new Date()));
        report_created_on.setTextColor(getResources().getColor(R.color.app_color));

        if (filePath != null) {
            pdf_image_text.setText("Selected PDF");
            String fileName = filePath.substring(filePath.lastIndexOf("/")+1, filePath.length());
            file_name.setText(fileName);
            fileUri = Uri.fromFile(new File(filePath));
            generateImageFromPdf(fileUri);
            image_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_pdf_img));
        } else if (imageFilePath != null ){
            File imgFile = new  File(imageFilePath);
            if(imgFile.exists()){
                pdf_image_text.setText("Selected Image");
                String fileName = imageFilePath.substring(imageFilePath.lastIndexOf("/")+1, imageFilePath.length());
                file_name.setText(fileName);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                pdf_image.setImageBitmap(myBitmap);
                fileUri = Uri.fromFile(new File(imageFilePath));
                image_type.setImageDrawable(getResources().getDrawable(R.drawable.ic_photo));
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.record_done:
                break;
            case R.id.pdf_image:
                if (pdf_image_text.getText().toString().equalsIgnoreCase("Selected PDF")) {
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setDataAndType(fileUri, "application/pdf");
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else if (pdf_image_text.getText().toString().equalsIgnoreCase("Selected Image")) {
                    AppUtills.showPhoto(getActivity(), fileUri);
                }
                break;
            case R.id.report_created_on:
                openDateDialog(report_created_on);
                break;
            case R.id.report_layout:
                if(!report_layout_clicked) {
                    report_image.setImageResource(R.mipmap.ic_launcher);
                    report_text.setTextColor(getResources().getColor(R.color.app_color));
                    report_layout_clicked = true;
                } else {
                    report_image.setImageResource(R.drawable.my_report);
                    report_text.setTextColor(getResources().getColor(R.color.black_light_Color));
                    report_layout_clicked = false;
                }
                prescription_image.setImageResource(R.drawable.my_report);
                prescription_text.setTextColor(getResources().getColor(R.color.black_light_Color));
                prescription_layout_clicked = false;
                invoice_image.setImageResource(R.drawable.my_report);
                invoice_text.setTextColor(getResources().getColor(R.color.black_light_Color));
                invoice_layout_clicked = false;
                break;
            case R.id.prescription_layout:
                if(!prescription_layout_clicked) {
                    prescription_image.setImageResource(R.mipmap.ic_launcher);
                    prescription_text.setTextColor(getResources().getColor(R.color.app_color));
                    prescription_layout_clicked = true;
                } else {
                    prescription_image.setImageResource(R.drawable.my_report);
                    prescription_text.setTextColor(getResources().getColor(R.color.black_light_Color));
                    prescription_layout_clicked = false;
                }
                report_image.setImageResource(R.drawable.my_report);
                report_text.setTextColor(getResources().getColor(R.color.black_light_Color));
                report_layout_clicked = false;
                invoice_image.setImageResource(R.drawable.my_report);
                invoice_text.setTextColor(getResources().getColor(R.color.black_light_Color));
                invoice_layout_clicked = false;
                break;
            case R.id.invoice_layout:
                if(!invoice_layout_clicked) {
                    invoice_image.setImageResource(R.mipmap.ic_launcher);
                    invoice_text.setTextColor(getResources().getColor(R.color.app_color));
                    invoice_layout_clicked = true;
                } else {
                    invoice_image.setImageResource(R.drawable.my_report);
                    invoice_text.setTextColor(getResources().getColor(R.color.black_light_Color));
                    invoice_layout_clicked = false;
                }
                report_image.setImageResource(R.drawable.my_report);
                report_text.setTextColor(getResources().getColor(R.color.black_light_Color));
                report_layout_clicked = false;
                prescription_image.setImageResource(R.drawable.my_report);
                prescription_text.setTextColor(getResources().getColor(R.color.black_light_Color));
                prescription_layout_clicked = false;
                break;
        }
    }

    private void openDateDialog(final TextView txtDate) {
        int day, month, year;
        if (!"".equals(txtDate.getText().toString().trim())) {
            String date = txtDate.getText().toString();
            int indexA = date.indexOf("/");
            int indexB = date.lastIndexOf("/");
            day = Integer.parseInt(date.substring(0, indexA));
            month = Integer.parseInt(date.substring(indexA + 1, indexB));
            year = Integer.parseInt(date.substring(indexB + 1, date.length()));
        } else {
            day = now.get(Calendar.DAY_OF_MONTH);
            month = now.get(Calendar.MONTH);
            year = now.get(Calendar.YEAR);
        }
        com.wdullaer.materialdatetimepicker.date.DatePickerDialog dpd = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                AddRecordFragment.this,
                year,
                month,
                day
        );
        dpd.show(getActivity().getFragmentManager(), "Datepickerdialog");
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        StringBuilder dateBuilder = new StringBuilder();
        dateBuilder.append(year).append("-").append((monthOfYear + 1)).append("-").append(dayOfMonth);
        String todayDate = dateBuilder.toString();
        if (TextUtils.isEmpty(todayDate)) {
            todayDate = DateUtils.getCurrentDate();
        }
        String date = DateUtils.changedDateFormat(todayDate);
        report_created_on.setText(date);
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
            pdf_image.setImageBitmap(bmp);
//            saveImage(bmp);
            pdfiumCore.closeDocument(pdfDocument);
        } catch(Exception e) {
            //todo with exception
            e.printStackTrace();
        }
    }

}
