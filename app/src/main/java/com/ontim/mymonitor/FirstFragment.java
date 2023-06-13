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

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.camera.core.Camera;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.video.QualitySelector;
import androidx.camera.video.Recorder;
import androidx.camera.video.Recording;
import androidx.camera.video.VideoCapture;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.google.common.util.concurrent.ListenableFuture;
import com.ontim.mymonitor.databinding.FragmentFirstBinding;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;

public class FirstFragment extends Fragment {

    private static final String TAG = "FirstFragment";
    // default use rear camera.
    private static CameraSelector mCurCameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA;
    VideoCapture<Recorder> mVideoCapture;
    Recording recording;
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

                startCamera();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        // startCamera();
        captureVideo();
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
                    preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());
                    CameraSelector cameraSelector = mCurCameraSelector;
                    cameraProvider.unbindAll();
                    // Bind use cases to camera
                    cameraProvider.bindToLifecycle(getViewLifecycleOwner(), cameraSelector, preview);
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch ( Exception e) {
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
            ProcessCameraProvider cameraProvider = (ProcessCameraProvider)mCameraProviderFuture.get();
            Preview preview = new Preview.Builder().build();
            preview.setSurfaceProvider(binding.viewFinder.getSurfaceProvider());
            CameraSelector cameraSelector = mCurCameraSelector;

            //val qualitySelector = QualitySelector.from(quality)

            Recorder recorder = new Recorder.Builder().build();
            mVideoCapture = VideoCapture.withOutput(recorder);
            Camera camera = cameraProvider.bindToLifecycle(getViewLifecycleOwner(), cameraSelector, preview);


        } catch ( Exception e) {
            e.printStackTrace();
        }

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