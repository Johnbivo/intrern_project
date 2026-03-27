package com.bivolaris.myapplication.ui.devices;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bivolaris.myapplication.R;
import com.bivolaris.myapplication.model.Device;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.Objects;

public class DeviceAdapter extends ListAdapter<Device,DeviceAdapter.DeviceViewHolder> {

    private final OnDeviceClickListener listener;
    private final int layoutId;

    public DeviceAdapter(OnDeviceClickListener listener){
        this(listener, R.layout.card_device_item);
    }
    
    public DeviceAdapter(OnDeviceClickListener listener, int layoutId){
        super(new DeviceDiffCallback());
        this.listener = listener;
        this.layoutId = layoutId;
    }
    
    public DeviceAdapter(List<Device> devices){
        this(null, R.layout.item_device);
        submitList(devices);
    }

    @NonNull
    @Override
    public DeviceViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new DeviceViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull DeviceViewHolder holder, int position) {
            Device device = getItem(position);
            holder.bind(device);
    }



    public static class DeviceDiffCallback extends DiffUtil.ItemCallback<Device>{

        @Override
        public boolean areItemsTheSame(@NonNull Device oldItem, @NonNull Device newItem) {
            return Objects.equals(oldItem.getSerialNumber(), newItem.getSerialNumber());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Device oldItem, @NonNull Device newItem) {
            return oldItem.equals(newItem);
        }

    }





    static class DeviceViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout actionsLayout;
        private TextView deviceName;
        private TextView deviceType;

        private MaterialButton editButton;
        private MaterialButton deleteButton;
        private MaterialButton assignEmployeeButton;
        private MaterialButton unassignEmployeeButton;

        private MaterialButton assignCompanyButton;
        private MaterialButton unassignCompanyButton;


        private boolean expanded = false;


        public DeviceViewHolder(@NonNull View itemView, OnDeviceClickListener listener) {
            super(itemView);

            actionsLayout = itemView.findViewById(R.id.actionsLayout);
            deviceName = itemView.findViewById(R.id.deviceName);
            deviceType = itemView.findViewById(R.id.deviceType);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            assignEmployeeButton = itemView.findViewById(R.id.assignToEmployeeButton);
            unassignEmployeeButton = itemView.findViewById(R.id.unassignFromEmployeeButton);
            assignCompanyButton = itemView.findViewById(R.id.assignToCompanyButton);
            unassignCompanyButton = itemView.findViewById(R.id.unassignFromCompanyButton);


            if (itemView.getId() == R.id.card_device_root || actionsLayout != null) {
                itemView.setOnClickListener(v -> {
                    if (expanded) {
                        if (actionsLayout != null) actionsLayout.setVisibility(View.GONE);
                        expanded = false;
                    } else {
                        if (actionsLayout != null) actionsLayout.setVisibility(View.VISIBLE);
                        expanded = true;
                    }
                });
            }

            if (editButton != null) {
                editButton.setOnClickListener(v -> {
                    Device device = (Device) editButton.getTag();
                    if (listener != null) listener.onEdit(device);
                });
            }

            if (deleteButton != null) {
                deleteButton.setOnClickListener(v -> {
                    Device device = (Device) deleteButton.getTag();
                    if (listener != null) listener.onDelete(device);
                });
            }

            if (assignEmployeeButton != null) {
                assignEmployeeButton.setOnClickListener(v -> {
                    Device device = (Device) assignEmployeeButton.getTag();
                    if (listener != null) listener.onAssignEmployee(device);
                });
            }

            if (unassignEmployeeButton != null) {
                unassignEmployeeButton.setOnClickListener(v -> {
                    Device device = (Device) unassignEmployeeButton.getTag();
                    if (listener != null) listener.onUnassignEmployee(device);
                });
            }

            if (assignCompanyButton != null) {
                assignCompanyButton.setOnClickListener(v -> {
                    Device device = (Device) assignCompanyButton.getTag();
                    if (listener != null) listener.onAssignCompany(device);
                });
            }

            if (unassignCompanyButton != null) {
                unassignCompanyButton.setOnClickListener(v -> {
                    Device device = (Device) unassignCompanyButton.getTag();
                    if (listener != null) listener.onUnassignCompany(device);
                });
            }


        }

        public void bind(Device device) {
            if (deviceName != null) this.deviceName.setText(device.getName());
            if (deviceType != null) this.deviceType.setText(device.getType());

            if (editButton != null) editButton.setTag(device);
            if (deleteButton != null) deleteButton.setTag(device);
            if (assignEmployeeButton != null) assignEmployeeButton.setTag(device);
            if (unassignEmployeeButton != null) unassignEmployeeButton.setTag(device);
            if (assignCompanyButton != null) assignCompanyButton.setTag(device);
            if (unassignCompanyButton != null) unassignCompanyButton.setTag(device);


            if (assignEmployeeButton != null && unassignEmployeeButton != null) {
                boolean isAssigned = device.getEmployeeId() != null;
                if (isAssigned) {
                    assignEmployeeButton.setEnabled(false);
                    assignEmployeeButton.setAlpha(0.4f);
                    unassignEmployeeButton.setEnabled(true);
                    unassignEmployeeButton.setAlpha(1f);
                } else {
                    assignEmployeeButton.setEnabled(true);
                    assignEmployeeButton.setAlpha(1f);
                    unassignEmployeeButton.setEnabled(false);
                    unassignEmployeeButton.setAlpha(0.4f);
                }
            }
            if (assignCompanyButton != null && unassignCompanyButton != null) {
                boolean isAssigned = device.getCompanyId() != null;
                if (isAssigned) {
                    assignCompanyButton.setEnabled(false);
                    assignCompanyButton.setAlpha(0.4f);
                    unassignCompanyButton.setEnabled(true);
                    unassignCompanyButton.setAlpha(1f);
                } else {
                    assignCompanyButton.setEnabled(true);
                    assignCompanyButton.setAlpha(1f);
                    unassignCompanyButton.setEnabled(false);
                    unassignCompanyButton.setAlpha(0.4f);
                }
            }
        }
    }



    public interface OnDeviceClickListener {
        void onEdit(Device device);
        void onDelete(Device device);
        void onAssignEmployee(Device device);
        void onUnassignEmployee(Device device);
        void onAssignCompany(Device device);
        void onUnassignCompany(Device device);
    }
}


