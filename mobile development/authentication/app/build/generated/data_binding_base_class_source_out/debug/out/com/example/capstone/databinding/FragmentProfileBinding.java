// Generated by view binder compiler. Do not edit!
package com.example.capstone.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.example.capstone.R;
import java.lang.NullPointerException;
import java.lang.Override;
import java.lang.String;

public final class FragmentProfileBinding implements ViewBinding {
  @NonNull
  private final LinearLayout rootView;

  @NonNull
  public final Button btnLogout;

  @NonNull
  public final TextView etEmail;

  @NonNull
  public final TextView etName;

  @NonNull
  public final TextView etPassword;

  @NonNull
  public final ImageView gambar;

  private FragmentProfileBinding(@NonNull LinearLayout rootView, @NonNull Button btnLogout,
      @NonNull TextView etEmail, @NonNull TextView etName, @NonNull TextView etPassword,
      @NonNull ImageView gambar) {
    this.rootView = rootView;
    this.btnLogout = btnLogout;
    this.etEmail = etEmail;
    this.etName = etName;
    this.etPassword = etPassword;
    this.gambar = gambar;
  }

  @Override
  @NonNull
  public LinearLayout getRoot() {
    return rootView;
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater) {
    return inflate(inflater, null, false);
  }

  @NonNull
  public static FragmentProfileBinding inflate(@NonNull LayoutInflater inflater,
      @Nullable ViewGroup parent, boolean attachToParent) {
    View root = inflater.inflate(R.layout.fragment_profile, parent, false);
    if (attachToParent) {
      parent.addView(root);
    }
    return bind(root);
  }

  @NonNull
  public static FragmentProfileBinding bind(@NonNull View rootView) {
    // The body of this method is generated in a way you would not otherwise write.
    // This is done to optimize the compiled bytecode for size and performance.
    int id;
    missingId: {
      id = R.id.btnLogout;
      Button btnLogout = ViewBindings.findChildViewById(rootView, id);
      if (btnLogout == null) {
        break missingId;
      }

      id = R.id.etEmail;
      TextView etEmail = ViewBindings.findChildViewById(rootView, id);
      if (etEmail == null) {
        break missingId;
      }

      id = R.id.etName;
      TextView etName = ViewBindings.findChildViewById(rootView, id);
      if (etName == null) {
        break missingId;
      }

      id = R.id.etPassword;
      TextView etPassword = ViewBindings.findChildViewById(rootView, id);
      if (etPassword == null) {
        break missingId;
      }

      id = R.id.gambar;
      ImageView gambar = ViewBindings.findChildViewById(rootView, id);
      if (gambar == null) {
        break missingId;
      }

      return new FragmentProfileBinding((LinearLayout) rootView, btnLogout, etEmail, etName,
          etPassword, gambar);
    }
    String missingId = rootView.getResources().getResourceName(id);
    throw new NullPointerException("Missing required view with ID: ".concat(missingId));
  }
}