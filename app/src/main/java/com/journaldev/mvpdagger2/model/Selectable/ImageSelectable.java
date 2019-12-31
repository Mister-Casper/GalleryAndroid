package com.journaldev.mvpdagger2.model.Selectable;

import android.net.Uri;

public interface ImageSelectable extends Selectable {

    Uri getImage();
    Boolean getLike();
}
