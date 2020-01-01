package com.journaldev.mvpdagger2.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.Button;
import android.widget.TextView;
import com.journaldev.mvpdagger2.R;
import com.journaldev.mvpdagger2.view.fragment.ImagesFragment.gridImages.GridAlbumImagesFragment;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class ViewImagesGridActivity extends BaseThemeActivity {

    @BindView(R.id.back)
    Button back;
    @BindView(R.id.title)
    TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_images_grid);
        ButterKnife.bind(this);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment baseGridImages = new GridAlbumImagesFragment();
        Bundle imagesBundle = getIntent().getExtras();
        baseGridImages.setArguments(imagesBundle);
        fragmentTransaction.add(R.id.imagesFragment, baseGridImages);
        fragmentTransaction.commit();

        setTitle(imagesBundle.getString("albumName"));
    }

    private void setTitle(String albumName) {
        title.setText(albumName);
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    @Override
    int getDarkTheme() {
        return R.style.DarkTheme2;
    }

    @Override
    int getLightTheme() {
        return R.style.LightTheme2;
    }
}
