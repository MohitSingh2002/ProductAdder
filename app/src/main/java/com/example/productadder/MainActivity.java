package com.example.productadder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.add_product) {
            new ProductAdderDialog().showDialog(MainActivity.this, new ProductAdderDialog.OnProductAddListener() {
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
        return super.onOptionsItemSelected(item);
    }

}
