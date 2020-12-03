package com.example.productadder.adapter;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.productadder.AllOrdersActivity;
import com.example.productadder.MainActivity;
import com.example.productadder.R;
import com.example.productadder.databinding.OrderDetailLayoutBinding;
import com.example.productadder.databinding.OrderItemsListBinding;
import com.example.productadder.model.Order;

import java.util.ArrayList;
import java.util.List;

public class AllOrdersAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public Context context;
    public List<Order> orderList;
    public int lastSelectedItemPosition;

    public AllOrdersAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = new ArrayList<>(orderList);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderDetailLayoutBinding b = OrderDetailLayoutBinding.inflate(
                LayoutInflater.from(context),
                parent,
                false
        );
        return new AllOrdersViewHolder(b);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Order order = orderList.get(position);
        OrderDetailLayoutBinding b = ((AllOrdersViewHolder) holder).binding;
        b.orderUserName.setText("" + order.name);
        b.orderUserPhoneNo.setText("" + order.phoneNo);
        b.orderUserAddress.setText("" + order.address);
        b.orderTotalItems.setText("Items: " + order.total_items);
        b.orderTotalPrice.setText("Rs. " + order.total_price);
        setupOrderStatus(b, order.action);
        setupProductsView(b, order);
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                lastSelectedItemPosition = holder.getAdapterPosition();
                return false;
            }
        });
    }

    private void setupOrderStatus(OrderDetailLayoutBinding b, int action) {
        if (action == Order.OrderStatus.PLACED) {
            b.orderStatus.setText("PLACED");
            b.orderStatus.setTextColor(Color.GREEN);
            showContextMenu(b.getRoot(), action);
        } else if (action == Order.OrderStatus.DECLINED) {
            b.orderStatus.setText("DECLINED");
            b.orderStatus.setTextColor(Color.RED);
        } else {
            b.orderStatus.setText("DELIVERED");
            b.orderStatus.setTextColor(Color.YELLOW);
        }
    }

    private void setupProductsView(OrderDetailLayoutBinding b, Order order) {
        for (int i = 0; i< order.orderItems.size(); i++) {
            OrderItemsListBinding bindingNew = OrderItemsListBinding.inflate(LayoutInflater.from(context));
            bindingNew.itemName.setText("" + order.orderItems.get(i).name);
            bindingNew.itemQuantity.setText("Total Items: " + order.orderItems.get(i).quantity);
            bindingNew.itemPrice.setText("Rs. " + order.orderItems.get(i).price);

            b.allOrderItems.addView(bindingNew.getRoot());

        }
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    private void showContextMenu(ConstraintLayout root, int action) {
        root.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                if (!(context instanceof AllOrdersActivity)) {
                    return;
                }
                if (action == Order.OrderStatus.PLACED) {
                    AllOrdersActivity activity = ((AllOrdersActivity) context);
                    activity.getMenuInflater().inflate(R.menu.order_status_context_menu, menu);
                }
            }
        });
    }

    public static class AllOrdersViewHolder extends RecyclerView.ViewHolder {
        OrderDetailLayoutBinding binding;

        public AllOrdersViewHolder(@NonNull OrderDetailLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
