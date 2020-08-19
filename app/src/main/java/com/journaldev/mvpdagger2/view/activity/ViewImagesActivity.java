package com.journaldev.mvpdagger2.view.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import com.journaldev.mvpdagger2.application.App;
import com.journaldev.mvpdagger2.data.Image.ImageRepository;
import com.journaldev.mvpdagger2.model.ImageModel;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.view.adapter.ImagesPageAdapter;
import com.journaldev.mvpdagger2.view.customView.ImageViewTouchViewPager;
import com.journaldev.mvpdagger2.utils.ImageHelper;
import java.io.IOException;
import java.util.ArrayList;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewImagesActivity extends BaseThemeActivity implements ImageHelper.alertDialogListener, ImagesPageAdapter.PagerClickListener {

    @BindView(R.id.pager)
    ImageViewTouchViewPager pager;
    @BindView(R.id.deleteImage)
    Button deleteImage;
    @BindView(R.id.likeImage)
    Button likeImage;
    @BindView(R.id.shareButtonImage)
    Button shareButtonImage;
    @BindView(R.id.windowViewImages)
    ConstraintLayout windowViewImages;

    ArrayList<ImageModel> imageModels;
    ImagesPageAdapter imagesPageAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.getApp().initRepositories();
        allScreen();
        super.onCreate(savedInstanceState);
        prepareTheLook();
        postponeEnterTransition();
        setContentView(R.layout.viewimages);
        ButterKnife.bind(this);
        imageModels = getAllDataImage();
        getOtherIntent();
        initViewPager();
        processingChangeCurrentItem();
        setLikeState(getImageId());
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void prepareTheLook() {
        setTitle("");
        transparentActionBar();
    }

    private void initViewPager() {
        pager.setOffscreenPageLimit(3);
        int current = getImageId();
        imagesPageAdapter = new ImagesPageAdapter(this, imageModels, this, current);
        pager.setAdapter(imagesPageAdapter);
        pager.setCurrentItem(current, App.getAppPreference().getIsAnim());
    }

    private void transparentActionBar() {
        ColorDrawable abDrawable = new ColorDrawable(getResources().getColor(R.color.colorPrimaryDark));
        abDrawable.setAlpha(0);
        getSupportActionBar().setBackgroundDrawable(abDrawable);
    }


    private void processingChangeCurrentItem() {
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            public void onPageScrollStateChanged(int state) {
            }

            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            public void onPageSelected(int position) {
                setLikeState(position);
            }
        });
    }


    private ArrayList<ImageModel> getAllDataImage() {
        ArrayList<ImageModel> uri = getIntent().getParcelableArrayListExtra("uri");

        if (uri == null)
            uri = App.getImageRepository().getUrls();

        return uri;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.file_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.info:
                return viewFileInfoActivity();
            case R.id.wallpaper:
                Uri imageUri = imageModels.get(pager.getCurrentItem()).getImage();
                ImageHelper.setWallpaper(ImageHelper.convertUriToBitmap(imageUri));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private Boolean viewFileInfoActivity() {
        hideNavigationBar();
        Intent intent = new Intent(this, FIleInformActivity.class);
        Uri currentUri = imagesPageAdapter.getCurrentUri(pager.getCurrentItem()).getImage();
        intent.putExtra("uri", currentUri.toString());
        startActivity(intent);
        if (App.getAppPreference().getIsAnim()) {
            overridePendingTransition(R.anim.back, R.anim.next);
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void hideNavigationBar() {
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);
    }

    private void getOtherIntent() {
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();
        if (Intent.ACTION_VIEW.equals(action) && type != null) {
            if (type.startsWith("image/")) {
                selectCurrentItemFromIntent(intent);
            }
        }
    }

    private void selectCurrentItemFromIntent(Intent intent) {
        String intentStr = intent.toString();

        final String intentFileName = getFileNameFromPath(intentStr);
        final String[] allFileName = getAllPath();

        int currentItem = getCurrentItem(intentFileName, allFileName);
        selectImageFromId(currentItem);
    }

    private void selectImageFromId(final int id) {
        pager.post(() -> pager.setCurrentItem(id, false));
    }

    private String[] getAllPath() {
        String[] allFileName = new String[imageModels.size()];

        for (int i = 0; i < imageModels.size(); i++) {
            allFileName[i] = getFileNameFromPath(imageModels.get(i).getImage().toString());
        }

        return allFileName;
    }

    private int getCurrentItem(String intentFileName, String[] allFileName) {
        for (int i = 0; i < allFileName.length; i++) {
            if (allFileName[i].equals(intentFileName)) {
                return i;
            }
        }
        return -1;
    }

    private String getFileNameFromPath(String str) {
        String[] fileStr = getPath(str).split("/");
        String fileName = fileStr[fileStr.length - 1];
        return fileName;
    }

    private String getPath(String str) {
        int start = getIdStartPath(str);
        int end = getIdEndPath(str);

        if (start != -1 && end != -1)
            return str.substring(start, end);
        else
            return str;
    }

    private int getIdStartPath(String str) {
        String startStr = " dat=";
        int start = str.indexOf(startStr) + startStr.length();
        return start;
    }

    private int getIdEndPath(String str) {
        String endStr = " typ=";
        int end = str.indexOf(endStr);
        return end;
    }


    private void allScreen() {
        Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
    }


    private int getImageId() {
        return getIntent().getIntExtra("idImage", 0);
    }


    @SuppressLint("ResourceAsColor")
    @OnClick(R.id.deleteImage)
    public void clickDeleteImage() {
        ImageHelper.createDeleteImageAlertDialog(this,
                this.getString(R.string.delete_image_confirmation), this);
    }

    private void deleteImage() {
        ImageHelper.deleteImage(this,imageModels.get(pager.getCurrentItem()).getImage());
        int currentPosition = pager.getCurrentItem();
        imageModels.remove(currentPosition);
        viewPagerUpdate(currentPosition);
    }

    private void viewPagerUpdate(int currentPosition) {
        pager.setAdapter(imagesPageAdapter);
        pager.setCurrentItem(currentPosition,false);
    }

    @OnClick(R.id.likeImage)
    public void clickLikeImage(View view) {
        changeLikeState(view);
        Uri fileUri = imagesPageAdapter.getCurrentUri(pager.getCurrentItem()).getImage();
        Boolean like = view.isSelected();
        ExifInterface exif;
        try {
            exif = new ExifInterface(fileUri.toString());
            exif.setAttribute(ExifInterface.TAG_USER_COMMENT, like.toString());
            exif.saveAttributes();
            imageModels.get(pager.getCurrentItem()).setLike(like);
           App.getImageRepository().getImageObserver().onChange(false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setLikeState(int imageId) {
        likeImage.setSelected(imageModels.get(imageId).getLike());
    }

    private void changeLikeState(View v) {
        v.setSelected(!v.isSelected());
    }

    @Override
    public void deleteClick() {
        deleteImage();
    }

    @Override
    public void setStartPostTransition(final View view) {
        view.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                view.getViewTreeObserver().removeOnPreDrawListener(this);
                startPostponedEnterTransition();
                return false;
            }
        });
    }

    @OnClick(R.id.shareButtonImage)
    public void shareButtonImageClick() {
        ArrayList<Uri> urls = new ArrayList<>();
        Uri localUri = imageModels.get(pager.getCurrentItem()).getImage();
        urls.add(ImageHelper.getGlobalPath(localUri.toString()));
        ImageHelper.shareImages(urls,this);
    }

    @Override
    int getDarkTheme() {
        return R.style.DarkTheme;
    }

    @Override
    int getLightTheme() {
        return R.style.LightTheme;
    }
}