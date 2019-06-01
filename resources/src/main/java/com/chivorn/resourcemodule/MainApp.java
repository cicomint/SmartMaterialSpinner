package com.chivorn.resourcemodule;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.chivorn.smartmaterialspinner.SmartMaterialSpinner;

import java.util.List;

public class MainApp extends AppCompatActivity implements View.OnClickListener {
    private final String repoUrl = "https://github.com/Chivorns/SmartMaterialSpinner";

    protected SmartMaterialSpinner spProvince;
    protected SmartMaterialSpinner spProvinceDialog;
    protected SmartMaterialSpinner spCustomColor;
    protected SmartMaterialSpinner spEmptyItem;
    protected SmartMaterialSpinner spSearchable;
    protected List<String> provinceList;

    private LinearLayout githubRepo;
    private Button btnShowError;
    private Button githubRepoBtn;
    private Intent intent;

    public void initBaseView() {
        spProvince = findViewById(R.id.sp_provinces);
        spProvinceDialog = findViewById(R.id.sp_provinces_dialog);
        spCustomColor = findViewById(R.id.sp_custom_color);
        spEmptyItem = findViewById(R.id.sp_empty_item);
        spSearchable = findViewById(R.id.sp_searchable);
        githubRepo = findViewById(R.id.git_repo_container);
        btnShowError = findViewById(R.id.btn_show_error);
        githubRepoBtn = findViewById(R.id.github_repo_btn);

        setUnderlineColor(spProvince, spProvinceDialog, spCustomColor, spSearchable);
    }

    public void onClickListenter() {
        if (githubRepo != null && githubRepoBtn != null) {
            githubRepo.setOnClickListener(this);
            githubRepoBtn.setOnClickListener(this);
        }

        if (btnShowError != null) {
            btnShowError.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.git_repo_container || v.getId() == R.id.github_repo_btn) {
            intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(repoUrl));
            startActivity(intent);
        } else if (v.getId() == R.id.btn_show_error) {
            spProvince.setError(getResources().getString(R.string.sample_error_message));
            spProvinceDialog.setError(getResources().getString(R.string.sample_error_message));
            spCustomColor.setError(getResources().getString(R.string.sample_error_message));
            spSearchable.setError(getResources().getString(R.string.sample_error_message));
        }
    }

    private void setUnderlineColor(SmartMaterialSpinner... spinners) {
        for (SmartMaterialSpinner spinner : spinners) {
            spinner.setUnderlineColor(ContextCompat.getColor(getApplicationContext(), R.color.underline_color));
        }
    }
}