package com.bivolaris.myapplication.ui.companies;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bivolaris.myapplication.R;
import com.bivolaris.myapplication.databinding.FragmentCompaniesBinding;
import com.bivolaris.myapplication.model.Company;
import com.bivolaris.myapplication.model.Employee;
import com.bivolaris.myapplication.ui.devices.DeviceAdapter;
import com.bivolaris.myapplication.ui.employees.EmployeeAdapter;
import com.bivolaris.myapplication.viewModel.CompaniesViewModel;
import com.bivolaris.myapplication.model.CreateCompanyRequest;
import com.bivolaris.myapplication.viewModel.DeviceViewModel;
import com.bivolaris.myapplication.viewModel.EmployeeViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;


public class CompaniesFragment extends Fragment {


    private FragmentCompaniesBinding binding;
    private CompaniesViewModel viewModel;


    public CompaniesFragment() {
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCompaniesBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        CompaniesAdapter adapter = new CompaniesAdapter(new CompaniesAdapter.OnCompanyClickListener() {
            @Override
            public void onEdit(Company company) {
                showEditCompanyDialog(company);
            }
            @Override
            public void onDelete(Company company) {
                showDeleteCompanyDialog(company);
            }
            @Override
            public void onViewDevices(Company company) {
                showAllCompanyDevicesDialog(company);
            }

            @Override
            public void onViewEmployees(Company company) {
                showAllCompanyEmployeesDialog(company);
            }
        });


        binding.companiesRecycler.setAdapter(adapter);
        binding.companiesRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
        viewModel = new ViewModelProvider(this).get(CompaniesViewModel.class);
        viewModel.getCompanies().observe(getViewLifecycleOwner(), adapter::submitList);
        viewModel.loadCompanies();
        binding.addCompanyButton.setOnClickListener(v -> showCreateCompanyDialog());




    }

    private void showCreateCompanyDialog() {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.create_company_dialog, null);

        TextInputEditText companyNameEditText = dialogView.findViewById(R.id.etCompanyName);
        TextInputEditText companyAddressEditText = dialogView.findViewById(R.id.etCompanyAddress);

        MaterialButton submitButton = dialogView.findViewById(R.id.btnSubmit);

        AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .create();

        submitButton.setOnClickListener(v -> {
            String companyName = companyNameEditText.getText().toString();
            String companyAddress = companyAddressEditText.getText().toString();

            if (companyName.isEmpty() || companyAddress.isEmpty()) {
                return;
            }
            CreateCompanyRequest request = new CreateCompanyRequest(companyName, companyAddress);
            viewModel.createCompany(request);
            dialog.dismiss();
        });
        dialog.show();
    }


    @SuppressLint("SetTextI18n")
    private void showEditCompanyDialog(Company company) {

        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.update_company_dialog, null);

        TextView title = dialogView.findViewById(R.id.tvDialogTitle);
        title.setText(company.getName());


        TextInputEditText etId = dialogView.findViewById(R.id.etCompanyId);
        TextInputEditText etName = dialogView.findViewById(R.id.etCompanyName);
        TextInputEditText etAddress = dialogView.findViewById(R.id.etCompanyAddress);

        etId.setText(company.getId().toString());
        etName.setText(company.getName());
        etAddress.setText(company.getAddress());

        MaterialButton submitButton = dialogView.findViewById(R.id.btnSubmit);
        submitButton.setText("Update");
        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        submitButton.setOnClickListener(v -> {
            String updatedCompanyId = Objects.requireNonNull(etId.getText()).toString();
            String updatedCompanyName = Objects.requireNonNull(etName.getText()).toString();
            String updatedCompanyAddress = Objects.requireNonNull(etAddress.getText()).toString();


            Company updatedCompany = new Company(updatedCompanyId, updatedCompanyName, updatedCompanyAddress);

            viewModel.updateCompany(updatedCompany);
            dialog.dismiss();
        });

        dialog.show();



    }

    private void showDeleteCompanyDialog(Company company) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.delete_company_dialog, null);

        TextView tvCompanyName = dialogView.findViewById(R.id.tvCompanyName);
        tvCompanyName.setText(company.getName());

        MaterialButton btnCancel = dialogView.findViewById(R.id.btnCancel);
        MaterialButton btnDelete = dialogView.findViewById(R.id.btnDelete);

        AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnDelete.setOnClickListener(v -> {
            viewModel.deleteCompany(company);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void showAllCompanyDevicesDialog(Company company){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.view_all_company_devices_dialog, null);

        TextView tvCompanyName = dialogView.findViewById(R.id.tvDialogCompanyName);
        RecyclerView rvDevives = dialogView.findViewById(R.id.rvDevices);
        TextView tvNoDevices = dialogView.findViewById(R.id.tvNoDevices);
        MaterialButton btnClose = dialogView.findViewById(R.id.btnClose);

        tvCompanyName.setText(company.getName());
        rvDevives.setLayoutManager(new LinearLayoutManager(getContext()));

        AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .create();

        DeviceViewModel deviceViewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        deviceViewModel.getDevices().observe(getViewLifecycleOwner(), devices -> {
            if(devices == null) return;
            if(devices.isEmpty()){
                tvNoDevices.setVisibility(View.VISIBLE);
                rvDevives.setVisibility(View.GONE);
            }else{
                tvNoDevices.setVisibility(View.GONE);
                rvDevives.setVisibility(View.VISIBLE);
            }


            rvDevives.setAdapter(new DeviceAdapter(null, R.layout.item_device));
            ((DeviceAdapter)rvDevives.getAdapter()).submitList(devices);
        });

        deviceViewModel.loadDevicesByCompany(company.getId());

        btnClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

    }


    private void showAllCompanyEmployeesDialog(Company company){
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View dialogView = inflater.inflate(R.layout.view_all_company_employees_dialog, null);

        TextView tvCompanyName = dialogView.findViewById(R.id.tvCompanyName);
        RecyclerView rvEmployees = dialogView.findViewById(R.id.rvEmployees);
        TextView tvNoEmployees = dialogView.findViewById(R.id.tvNoEmployees);
        MaterialButton btnClose = dialogView.findViewById(R.id.btnClose);

        tvCompanyName.setText(company.getName());
        rvEmployees.setLayoutManager(new LinearLayoutManager(getContext()));

        AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .create();

        EmployeeViewModel employeeViewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);

        EmployeeAdapter employeeAdapter = new EmployeeAdapter(null, R.layout.item_employee);
        rvEmployees.setAdapter(employeeAdapter);

        Observer<List<Employee>> observer = employees -> {
            if(employees == null) {
                rvEmployees.setVisibility(View.GONE);
                tvNoEmployees.setVisibility(View.GONE);
                return;
            }
            if(employees.isEmpty()){
                tvNoEmployees.setVisibility(View.VISIBLE);
                rvEmployees.setVisibility(View.GONE);
            }else{
                tvNoEmployees.setVisibility(View.GONE);
                rvEmployees.setVisibility(View.VISIBLE);
            }

            employeeAdapter.submitList(employees);
        };

        employeeViewModel.getEmployees().observe(getViewLifecycleOwner(), observer);
        employeeViewModel.loadEmployeesByCompany(company.getId());

        btnClose.setOnClickListener(v -> dialog.dismiss());
        dialog.setOnDismissListener(d -> employeeViewModel.getEmployees().removeObserver(observer));

        dialog.show();
    }

}
