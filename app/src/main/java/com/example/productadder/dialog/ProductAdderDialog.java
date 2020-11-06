package com.example.productadder.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.productadder.R;
import com.example.productadder.databinding.ProductAdderDialogBinding;
import com.example.productadder.model.Product;

import java.util.regex.Pattern;

public class ProductAdderDialog {

    ProductAdderDialogBinding b;
    Product product;

    public void showDialog(Context context, OnProductAddListener listener) {
        b = ProductAdderDialogBinding.inflate(
                LayoutInflater.from(context)
        );

        new AlertDialog.Builder(context)
                .setTitle("Add Peoduct")
                .setView(b.getRoot())
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(areProductDetailsValid()) {
                            listener.onProductAdded(product);
                        } else {
                            Toast.makeText(context, "Invalid Details!", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        listener.onCancelled();
                    }
                })
                .show();
        setupRadioGroup();
    }

    private boolean areProductDetailsValid() {
        String name = b.name.getText().toString().trim();
        if(name.isEmpty()) {
            return false;
        }
        switch (b.radioGroupItemBased.getCheckedRadioButtonId()) {
            case R.id.radio_button_weight_based:
                String pricePerKg = b.pricePerKg.getText().toString().trim();
                String minQty = b.minQuantity.getText().toString().trim();
                if (pricePerKg.isEmpty() || minQty.isEmpty() || !minQty.matches("\\d+(kg|g)")) {
                    return false;
                }
                product = new Product(name, Integer.parseInt(pricePerKg), extractMinimumQuantity(minQty));
                return true;

            case R.id.radio_button_varient_based:
                String varients = b.varients.getText().toString().trim();
                product = new Product(name);
                return areVarientsDetailsValid(varients);
        }
        return false;
    }

    private boolean areVarientsDetailsValid(String varients) {
        if (varients.length() == 0) {
            return true;
        }
        String[] vs = varients.split("\n");
        Pattern pattern = Pattern.compile("^\\w+(\\s|\\w)+,\\d+$");
        for (String varient : vs) {
            if (!pattern.matcher(varient).matches()) {
                return false;
            }
        }
        product.fromVarientStrings(vs);
        return true;
    }

    private float extractMinimumQuantity(String minQty) {
        if (minQty.contains("kg")) {
            return Float.parseFloat(minQty.replace("kg", ""));
        } else {
            return Float.parseFloat(minQty.replace("g", "")) / 1000f;
        }
    }

    private void setupRadioGroup() {
        b.radioGroupItemBased.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radio_button_weight_based) {
                    b.weightBasedRoot.setVisibility(View.VISIBLE);
                    b.variantsRoot.setVisibility(View.GONE);
                } else {
                    b.variantsRoot.setVisibility(View.VISIBLE);
                    b.weightBasedRoot.setVisibility(View.GONE);
                }
            }
        });
    }

    public interface OnProductAddListener {
        void onProductAdded(Product product);
        void onCancelled();
    }

}
