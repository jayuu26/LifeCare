package com.thunder.lifecare.fragment.Registration;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.RelativeLayout;

import com.thunder.lifecare.R;
import com.thunder.lifecare.fragment.BaseHomeFragment;
import com.thunder.lifecare.util.AppUtills;
import com.thunder.lifecare.util.PreferenceHelper;
import com.toptoche.searchablespinnerlibrary.SearchableSpinner;
import android.support.design.widget.TextInputLayout;
import android.widget.EditText;
import android.widget.Toast;

public class MobileLoginFragment extends Fragment implements View.OnClickListener {


    public enum Single {
        INSTANCE;
        MobileLoginFragment s = new MobileLoginFragment();

        public MobileLoginFragment getInstance() {
            if (s == null)
                return new MobileLoginFragment();
            else return s;
        }
    }
    private Context mContext;

    private LinearLayout mobileNumberLayout;
    private TextView text;
    private RelativeLayout layout1;
    private SearchableSpinner spinner;
    private TextInputLayout inputLayoutName;
    private LinearLayout otpLayout;
    private RelativeLayout layout2;
    private TextInputLayout otp;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mContext = getActivity();
        return inflater.inflate(R.layout.mobile_login_layout, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mobileNumberLayout = (LinearLayout) view.findViewById(R.id.mobileNumberLayout);
        text = (TextView) view.findViewById(R.id.text);
        layout1 = (RelativeLayout) view.findViewById(R.id.layout1);
        spinner = (SearchableSpinner) view.findViewById(R.id.spinner);
        inputLayoutName = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        view.findViewById(R.id.getOTP).setOnClickListener(this);
        otpLayout = (LinearLayout) view.findViewById(R.id.otpLayout);
        layout2 = (RelativeLayout) view.findViewById(R.id.layout2);
        otp = (TextInputLayout) view.findViewById(R.id.otp);
        view.findViewById(R.id.submit_otp).setOnClickListener(this);
    }

    private EditText getMobileNo(){
        return (EditText) getView().findViewById(R.id.mobile_no);
    }

    private EditText getOTPNo(){
        return (EditText) getView().findViewById(R.id.otp_no);
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.getOTP:
                //TODO implement
                String mobile_no = getMobileNo().getText()+"";
                if(AppUtills.isValidMobileNumber(mobile_no)){
                    otpLayout.setVisibility(View.VISIBLE);
                }else{
                    Toast.makeText(mContext, "Check Your Mobile Number.", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.submit_otp:
                //TODO implement
                String otp = getOTPNo().getText()+"";
                if(AppUtills.isValidOTP(otp)){
                    PreferenceHelper preferenceHelper = new PreferenceHelper(getActivity());
                    preferenceHelper.setFirstTimeLaunch(true);
                    AppUtills.loadFragment(BaseHomeFragment.Single.INSTANCE.getInstance(), getActivity(), R.id.container);

                }else{
                    Toast.makeText(mContext, "Check Your OTP.", Toast.LENGTH_SHORT).show();
                }

                break;
        }
    }
}
