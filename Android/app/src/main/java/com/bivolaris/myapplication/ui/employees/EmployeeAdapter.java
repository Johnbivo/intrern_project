package com.bivolaris.myapplication.ui.employees;

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
import com.bivolaris.myapplication.model.Employee;
import com.google.android.material.button.MaterialButton;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class EmployeeAdapter extends ListAdapter<Employee,EmployeeAdapter.EmployeeViewHolder> {


    private final OnEmployeeClickListener listener;
    private final int layoutId;



    public EmployeeAdapter(OnEmployeeClickListener listener){
        this(listener, R.layout.item_card_employee);
    }

    public EmployeeAdapter(OnEmployeeClickListener listener, int layoutId){
        super(new EmployeeDiffCallback());
        this.listener = listener;
        this.layoutId = layoutId;
    }


    @NotNull
    @Override
    public EmployeeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(layoutId, parent, false);
        return new EmployeeViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeViewHolder holder, int position) {
        Employee employee = getItem(position);
        holder.bind(employee);
    }



    public static class EmployeeDiffCallback extends DiffUtil.ItemCallback<Employee>{

        @Override
        public boolean areItemsTheSame(@NonNull Employee oldItem, @NonNull Employee newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull Employee oldItem, @NonNull Employee newItem) {
            return oldItem.equals(newItem);
        }
    }




    static class EmployeeViewHolder extends RecyclerView.ViewHolder {
        TextView employeeName;
        TextView employeeEmail;

        MaterialButton editButton;
        MaterialButton deleteButton;
        MaterialButton assignButton;
        MaterialButton unassignButton;

        LinearLayout actionsLayout;

        boolean expanded = false;


        public EmployeeViewHolder(View itemView, OnEmployeeClickListener listener) {
            super(itemView);



            employeeName = itemView.findViewById(R.id.employeeName);
            employeeEmail = itemView.findViewById(R.id.employeeEmail);
            editButton = itemView.findViewById(R.id.editButton);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            assignButton = itemView.findViewById(R.id.assignEmployeeToCompany);
            unassignButton = itemView.findViewById(R.id.unassignEmployeeFromCompany);
            actionsLayout = itemView.findViewById(R.id.actionsLayout);

            if (editButton != null){
                editButton.setOnClickListener(v -> {
                    Employee employee = (Employee) editButton.getTag();
                    if (listener != null) listener.onEdit(employee);
                });
            }

            if (deleteButton != null){
                deleteButton.setOnClickListener(v -> {
                    Employee employee = (Employee) deleteButton.getTag();
                    if (listener != null) listener.onDelete(employee);
                });
            }

            if (assignButton != null){
                assignButton.setOnClickListener(v -> {
                    Employee employee = (Employee) assignButton.getTag();
                    if (listener != null) listener.onAssignEmployee(employee);
                });
            }

            if (unassignButton != null){
                unassignButton.setOnClickListener(v -> {
                    Employee employee = (Employee) unassignButton.getTag();
                    if (listener != null) listener.onUnassignEmployee(employee);
                });
            }

            itemView.setOnClickListener(v -> {
                if (actionsLayout != null) {
                    if (expanded) {
                        actionsLayout.setVisibility(View.GONE);
                        expanded = false;
                    } else {
                        actionsLayout.setVisibility(View.VISIBLE);
                        expanded = true;
                    }
                }
            });

        }

        public void bind(Employee employee) {

            if (employeeName != null) employeeName.setText(employee.getName());
            if (employeeEmail != null) employeeEmail.setText(employee.getEmail());
            if (editButton != null) editButton.setTag(employee);
            if (deleteButton != null) deleteButton.setTag(employee);
            if (assignButton != null) assignButton.setTag(employee);
            if (unassignButton != null) unassignButton.setTag(employee);

            if (assignButton != null && unassignButton != null) {
                boolean isAssigned = employee.getCompanyId() != null;
                if (isAssigned) {
                    assignButton.setEnabled(false);
                    assignButton.setAlpha(0.4f);
                    unassignButton.setEnabled(true);
                    unassignButton.setAlpha(1f);
                } else {
                    assignButton.setEnabled(true);
                    assignButton.setAlpha(1f);
                    unassignButton.setEnabled(false);
                    unassignButton.setAlpha(0.4f);
                }
            }
        }
    }

    public interface OnEmployeeClickListener {
        void onEdit(Employee employee);
        void onDelete(Employee employee);
        void onAssignEmployee(Employee employee);
        void onUnassignEmployee(Employee employee);
    }
}
