package com.thunder.lifecare.customlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.thunder.lifecare.R;
import com.yalantis.contextmenu.lib.ContextMenuDialogFragment;
import com.yalantis.contextmenu.lib.MenuObject;
import com.yalantis.contextmenu.lib.MenuParams;
import com.yalantis.contextmenu.lib.interfaces.OnMenuItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ist on 28/10/16.
 */

public class ContextMenu {

    Context mContext;
    TypedArray menu_icons;
    String[] menuOptions;
    private ContextMenuDialogFragment mMenuDialogFragment;
    private OnMenuItemClickListener listener;

    public ContextMenu(Context mContext, OnMenuItemClickListener listener) {

        this.listener = listener;
        this.mContext = mContext;
        menuOptions =  mContext.getResources().getStringArray(R.array.option_menu);
        menu_icons = mContext.getResources().obtainTypedArray(R.array.menu_icon);
    }

    public ContextMenuDialogFragment initMenuFragment() {
        MenuParams menuParams = new MenuParams();
        menuParams.setActionBarSize((int) mContext.getResources().getDimension(R.dimen.tool_bar_height));
//        menuParams.setActionBarSize(50);
        menuParams.setMenuObjects(getMenuObjects());
        menuParams.setClosableOutside(false);
        mMenuDialogFragment = ContextMenuDialogFragment.newInstance(menuParams);
        mMenuDialogFragment.setItemClickListener(listener);

        return mMenuDialogFragment;
//        mMenuDialogFragment.setItemLongClickListener(listener);
    }

    private List<MenuObject> getMenuObjects() {
        // You can use any [resource, bitmap, drawable, color] as image:
        // item.setResource(...)
        // item.setBitmap(...)
        // item.setDrawable(...)
        // item.setColor(...)
        // You can set image ScaleType:
        // item.setScaleType(ScaleType.FIT_XY)
        // You can use any [resource, drawable, color] as background:
        // item.setBgResource(...)
        // item.setBgDrawable(...)
        // item.setBgColor(...)
        // You can use any [color] as text color:
        // item.setTextColor(...)
        // You can set any [color] as divider color:
        // item.setDividerColor(...)

        List<MenuObject> menuObjects = new ArrayList<>();

        for(int i=0;i<menuOptions.length;i++){

            MenuObject menuObject = new MenuObject(""+menuOptions[i]);
            menuObject.setResource(menu_icons.getResourceId(i, -1));
            menuObject.setTextColor(R.color.colorPrimary);
            menuObject.setScaleType(ImageView.ScaleType.FIT_XY);
            menuObjects.add(menuObject);
        }

//        MenuObject close = new MenuObject("Close");
//        close.setResource(R.drawable.ic_close_dark);
//
//
//        MenuObject overall = new MenuObject("Over-All");
//        overall.setResource(R.drawable.ic_add_value);
//        overall.setTextColor(R.color.colorPrimary);
//        overall.setScaleType(ImageView.ScaleType.FIT_XY);
//
//        MenuObject oneOnOne = new MenuObject("One-on-One");
//        oneOnOne.setResource(R.drawable.ic_add_value);
//        oneOnOne.setTextColor(R.color.colorPrimary);
//        oneOnOne.setScaleType(ImageView.ScaleType.FIT_XY);
//
//        MenuObject myPost = new MenuObject("My-Post");
//        myPost.setResource(R.drawable.ic_add_value);
//        myPost.setTextColor(R.color.colorPrimary);
//        myPost.setScaleType(ImageView.ScaleType.FIT_XY);
//
//
//        menuObjects.add(close);
//        menuObjects.add(overall);
//        menuObjects.add(oneOnOne);
//        menuObjects.add(myPost);
        return menuObjects;
    }

    public void setDialogPosition(ContextMenuDialogFragment mMenuDialogFragment) {
        Window window = mMenuDialogFragment.getDialog().getWindow();
        window.setGravity(Gravity.BOTTOM | Gravity.LEFT);

        WindowManager.LayoutParams params = window.getAttributes();
        params.y = dpToPx(60);
        window.setAttributes(params);
    }

    private int dpToPx(int dp) {
        DisplayMetrics metrics = mContext.getResources().getDisplayMetrics();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, metrics);
    }

    public void reposition(){

//        Display display = mContext.getWindowManager().getDefaultDisplay();
//        Point size = new Point();
//        display.getSize(size);
//        int widthOriginal = size.x;
//        int heightOriginal = size.y;
//
//        WindowManager.LayoutParams wmlp = dialog.getWindow().getAttributes();
//        wmlp.gravity = Gravity.TOP | Gravity.LEFT;
//        if (location[1] >= height) {
//            if(widthOriginal == 600 && heightOriginal == 976 ){
//                wmlp.x = location[0] - 110;
//                wmlp.y = location[1] - 125;
//            } else {
//                wmlp.x = location[0] - 150;
//                wmlp.y = location[1] - 170;
//            }
//        } else {
//            if(widthOriginal == 600 && heightOriginal == 976 ){
//                wmlp.x = location[0] - 110; // x position
//                wmlp.y = location[1]; // y position
//            } else {
//                wmlp.x = location[0] - 150; // x position
//                wmlp.y = location[1]; // y position
//            }
//        }

    }
}

