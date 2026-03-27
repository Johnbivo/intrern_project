package com.bivolaris.myapplication.ui.employees;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.bivolaris.myapplication.R;
import com.bivolaris.myapplication.databinding.FragmentEmployeeBinding;
import com.bivolaris.myapplication.model.Company;
import com.bivolaris.myapplication.model.CreateEmployeeRequest;
import com.bivolaris.myapplication.model.Employee;
import com.bivolaris.myapplication.viewModel.CompaniesViewModel;
import com.bivolaris.myapplication.viewModel.EmployeeViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class EmployeeFragment extends Fragment {

    private FragmentEmployeeBinding binding;
    private EmployeeViewModel viewModel;
    private CompaniesViewModel companiesViewModel;
    private String selectedCompanyId;


    public EmployeeFragment() {}


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEmployeeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        EmployeeAdapter adapter = new EmployeeAdapter(new EmployeeAdapter.OnEmployeeClickListener() {

            @Override
            public void onEdit(Employee employee) {showEditEmployeeDialog(employee);}
            @Override
            public void onDelete(Employee employee) {showDeleteEmployeeDialog(employee);}
            @Override
            public void onAssignEmployee(Employee employee) {showAssignEmployeeDialog(employee);}
            @Override
            public void onUnassignEmployee(Employee employee) {showUnassignEmployeeDialog(employee);}
        });

        binding.employeeRecycler.setAdapter(adapter);
        binding.employeeRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewModel = new ViewModelProvider(this).get(EmployeeViewModel.class);

        companiesViewModel = new ViewModelProvider(requireActivity()).get(CompaniesViewModel.class);

        viewModel.getEmployees().observe(getViewLifecycleOwner(), adapter::submitList);
        viewModel.loadEmployees();
        binding.addEmployeeButton.setOnClickListener(v -> showAddEmployeeDialog());


    }


    private void showAddEmployeeDialog() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.create_employee_dialog, null);

        TextInputEditText employeeNameEditText = dialogView.findViewById(R.id.etEmployeeName);
        TextInputEditText employeeEmailEditText = dialogView.findViewById(R.id.etEmployeeEmail);
        MaterialButton submitButton = dialogView.findViewById(R.id.btnSubmit);

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        submitButton.setOnClickListener(v -> {
            String employeeName = Objects.requireNonNull(employeeNameEditText.getText()).toString();
            String employeeEmail = Objects.requireNonNull(employeeEmailEditText.getText()).toString();


            CreateEmployeeRequest request = new CreateEmployeeRequest(employeeName, employeeEmail, null);
            viewModel.createEmployee(request);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void showEditEmployeeDialog(Employee employee){
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.update_employee_dialog, null);

        TextInputEditText employeeIdEditText = dialogView.findViewById(R.id.etEmployeeId);
        TextInputEditText employeeNameEditText = dialogView.findViewById(R.id.etEmployeeName);
        TextInputEditText employeeEmailEditText = dialogView.findViewById(R.id.etEmployeeEmail);
        MaterialButton submitButton = dialogView.findViewById(R.id.btnSubmit);

        employeeIdEditText.setText(employee.getId());
        employeeNameEditText.setText(employee.getName());
        employeeEmailEditText.setText(employee.getEmail());
        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();
        submitButton.setOnClickListener(v -> {
            String employeeName = Objects.requireNonNull(employeeNameEditText.getText()).toString();
            String employeeEmail = Objects.requireNonNull(employeeEmailEditText.getText()).toString();

            Employee updatedEmployee = Employee.builder()
                            .id(employee.getId())
                            .name(employeeName)
                            .email(employeeEmail)
                            .companyId(employee.getCompanyId())
                            .build();

            viewModel.updateEmployee(updatedEmployee);
            dialog.dismiss();
        });
        dialog.show();
    }


    private void showDeleteEmployeeDialog(Employee employee){
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.delete_employee_dialog, null);

        TextView employeeNameTextView = dialogView.findViewById(R.id.tvEmployeeName);
        MaterialButton deleteButton = dialogView.findViewById(R.id.btnDelete);
        MaterialButton cancelButton = dialogView.findViewById(R.id.btnCancel);

        employeeNameTextView.setText(employee.getName());
        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();
        deleteButton.setOnClickListener(v -> {
            viewModel.deleteEmployee(employee.getId());
            dialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    public void showAssignEmployeeDialog(Employee employee){
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.assign_employee_to_company, null);

        AutoCompleteTextView autoCompleteTextView = dialogView.findViewById(R.id.companyDropdown);
        MaterialButton cancelButton = dialogView.findViewById(R.id.cancelButton);
        MaterialButton assignButton = dialogView.findViewById(R.id.assignButton);

        selectedCompanyId = null;

        companiesViewModel.getCompanies().observe(getViewLifecycleOwner(), companies -> {
            if (companies != null && !companies.isEmpty()) {
                List<String> companyNames = companies.stream()
                        .map(Company::getName)
                        .collect(Collectors.toList());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, companyNames);
                autoCompleteTextView.setAdapter(adapter);
                autoCompleteTextView.setOnItemClickListener((parent, view, position, id) -> {
                    selectedCompanyId = companies.get(position).getId();
                });

            }
        });
        if (companiesViewModel.getCompanies().getValue() == null || companiesViewModel.getCompanies().getValue().isEmpty()) {
            companiesViewModel.loadCompanies();
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        assignButton.setOnClickListener(v -> {
            if (selectedCompanyId != null){
                viewModel.updateEmployee(Employee.builder()
                        .id(employee.getId())
                        .name(employee.getName())
                        .email(employee.getEmail())
                        .companyId(selectedCompanyId)
                        .build());
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }

    public void showUnassignEmployeeDialog(Employee employee){

        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.unassign_employee_from_company, null);

        TextView employeeName = dialogView.findViewById(R.id.tvEmployeeName);
        MaterialButton cancelButton = dialogView.findViewById(R.id.btnCancel);
        MaterialButton unassignButton = dialogView.findViewById(R.id.btnUnassign);


        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        employeeName.setText(employee.getName());
        unassignButton.setOnClickListener(v -> {
            viewModel.updateEmployee(Employee.builder()
                    .id(employee.getId())
                    .name(employee.getName())
                    .email(employee.getEmail())
                    .companyId(null)
                    .build());
            dialog.dismiss();
        });
        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }



}
