package com.chivorn.demojava;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.chivorn.resourcemodule.MainApp;

import java.util.ArrayList;

public class MainActivity extends MainApp {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smart_material_spinner_sample_layout);

        initBaseView();
        initSpinnerInJava();
        onClickListenter();
    }

    private void initSpinnerInJava() {
        provinceList = new ArrayList<>();
        provinceList.add("Banteay Meanchey");
        provinceList.add("Battambang");
        provinceList.add("Kampong Cham");
        provinceList.add("Kampong Chhnang");
        provinceList.add("Kampong Speu");
        provinceList.add("Kampong Thom");
        provinceList.add("Kampot");
        provinceList.add("Kandal");
        provinceList.add("Kep");
        provinceList.add("Koh Kong");
        provinceList.add("Kratie");
        provinceList.add("Mondulkiri");
        provinceList.add("Oddar Meanchey");
        provinceList.add("Pailin");
        provinceList.add("Phnom Penh");
        provinceList.add("Preah Vihear");
        provinceList.add("Prey Veng");
        provinceList.add("Pursat");
        provinceList.add("Ratanakiri");
        provinceList.add("Siem Reap");
        provinceList.add("Sihanoukville");
        provinceList.add("Stung Treng");
        provinceList.add("Svay Rieng");
        provinceList.add("Takeo");
        provinceList.add("Tbong Khmum");

        spProvince.setItems(provinceList);
        spProvinceDialog.setItems(provinceList);
        spCustomColor.setItems(provinceList);
        spSearchable.setItems(provinceList);

        spCustomColor.setItemColor(getResources().getColor(R.color.custom_item_color));
        spCustomColor.setSelectedItemColor(getResources().getColor(R.color.custom_selected_item_color));
        spCustomColor.setItemListColor(getResources().getColor(R.color.custom_item_list_color));

        spProvince.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spProvince.setError("You are selecting on spinner item -> \"" + provinceList.get(position) + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "On Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });

        spProvinceDialog.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spProvinceDialog.setError("You are selecting on spinner item -> \"" + provinceList.get(position) + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "On Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });

        spCustomColor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spCustomColor.setError("You are selecting on spinner item -> \"" + provinceList.get(position) + "\" . You can show it both in XML and programmatically and you can display as single line or multiple lines");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "On Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });

        spSearchable.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                spSearchable.setError("Your selected item is \"" + provinceList.get(position) + "\" .");
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(MainActivity.this, "On Nothing Selected", Toast.LENGTH_SHORT).show();
            }
        });

        spEmptyItem.setShowEmptyDropdown(false);
        // Use this method instead of OnClicklistener() to handle touch even.
        spEmptyItem.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    Toast.makeText(MainActivity.this, getString(R.string.empty_item_spinner_click_msg), Toast.LENGTH_SHORT).show();
                }
                return false;
            }
        });
    }
}
