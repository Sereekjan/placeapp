package kz.ikar.cities.ui.fragment;

import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import kz.ikar.cities.R;
import kz.ikar.cities.ui.viewmodel.LoginViewModel;

public class LoginFragment extends BaseFragment {

    private LoginViewModel mViewModel;

    private Button loginButton;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private ProgressBar progressBar;

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login_fragment, container, false);

        loginButton = view.findViewById(R.id.loginButton);
        usernameEditText = view.findViewById(R.id.usernameEditText);
        passwordEditText = view.findViewById(R.id.passwordEditText);
        progressBar = view.findViewById(R.id.progressBar);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performAuth();
            }
        });

        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_GO || actionId == EditorInfo.IME_ACTION_NEXT) {
                    performAuth();
                }
                return false;
            }
        });

        return view;
    }

    private void performAuth() {
        hideKeyboard();
        showProgress(true);

        mViewModel.login(
            usernameEditText.getText().toString(),
            passwordEditText.getText().toString(),
            new LoginViewModel.OnAuthErrorListener() {
                @Override
                public void onError(String message) {
                    showProgress(false);
                    Snackbar.make(loginButton, message, Snackbar.LENGTH_SHORT).show();
                }
            }
        );
    }

    private void showProgress(boolean show) {
        if (show) {
            loginButton.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.setVisibility(View.GONE);
            loginButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
    }

    @Override
    public void onDestroyView() {
        loginButton = null;
        usernameEditText = null;
        passwordEditText = null;
        progressBar = null;

        super.onDestroyView();
    }

    private void hideKeyboard() {
        if (usernameEditText != null) {
            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(usernameEditText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    public void onBackPressed() {
        mViewModel.exit();
    }
}
