package com.MrSoftIt.class9_10allbook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.downloader.OnCancelListener;
import com.downloader.OnDownloadListener;
import com.downloader.OnPauseListener;
import com.downloader.OnProgressListener;
import com.downloader.OnStartOrResumeListener;
import com.downloader.PRDownloader;
import com.downloader.PRDownloaderConfig;
import com.downloader.Progress;
import com.downloader.utils.Utils;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.link.LinkHandler;
import com.github.barteksc.pdfviewer.listener.OnDrawListener;
import com.github.barteksc.pdfviewer.listener.OnErrorListener;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnLongPressListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.listener.OnPageScrollListener;
import com.github.barteksc.pdfviewer.listener.OnRenderListener;
import com.github.barteksc.pdfviewer.listener.OnTapListener;
import com.github.barteksc.pdfviewer.model.LinkTapEvent;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import com.github.barteksc.pdfviewer.util.FitPolicy;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;


public class PdfActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {


    PDFView pdfView;
    String folderName = "";
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference storageRef = storage.getReference();

    public static String TAG = "dsjhfjhsdkjfsd";

    String PDF;
    String BookName;
    int PageNumber;

    File file;

    ProgressDialog barProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf);
        Toolbar toolbar = findViewById(R.id.toolbar_support);
        barProgressDialog = new ProgressDialog(PdfActivity.this);
        barProgressDialog.setTitle("Downloading ...");
        barProgressDialog.setMessage("For the first time next time read on offline ...");
        barProgressDialog.setProgressStyle(barProgressDialog.STYLE_HORIZONTAL);
        barProgressDialog.setCancelable(false);
        pdfView = findViewById(R.id.pdfView);
        Bundle bundle = getIntent().getExtras();
        // displayFromUri();
        PDF = bundle.getString("PDF");
        PageNumber = bundle.getInt("PageNumber");
        // PageNumber1 = Integer.parseInt(PageNumber1);
        BookName = bundle.getString("BookName");
        toolbar.setTitle("নবম ও দশম শ্রেণির " + BookName);

        try {
            folderName = getDataDir(PdfActivity.this);
            File file = new File(folderName + "/pdf");
            if (!file.exists()) {
                if (!file.exists()) {
                    file.mkdir();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        file = new File(folderName + "/pdf/" + BookName + ".pdf");
        if (file.exists()) {
            displayFromUri();
        } else {
            downloadFile();
        }

    }


    public void downloadFile() {
        // Enabling database for resume support even after the application is killed:
        PRDownloaderConfig config = PRDownloaderConfig.newBuilder()
                .setDatabaseEnabled(true)
                .build();
        PRDownloader.initialize(getApplicationContext(), config);
        PRDownloader.download(PDF, folderName + "/pdf", BookName + ".pdf")
                .build()
                .setOnStartOrResumeListener(new OnStartOrResumeListener() {
                    @Override
                    public void onStartOrResume() {

                    }
                })
                .setOnPauseListener(new OnPauseListener() {
                    @Override
                    public void onPause() {

                    }
                })
                .setOnCancelListener(new OnCancelListener() {
                    @Override
                    public void onCancel() {

                    }
                })
                .setOnProgressListener(new OnProgressListener() {
                    @Override
                    public void onProgress(Progress progress) {
                        long progressPercent = progress.currentBytes * 100 / progress.totalBytes;
                        barProgressDialog.setProgress((int) progressPercent);
                        barProgressDialog.setMax(100);//In this part you can set the  MAX value of data
                        barProgressDialog.show();
                    }
                })
                .start(new OnDownloadListener() {
                    @Override
                    public void onDownloadComplete() {
                        Log.d("fdsgfdfdgdgf", "onDownloadComplete: ");
                        displayFromUri();

                        File file = new File(folderName + "/pdf/" + BookName + ".pdf");

                        if (file.exists()) {
                            Log.d("fdsgfdfdgdgf", "onCreate: " + file.getAbsolutePath());
                            barProgressDialog.dismiss();
                            displayFromUri();

                        } else {
                            Log.d("fdsf", "onCreate: ");
                        }

                    }

                    @Override
                    public void onError(com.downloader.Error error) {
                        Toast.makeText(getApplicationContext(), error.getServerErrorMessage() + "", Toast.LENGTH_SHORT).show();


                    }

                });
    }

    public static String getDataDir(Context context) throws Exception {
        return context.getPackageManager()
                .getPackageInfo(context.getPackageName(), 0)
                .applicationInfo.dataDir;
    }

    private void displayFromUri() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        // File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/Class9-10/pdf/"+BookName+".pdf");

        File file = new File(folderName + "/pdf/" + BookName + ".pdf");

        if (file.exists() && pdfView != null) {
            pdfView.fromFile(file)
                    .defaultPage(PageNumber)
                    .enableAnnotationRendering(true)
                    .enableSwipe(true)
                    // allows to draw something on the current page, usually visible in the middle of the screen
                    .onDraw(new OnDrawListener() {
                        @Override
                        public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
                            Log.d(TAG, "onLayerDrawn 11: "+canvas.toString()+  " "+ pageHeight + "  "+displayedPage);
                        }
                    })
                    // allows to draw something on all pages, separately for every page. Called only for visible pages
                    .onDrawAll(new OnDrawListener() {
                        @Override
                        public void onLayerDrawn(Canvas canvas, float pageWidth, float pageHeight, int displayedPage) {
                            Log.d(TAG, "onLayerDrawn: "+canvas.toString()+  " "+ pageHeight + "  "+displayedPage);
                        }
                    })
                    .onLoad(new OnLoadCompleteListener() {
                        @Override
                        public void loadComplete(int nbPages) {

                            Log.d(TAG, "loadComplete: "+nbPages);
                        }
                    }) // called after document is loaded and starts to be rendered
                    .onPageChange(new OnPageChangeListener() {
                        @Override
                        public void onPageChanged(int page, int pageCount) {
                            Log.d(TAG, "onPageChanged: "+page +"  "+ pageCount);
                        }
                    })
                    .onPageScroll(new OnPageScrollListener() {
                        @Override
                        public void onPageScrolled(int page, float positionOffset) {

                            Log.d(TAG, "onPageScrolled: "+page+"  "+positionOffset);
                        }
                    })
                    .onError(new OnErrorListener() {
                        @Override
                        public void onError(Throwable t) {

                            Log.d(TAG, "onError: "+t.toString());
                        }
                    })
                    .onPageError(new OnPageErrorListener() {
                        @Override
                        public void onPageError(int page, Throwable t) {
                            Log.d(TAG, "onPageError: "+t.getMessage());
                        }
                    })
                    .onRender(new OnRenderListener() {
                        @Override
                        public void onInitiallyRendered(int nbPages) {
                            Log.d(TAG, "onInitiallyRendered: "+nbPages);
                        }
                    }) // called after document is rendered for the first time
                    // called on single tap, return true if handled, false to toggle scroll handle visibility
                    .onTap(new OnTapListener() {
                        @Override
                        public boolean onTap(MotionEvent e) {
                            return false;
                        }
                    })
                    .onLongPress(new OnLongPressListener() {
                        @Override
                        public void onLongPress(MotionEvent e) {
                            Log.d(TAG, "onLongPress: " +e.getAction()+ e.getActionIndex());
                        }
                    })
                    .enableAnnotationRendering(false) // render annotations (such as comments, colors or forms)
                    .password(null)
                    .scrollHandle(null)
                    .enableAntialiasing(true) // improve rendering a little bit on low-res screens
                    // spacing between pages in dp. To define spacing color, set view background
                    .spacing(0)
                    .autoSpacing(false) // add dynamic spacing to fit each page on its own on the screen
                    .linkHandler(new LinkHandler() {
                        @Override
                        public void handleLinkEvent(LinkTapEvent event) {

                        }
                    })
                    .pageFitPolicy(FitPolicy.WIDTH) // mode to fit pages in the view
                    .fitEachPage(false) // fit each page to the view, else smaller pages are scaled relative to largest page.
                    .pageSnap(false) // snap pages to screen boundaries
                    .pageFling(false) // make a fling change only a single page like ViewPager
                    .nightMode(false)
                    .load();
        } else {

            Log.d("fdsfsdfsd", "displayFromUri: " + file.getAbsolutePath());
            storage.getReferenceFromUrl(PDF)
                    .getBytes(900000000)
                    .addOnSuccessListener(new OnSuccessListener<byte[]>() {
                        @Override
                        public void onSuccess(byte[] bytes) {

                            pdfView.fromBytes(bytes)
                                    .defaultPage(PageNumber)
                                    .enableSwipe(true) // allows to block changing pages using swipe
                                    .swipeHorizontal(false)
                                    .enableDoubletap(true)
                                    .load();

                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(PdfActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                        }
                    });


        }
    }

    @AfterPermissionGranted(123)
    private void openStoreg() {
        String[] perms = {android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(this, perms)) {
            if (MainActivity.InternetConnection.checkConnection(PdfActivity.this)) {
                // Its Available...

            } else {
                // Not Available...
                Toast.makeText(this, " No Internet ", Toast.LENGTH_LONG).show();

            }


        } else {
            EasyPermissions.requestPermissions(this, "We need storage permissions ",
                    123, perms);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {

        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {

        }
    }


}