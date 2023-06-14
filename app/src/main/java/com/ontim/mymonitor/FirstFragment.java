/*
 * Copyright (C) 2023 The ONTIM Technologies Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.ontim.mymonitor;

import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.MediaStoreOutputOptions;
import androidx.camera.video.Quality;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.RecordingStats;
import androidx.camera.video.VideoCapture;
import androidx.camera.video.VideoRecordEvent;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.util.Consumer;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.common.util.concurrent.ListenableFuture;
import com.ontim.mymonitor.Util.Utils;
import com.ontim.mymonitor.databinding.FragmentFirstBinding;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

public class FirstFragment extends Fragment {

    private static final String TAG = "FirstFragment";
    private static final long MAX_MIN = 1;
    // default use rear camera.
    private static CameraSelector mCurCameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
    VideoCapture<Recorder> mVideoCapture;
    Recording mRecording;
    TextView mTextView;
    NavController mNavController;
    ListenableFuture<ProcessCameraProvider> mCameraProviderFuture;
    private FragmentFirstBinding binding;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        binding = FragmentFirstBinding.inflate(inflater, container, false);
        mTextView = binding.cameraIndexText;
        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mNavController = NavHostFragment.findNavController(FirstFragment.this);

        binding.buttonFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                NavHostFragment.findNavController(FirstFragment.this)
//                        .navigate(R.id.action_FirstFragment_to_SecondFragment);
                if (mCurCameraSelector == CameraSelector.DEFAULT_FRONT_CAMERA) {
                    mCurCameraSelector = CameraSelector.DEFAULT_BACK_CAMERA;
                    mTextView.setText(R.string.rear_carmer);

                } else {
                    mCurCameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
                    mTextView.setText(R.string.front_carmer);
                }

                captureVideo();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // startCamera();
        captureVideo();
    }

    private void startCameraX(ProcessCameraProvider cameraProvider) {

        cameraProvider.unbindAll();

        CameraSelector cameraSelector = new CameraSelector.Builder()
                .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                .build();

        Preview preview = new Preview.Builder().build();

        preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());

        Recorder recorder = new Recorder.Builder()
                // .setQualitySelector(qualitySelector)
                .build();
        mVideoCapture = VideoCapture.withOutput(recorder);

        cameraProvider.bindToLifecycle(this, cameraSelector, preview, mVideoCapture);
    }

    private void startCamera() {

        if (mCameraProviderFuture == null) {
            mCameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        }

        mCameraProviderFuture.addListener(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "runnable...");
                try {
                    ProcessCameraProvider cameraProvider = (ProcessCameraProvider) mCameraProviderFuture.get();
                    Preview preview = new Preview.Builder().build();
                    CameraSelector cameraSelector = mCurCameraSelector;
                    cameraProvider.unbindAll();
                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(getViewLifecycleOwner(), cameraSelector, preview);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, getExecutor());
    }

    private void captureVideo() {
        Log.d(TAG, "captureVideo");

        if (mCameraProviderFuture == null) {
            mCameraProviderFuture = ProcessCameraProvider.getInstance(getContext());
        }
        try {
            ProcessCameraProvider cameraProvider = (ProcessCameraProvider) mCameraProviderFuture.get();
            Preview preview = new Preview.Builder().build();
            preview.setSurfaceProvider(binding.previewView.getSurfaceProvider());
            CameraSelector cameraSelector = mCurCameraSelector;

            //val qualitySelector = QualitySelector.from(quality)

            Recorder recorder = new Recorder.Builder()
                    .setQualitySelector(QualitySelector.from(Quality.SD))
                    .build();

            mVideoCapture = VideoCapture.withOutput(recorder);

            cameraProvider.unbindAll();
            cameraProvider.bindToLifecycle(getViewLifecycleOwner(), cameraSelector, mVideoCapture,
                    preview);

            recordVideo();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void recordVideo() {
        if (mVideoCapture == null) return;

        String name = Utils.getFileName("video") + ".mp4";

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, name);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "video/mp4");

        MediaStoreOutputOptions mediaStoreOutput = new MediaStoreOutputOptions.Builder(
                getContext().getContentResolver(),
                MediaStore.Video.Media.EXTERNAL_CONTENT_URI)
                .setContentValues(contentValues)
                .build();

        // configure Recorder and Start recording to the mediaStoreOutput.
        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.RECORD_AUDIO)
                != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            mRecording = mVideoCapture.getOutput()
                    .prepareRecording(requireActivity(), mediaStoreOutput)
                    .start(getExecutor(), captureListener);
            Log.i(TAG, "Recording started， without audio.");

            return;
        }
        mRecording = mVideoCapture.getOutput()
                .prepareRecording(requireActivity(), mediaStoreOutput)
                .withAudioEnabled()
                .start(getExecutor(), captureListener);

        Log.i(TAG, "Recording started");
    }


    /**
     * CaptureEvent listener.
     */
    private Consumer<VideoRecordEvent> captureListener = new Consumer<VideoRecordEvent>(){

        @Override
        public void accept(VideoRecordEvent videoRecordEvent) {

            // Log.d(TAG, "Duration: " + duration + ", RecordingStats:" + recordingStats);
            updateUi(videoRecordEvent);
        }
    };




    private void updateUi(VideoRecordEvent videoRecordEvent) {
        RecordingStats recordingStats = videoRecordEvent.getRecordingStats();

        long duration = recordingStats.getRecordedDurationNanos();

        long size = (recordingStats.getNumBytesRecorded() / 1024);
        long time = TimeUnit.NANOSECONDS.toSeconds(duration);
        String text = recordingStats + ": recorded" + size + ", in" + time+ "second";
        if (videoRecordEvent instanceof VideoRecordEvent.Finalize)
            text = text + "\nFile saved to: " + ((VideoRecordEvent.Finalize) videoRecordEvent)
                    .getOutputResults().getOutputUri();

        long timeMin = time/60;
        if (timeMin > MAX_MIN) {
            mRecording.stop();
        }

        binding.recordInfo.setText(text);
    }


    private Executor getExecutor() {
        return ContextCompat.getMainExecutor(getContext());
    }


    private void initializeQualitySectionsUI() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

}