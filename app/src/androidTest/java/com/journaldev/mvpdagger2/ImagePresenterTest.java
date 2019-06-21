package com.journaldev.mvpdagger2;

import android.net.Uri;

import com.journaldev.mvpdagger2.activity.ViewImages.ViewImagesContract;
import com.journaldev.mvpdagger2.activity.ViewImages.model.ViewImagesModel;
import com.journaldev.mvpdagger2.activity.ViewImages.presenter.ViewImagesPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class ImagePresenterTest {
    @Mock
    private ViewImagesContract.ViewCallBack imageView;

    @Mock
    private ViewImagesModel imageModelContract;

    private ViewImagesPresenter imagePresenter;

    @Before
    public void setUp() {
        imagePresenter = new ViewImagesPresenter(imageView, imageModelContract);
    }

    @Test
    public void checkСhangeCurrentImage() {
        imagePresenter.chandgeCurrentImage(0);
        Mockito.verify(imageView).viewImage(imageModelContract.getImage(0));
    }

    @Test
    public void checkCorrectnessChandgeCurrentImage() throws Exception {
        Uri testUri = Uri.parse("TestUri");
        Mockito.when(imageModelContract.getImage(ArgumentMatchers.anyInt())).thenReturn(testUri);
        imagePresenter.chandgeCurrentImage(0);
        ArgumentCaptor<Uri> secondFooCaptor = ArgumentCaptor.forClass(Uri.class);
        Mockito.verify(imageView).viewImage(secondFooCaptor.capture());
        assertEquals(testUri, secondFooCaptor.getValue());
    }

    @Test
    public void checkOutOfRangeMax() {
        Mockito.when(imageModelContract.getMaxId()).thenReturn(200);
        imageModelContract.currentImageId = 200;
        imagePresenter.chandgeCurrentImage(1);
        assertEquals(imageModelContract.currentImageId, 200);
    }
    @Test
    public void checkOutOfRangeMin() {
        Mockito.when(imageModelContract.getMaxId()).thenReturn(0);
        imageModelContract.currentImageId = 0;
        imagePresenter.chandgeCurrentImage(-1);
        assertEquals(imageModelContract.currentImageId, 0);
    }
}
