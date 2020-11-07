package com.example.productadder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.productadder.adapter.ProductsAdapter;
import com.example.productadder.databinding.ActivityMainBinding;
import com.example.productadder.dialog.ProductAdderDialog;
import com.example.productadder.model.Product;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    List<Product> list;
    ProductsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setupProductsList();

    }

    private void setupProductsList() {
        list = new ArrayList<>();
        adapter = new ProductsAdapter(MainActivity.this, list);
        binding.recyclerView.setAdapter(adapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        binding.recyclerView.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_product) {
            addProduct();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void addProduct() {
        new ProductAdderDialog(ProductAdderDialog.PRODUCT_ADD).showDialog(MainActivity.this , "Add", new ProductAdderDialog.OnProductAddListener() {
            @Override
            public void onProductAdded(Product product) {
//                    Toast.makeText(MainActivity.this, product.toString(), Toast.LENGTH_LONG).show();
                list.add(product);
                adapter.notifyItemInserted(list.size() - 1);
            }

            @Override
            public void onCancelled() {
                Toast.makeText(MainActivity.this, "Cancel Pressed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.product_edit:
                editProduct();
                return true;

            case R.id.product_remove:
                removeProduct();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void removeProduct() {
        new AlertDialog.Builder(MainActivity.this)
                .setTitle("Do you really want to delete this product?")
                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.remove(adapter.lastSelectedItemPosition);
                        adapter.notifyItemRemoved(adapter.lastSelectedItemPosition);
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(MainActivity.this, "Cancelled!", Toast.LENGTH_SHORT).show();
                    }
                })
                .show();
    }

    private void editProduct() {



        Product productToBeEdited = list.get(adapter.lastSelectedItemPosition);
        ProductAdderDialog productAdderDialog = new ProductAdderDialog(ProductAdderDialog.PRODUCT_EDIT);
        productAdderDialog.product = productToBeEdited;
        productAdderDialog.showDialog(MainActivity.this, "Edit", new ProductAdderDialog.OnProductAddListener() {
            @Override
            public void onProductAdded(Product product) {
                list.set(adapter.lastSelectedItemPosition, product);
                adapter.notifyItemChanged(adapter.lastSelectedItemPosition);
                Toast.makeText(MainActivity.this, "Editted!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled() {
                Toast.makeText(MainActivity.this, "Cancel Pressed!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
