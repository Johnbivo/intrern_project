package com.bivolaris.myapplication.ui.companies;

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
import com.bivolaris.myapplication.model.Company;
import com.google.android.material.button.MaterialButton;

import java.util.Objects;

public class CompaniesAdapter extends ListAdapter<Company, CompaniesAdapter.CompanyViewHolder> {

    private final OnCompanyClickListener listener;
    public CompaniesAdapter(OnCompanyClickListener listener) {
        super(new CompanyDiffCallback());
        this.listener = listener;
    }

    @NonNull
    @Override
    public CompanyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_company, parent, false);
        return new CompanyViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CompanyViewHolder holder, int position) {
        Company company = getItem(position);
        holder.bind(company);
    }



    public static class CompanyDiffCallback extends DiffUtil.ItemCallback<Company> {
        @Override
        public boolean areItemsTheSame(Company oldItem, @NonNull Company newItem) {
            return Objects.equals(oldItem.getId(), newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(Company oldItem, @NonNull Company newItem) {
            return oldItem.equals(newItem);
        }
    }


    public static class CompanyViewHolder extends RecyclerView.ViewHolder {
        private TextView companyName;
        private TextView companyAddress;
        private LinearLayout actionsLayout;
        private MaterialButton deleteButton;
        private MaterialButton editButton;
        private MaterialButton viewDevicesButton;
        private MaterialButton viewEmployeesButton;


        private boolean expanded = false;
        public CompanyViewHolder(@NonNull View itemView, OnCompanyClickListener listener) {
            super(itemView);

            companyName = itemView.findViewById(R.id.companyName);
            companyAddress = itemView.findViewById(R.id.companyAddress);
            actionsLayout = itemView.findViewById(R.id.actionsLayout);
            deleteButton = itemView.findViewById(R.id.deleteButton);
            editButton = itemView.findViewById(R.id.editButton);
            viewDevicesButton = itemView.findViewById(R.id.viewAllCompanyDevicesButton);
            viewEmployeesButton = itemView.findViewById(R.id.viewAllCompanyEmployeesButton);


            itemView.setOnClickListener(v -> {
                if (expanded) {
                    actionsLayout.setVisibility(View.GONE);
                    expanded = false;
                } else {
                    actionsLayout.setVisibility(View.VISIBLE);
                    expanded = true;
                }
            });

            editButton.setOnClickListener(v -> {
                Company company = (Company) editButton.getTag();
                listener.onEdit(company);
            });

            deleteButton.setOnClickListener(v -> {
                Company company = (Company) deleteButton.getTag();
                listener.onDelete(company);
            });

            viewDevicesButton.setOnClickListener(v -> {
                Company company = (Company) viewDevicesButton.getTag();
                listener.onViewDevices(company);
            });

            viewEmployeesButton.setOnClickListener(v -> {
                Company company = (Company) viewEmployeesButton.getTag();
                listener.onViewEmployees(company);
            });
        }
        public void bind(Company company){
            this.companyName.setText(company.getName());
            this.companyAddress.setText(company.getAddress());
            editButton.setTag(company);
            deleteButton.setTag(company);
            viewDevicesButton.setTag(company);
            viewEmployeesButton.setTag(company);
        }
    }


    public interface OnCompanyClickListener {
        void onEdit(Company company);
        void onDelete(Company company);
        void onViewDevices(Company company);
        void onViewEmployees(Company company);
    }



}
