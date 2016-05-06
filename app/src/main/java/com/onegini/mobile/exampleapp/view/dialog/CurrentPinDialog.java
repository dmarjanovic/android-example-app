package com.onegini.mobile.exampleapp.view.dialog;

import android.content.Context;
import android.content.Intent;
import com.onegini.mobile.exampleapp.R;
import com.onegini.mobile.exampleapp.view.activity.PinActivity;
import com.onegini.mobile.sdk.android.library.handlers.OneginiPinProvidedHandler;
import com.onegini.mobile.sdk.android.library.utils.dialogs.OneginiCurrentPinDialog;

public class CurrentPinDialog implements OneginiCurrentPinDialog {

  public static OneginiPinProvidedHandler oneginiPinProvidedHandler;

  private Context applicationContext;

  public CurrentPinDialog(final Context context) {
    applicationContext = context.getApplicationContext();
  }

  @Override
  public void getCurrentPin(final OneginiPinProvidedHandler pinProvidedHandler) {
    PinActivity.setIsCreatePinFlow(false);
    oneginiPinProvidedHandler = pinProvidedHandler;
    notifyActivity();
  }

  private void notifyActivity() {
      startPinActivity();
  }

  private void startPinActivity() {
    final Intent intent = new Intent(applicationContext, PinActivity.class);
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.putExtra(PinActivity.EXTRA_TITLE, applicationContext.getString(R.string.pin_title_enter_pin));
    intent.putExtra(PinActivity.EXTRA_MESSAGE, "");
    applicationContext.startActivity(intent);
  }
}
