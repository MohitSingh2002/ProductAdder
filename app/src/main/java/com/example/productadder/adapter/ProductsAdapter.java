package com.example.productadder.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productadder.databinding.VarientItemLayoutBinding;
import com.example.productadder.databinding.WeightItemLayoutBinding;
import com.example.productadder.model.Product;

import java.util.List;

public class ProductsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    List<Product> productList;

    public ProductsAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Product.WEIGHT_BASED) {
            WeightItemLayoutBinding b = WeightItemLayoutBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
            );
            return new WeightBasedProductViewHolder(b);
        } else {
            VarientItemLayoutBinding b = VarientItemLayoutBinding.inflate(
                    LayoutInflater.from(context),
                    parent,
                    false
            );
            return new VarientsBasedProductViewHolder(b);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Product product = productList.get(position);
        if (product.type == Product.WEIGHT_BASED) {
            WeightItemLayoutBinding b = ((WeightBasedProductViewHolder) holder).b;
            b.nameWeight.setText(product.name);
            b.priceWeight.setText("Rs. " + product.pricePerKg);
            b.minQtyWeight.setText("MinQty - " + product.minQty);
        } else {
            VarientItemLayoutBinding b = ((VarientsBasedProductViewHolder) holder).b;
            b.nameVarient.setText(product.name);
            b.varientsVarients.setText(product.varientsString());
        }
    }

    @Override
    public int getItemViewType(int position) {
        return productList.get(position).type;
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public static class WeightBasedProductViewHolder extends RecyclerView.ViewHolder {

        WeightItemLayoutBinding b;

        public WeightBasedProductViewHolder(@NonNull WeightItemLayoutBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

        public static class VarientsBasedProductViewHolder extends RecyclerView.ViewHolder {

        VarientItemLayoutBinding b;

        public VarientsBasedProductViewHolder(@NonNull VarientItemLayoutBinding b) {
            super(b.getRoot());
            this.b = b;
        }
    }

}
