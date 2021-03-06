/*
 * Copyright (c) 2016-2017 Onegini B.V.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.onegini.mobile.exampleapp.network.gcm;

import java.io.IOException;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.onegini.mobile.exampleapp.BuildConfig;
import com.onegini.mobile.exampleapp.Constants;
import com.onegini.mobile.exampleapp.OneginiSDK;
import com.onegini.mobile.exampleapp.storage.GCMStorage;
import com.onegini.mobile.sdk.android.client.UserClient;
import com.onegini.mobile.sdk.android.handlers.OneginiMobileAuthenticationEnrollmentHandler;
import com.onegini.mobile.sdk.android.handlers.error.OneginiMobileAuthenticationEnrollmentError;

public class GCMRegistrationService {

  private static final String TAG = "GCMRegistrationService";

  private final Context context;
  private final GCMStorage storage;

  private OneginiMobileAuthenticationEnrollmentHandler enrollmentHandler;

  public GCMRegistrationService(final Context context) {
    this.context = context;
    storage = new GCMStorage(context);
  }

  public void registerGCMService(final OneginiMobileAuthenticationEnrollmentHandler handler) {
    enrollmentHandler = handler;
    final String regid = getRegistrationId();
    if (regid.isEmpty()) {
      registerInBackground();
    } else {
      enrollForMobileAuthentication(regid);
    }
  }

  /**
   * Gets the current registration ID for application on GCM service. If result is empty, the app needs to register.
   *
   * @return registration ID, or empty string if there is no existing registration ID.
   */
  private String getRegistrationId() {
    final String registrationId = storage.getRegistrationId();
    if (registrationId == null || registrationId.isEmpty()) {
      Log.i(TAG, "Registration not found.");
      return "";
    }

    // Check if app was updated; if so, it must clear the registration ID since the existing regID is not guaranteed to work with the new app version.
    final int registeredVersion = storage.getAppVersion();
    final int currentVersion = BuildConfig.VERSION_CODE;
    if (registeredVersion != currentVersion) {
      Log.i(TAG, "App version changed.");
      return "";
    }
    return registrationId;
  }

  /**
   * Registers the application with GCM servers asynchronously. Stores the registration ID and app versionCode in the application's shared preferences.
   */
  private void registerInBackground() {
    new AsyncTask<Void, Void, Void>() {
      @Override
      protected Void doInBackground(Void... params) {
        try {
          InstanceID instanceID = InstanceID.getInstance(context);
          String regid = instanceID.getToken(Constants.GCM_SENDER_ID,
              GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
          enrollForMobileAuthentication(regid);
          storeRegisteredId(regid);
        } catch (final IOException ex) {
          enrollmentHandler
              .onError(new OneginiMobileAuthenticationEnrollmentError(OneginiMobileAuthenticationEnrollmentError.GENERAL_ERROR, "Unable to register in GCM"));
        }
        return null;
      }
    }.execute(null, null, null);
  }

  private void storeRegisteredId(final String regid) {
    storage.setRegistrationId(regid);
    storage.setAppVersion(BuildConfig.VERSION_CODE);
    storage.save();
  }

  private void enrollForMobileAuthentication(final String regId) {
    final UserClient userClient = OneginiSDK.getOneginiClient(context).getUserClient();
    userClient.enrollUserForMobileAuthentication(regId, enrollmentHandler);
  }
}
