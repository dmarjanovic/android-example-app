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

package com.onegini.mobile.exampleapp.model;

import com.onegini.mobile.sdk.android.model.entity.UserProfile;

public class User {

  private final String name;
  private final UserProfile userProfile;

  public User(final UserProfile userProfile, final String name) {
    this.userProfile = userProfile;
    this.name = name;
  }

  public UserProfile getUserProfile() {
    return userProfile;
  }

  public String getName() {
    return name;
  }

  @Override
  public String toString() {
    return name + " (id: " + userProfile.getProfileId() + ")";
  }
}
