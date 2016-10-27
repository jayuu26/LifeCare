package com.thunder.lifecare.fragment.SubCategory.Doctor;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.thunder.lifecare.GreenDao.daomodel.Category;
import com.thunder.lifecare.R;
import com.thunder.lifecare.adapter.CategoryListAdapter;
import com.thunder.lifecare.fragment.DetailFragment.SubCategoryDetailListFragment;
import com.thunder.lifecare.util.AppUtills;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ist-150 on 17/10/16.
 */
public class SubCategoryFragment extends Fragment implements CategoryListAdapter.CategoryActionListener{

    private View mainView;
    private RecyclerView recyclerView;
    private LinearLayoutManager layoutManager;
    private Context mContext;

    public enum Single {
        INSTANCE;
        SubCategoryFragment s = new SubCategoryFragment();

        public SubCategoryFragment getInstance() {
            if (s == null)
                return new SubCategoryFragment();
            else return s;
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mainView = (ViewGroup)inflater.inflate(R.layout.category_list_layout, container, false);
        AppUtills.setActionBarTitle("Doctors","Location Address", ((AppCompatActivity) getActivity()).getSupportActionBar(), getActivity(), true);

        initView(mainView);
        return  mainView;
    }

    private void initView(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.categoryList);
        layoutManager = new LinearLayoutManager(mContext);
        recyclerView.setLayoutManager(layoutManager);
        loadCategorylList();
    }
    private void loadCategorylList() {
        String[] itemList = {"Dentist", "Gynecologist/Obstertrician", "Dermatologist", "Homeopath", "Ayurveda", "Cardiologist", "Gastroenterologist", "Neurologist", "ENT", "General Physician", "Physiotherapist", "Pediatrician", "Urologist", "Orthopedist", "Opthalmologist", "Dietitian/Nutritionist"};
        int icons[] = {};//{R.drawable.doctor_one, R.mipmap.ic_get_health_tips, R.mipmap.ic_pharma_banner, R.mipmap.records_banner, R.mipmap.ic_ask_boarding, R.mipmap.ic_get_health_tips, R.mipmap.ic_pharma_banner, R.mipmap.records_banner, R.mipmap.ic_ask_boarding, R.mipmap.ic_get_health_tips, R.mipmap.ic_pharma_banner, R.mipmap.records_banner};

        List<Category> categoryList = new ArrayList<>();
        for(String str : itemList) {
            Category c = new Category();
            c.setCategoryName(str);
//            c.setCategoryImage(icons[0]);
            categoryList.add(c);
        }

        CategoryListAdapter adapter = new CategoryListAdapter(mContext, categoryList, this);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void OnCategoryClick(Category category) {
        AppUtills.loadFragment(SubCategoryDetailListFragment.Single.INSTANCE.getInstance(),getActivity(),R.id.container);
    }
}
