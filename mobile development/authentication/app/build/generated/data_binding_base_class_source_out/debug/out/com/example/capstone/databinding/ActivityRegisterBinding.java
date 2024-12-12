// Generated by view binder compiler. Do not edit!
package com.example.capstone.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.capstone.R;
import com.example.capstone.ValidatingEditText;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class ActivityRegisterBinding implements ViewBinding {
  @NonNull
  private final RelativeLayout rootView;

  @NonNull
  public final Button btnRegister;

  @NonNull
  public final CardView cardView;

  @NonNull
  public final ValidatingEditText edRegisterEmail;

  @NonNull
  public final EditText edRegisterName;

  @NonNull
  public final ValidatingEditText edRegisterPassword;

  @NonNull
  public final TextView hiWelcome;

  @NonNull
  public final ImageView passwordVisibilityToggle;

  @NonNull
  public final TextView registerTitle;

  @NonNull
  public final TextView tvLogin;

  private ActivityRegisterBinding(@NonNull RelativeLayout rootView, @NonNull Button btnRegister,
      @NonNull CardView cardView, @NonNull ValidatingEditText edRegisterEmail,
      @NonNull EditText edRegisterName, @NonNull ValidatingEditText edRegisterPassword,
      @NonNull TextView hiWelcome, @NonNull ImageView passwordVisibilityToggle,
      @NonNull TextView registerTitle, @NonNull TextView tvLogin) {
    this.rootView = rootView;
    this.btnRegister = btnRegister;
    this.cardView = cardView;
    this.edRegisterEmail = edRegisterEmail;
    this.edRegisterName = edRegisterName;
    this.edRegisterPassword = edRegisterPassword;
    this.hiWelcome = hiWelcome;
    this.passwordVisibilityToggle = passwordVisibilityToggle;
    this.registerTitle = registerTitle;
    this.tvLogin = tvLogin;
  }

  @Override
  @NonNull
  public RelativeLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static ActivityRegisterBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.activity_register, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static ActivityRegisterBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btn_register;
      Button btnRegister = ViewBindings.findChildViewById(rootView, id);
      if (btnRegister == null) {
        break missingId;
      }

      id = R.id.cardView;
      CardView cardView = ViewBindings.findChildViewById(rootView, id);
      if (cardView == null) {
        break missingId;
      }

      id = R.id.ed_register_email;
      ValidatingEditText edRegisterEmail = ViewBindings.findChildViewById(rootView, id);
      if (edRegisterEmail == null) {
        break missingId;
      }

      id = R.id.ed_register_name;
      EditText edRegisterName = ViewBindings.findChildViewById(rootView, id);
      if (edRegisterName == null) {
        break missingId;
      }

      id = R.id.ed_register_password;
      ValidatingEditText edRegisterPassword = ViewBindings.findChildViewById(rootView, id);
      if (edRegisterPassword == null) {
        break missingId;
      }

      id = R.id.hi_welcome_;
      TextView hiWelcome = ViewBindings.findChildViewById(rootView, id);
      if (hiWelcome == null) {
        break missingId;
      }

      id = R.id.passwordVisibilityToggle;
      ImageView passwordVisibilityToggle = ViewBindings.findChildViewById(rootView, id);
      if (passwordVisibilityToggle == null) {
        break missingId;
      }

      id = R.id.registerTitle;
      TextView registerTitle = ViewBindings.findChildViewById(rootView, id);
      if (registerTitle == null) {
        break missingId;
      }

      id = R.id.tvLogin;
      TextView tvLogin = ViewBindings.findChildViewById(rootView, id);
      if (tvLogin == null) {
        break missingId;
      }

      return new ActivityRegisterBinding((RelativeLayout) rootView, btnRegister, cardView,
          edRegisterEmail, edRegisterName, edRegisterPassword, hiWelcome, passwordVisibilityToggle,
          registerTitle, tvLogin);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}
