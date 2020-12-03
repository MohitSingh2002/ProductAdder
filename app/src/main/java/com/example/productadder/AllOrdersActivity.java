package com.example.productadder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.productadder.adapter.AllOrdersAdapter;
import com.example.productadder.constants.Constants;
import com.example.productadder.databinding.ActivityAllOrdersBinding;
import com.example.productadder.fcm.FCMSender;
import com.example.productadder.fcm.MessageFormatter;
import com.example.productadder.model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.SetOptions;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class AllOrdersActivity extends AppCompatActivity {
    
    ActivityAllOrdersBinding binding;

    List<Order> orderList = new ArrayList<>();

    MyApp myApp;

    AllOrdersAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAllOrdersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        myApp = (MyApp) getApplicationContext();
        
        fetchAllOrdersFromDB();
        
    }

    private void fetchAllOrdersFromDB() {
        myApp.db.collection(Constants.ORDERS)
                .orderBy(Constants.ORDER_TIME, Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        for (QueryDocumentSnapshot snapshot : task.getResult()) {
                            Order order = snapshot.toObject(Order.class);
                            orderList.add(order);
                        }
                        setupAdapter();
                    }
                });
    }

    private void setupAdapter() {
        adapter = new AllOrdersAdapter(AllOrdersActivity.this, orderList);
        binding.recyclerViewAllOrders.setAdapter(adapter);
        binding.recyclerViewAllOrders.setLayoutManager(new LinearLayoutManager(AllOrdersActivity.this));
        binding.recyclerViewAllOrders.addItemDecoration(
                new DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        );
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.declined:
                declineOrder();
                return true;

            case R.id.delivered:
                deliverOrder();
                return true;
        }
        return super.onContextItemSelected(item);
    }

    private void declineOrder() {
        Order orderToBeDecline = adapter.orderList.get(adapter.lastSelectedItemPosition);
        orderToBeDecline.action = Order.OrderStatus.DECLINED;
        adapter.notifyDataSetChanged();
        sendNotificationToUser("DECLINED", orderToBeDecline);
        updateFirestore(orderToBeDecline);
    }

    private void deliverOrder() {
        Order orderToBeDeliver = adapter.orderList.get(adapter.lastSelectedItemPosition);
        orderToBeDeliver.action = Order.OrderStatus.DELIVERED;
        adapter.notifyDataSetChanged();
        sendNotificationToUser("DELIVERED", orderToBeDeliver);
        updateFirestore(orderToBeDeliver);
    }

    private void sendNotificationToUser(String message, Order order) {
        new FCMSender().send(MessageFormatter.getSampleMessage("users", "Your Order!", order.orderID.substring(0, 28), message), new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                Log.e("bxj", "Failure");
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                Log.e("cjsc", "Success");
            }
        });
    }

    private void updateFirestore(Order order) {
        myApp.db.collection(Constants.ORDERS).document(order.orderID)
                .set(order, SetOptions.merge())
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(AllOrdersActivity.this, "Order status updated!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AllOrdersActivity.this, "Order status failed to update!", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
