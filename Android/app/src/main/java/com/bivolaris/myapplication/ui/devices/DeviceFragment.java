package com.bivolaris.myapplication.ui.devices;

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
import com.bivolaris.myapplication.databinding.FragmentDeviceBinding;
import com.bivolaris.myapplication.model.Company;
import com.bivolaris.myapplication.model.CreateDeviceRequest;
import com.bivolaris.myapplication.model.Device;
import com.bivolaris.myapplication.model.Employee;
import com.bivolaris.myapplication.viewModel.CompaniesViewModel;
import com.bivolaris.myapplication.viewModel.DeviceViewModel;
import com.bivolaris.myapplication.viewModel.EmployeeViewModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.MaterialAutoCompleteTextView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class DeviceFragment extends Fragment {


    private FragmentDeviceBinding binding;
    private DeviceViewModel viewModel;
    private CompaniesViewModel companiesViewModel;
    private EmployeeViewModel employeeViewModel;
    private String selectedCompanyId;
    private String selectedEmployeeId;

    public DeviceFragment() {}




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentDeviceBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        DeviceAdapter adapter = new DeviceAdapter(new DeviceAdapter.OnDeviceClickListener() {
            @Override
            public void onEdit(Device device) {
                showEditDeviceDialog(device);
            }
            @Override
            public void onDelete(Device device) {
                showDeleteDeviceDialog(device);
            }
            @Override
            public void onAssignEmployee(Device device) {
                showAssignEmployeeDialog(device);
            }
            @Override
            public void onUnassignEmployee(Device device){
                showUnassignEmployeeDialog(device);
            }
            @Override
            public void onAssignCompany(Device device) {showAssignCompanyDialog(device);}
            @Override
            public void onUnassignCompany(Device device) {showUnassignCompanyDialog(device);}

        });

        binding.devicesRecycler.setAdapter(adapter);
        binding.devicesRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
        viewModel = new ViewModelProvider(this).get(DeviceViewModel.class);
        companiesViewModel = new ViewModelProvider(requireActivity()).get(CompaniesViewModel.class);
        employeeViewModel = new ViewModelProvider(requireActivity()).get(EmployeeViewModel.class);

        viewModel.getDevices().observe(getViewLifecycleOwner(), adapter::submitList);
        viewModel.loadDevices();
        binding.addDeviceButton.setOnClickListener(v -> showAddDeviceDialog());

    }

    private void showEditDeviceDialog(Device device) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.update_device_dialog, null);

        TextView title = dialogView.findViewById(R.id.tvDialogTitle);
        title.setText(getResources().getString(R.string.edit_device));

        TextInputEditText etSerialNumber = dialogView.findViewById(R.id.etDeviceSerialNumber);
        TextInputEditText etName = dialogView.findViewById(R.id.etDeviceName);
        TextInputEditText etType = dialogView.findViewById(R.id.etDeviceType);
        MaterialButton submitButton = dialogView.findViewById(R.id.btnSubmit);

        etSerialNumber.setText(device.getSerialNumber());
        etName.setText(device.getName());
        etType.setText(device.getType());
        submitButton.setText(getResources().getString(R.string.update));


        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        submitButton.setOnClickListener(v -> {
            String updatedSerialNumber = Objects.requireNonNull(etSerialNumber.getText()).toString();
            String updatedName = Objects.requireNonNull(etName.getText()).toString();
            String updatedType = Objects.requireNonNull(etType.getText()).toString();


            Device updatedDevice = new Device(device.getSerialNumber(), updatedName, updatedType,device.getCompanyId(), device.getEmployeeId());
            viewModel.updateDevice(updatedDevice);
            dialog.dismiss();
        });
        dialog.show();

    }
    private void showDeleteDeviceDialog(Device device) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.delete_device_dialog, null);

        TextView tvDeviceName = dialogView.findViewById(R.id.tvDeviceName);
        tvDeviceName.setText(device.getName());

        MaterialButton btnCancel = dialogView.findViewById(R.id.btnCancel);
        MaterialButton btnDelete = dialogView.findViewById(R.id.btnDelete);

        AlertDialog dialog = new MaterialAlertDialogBuilder(getContext())
                .setView(dialogView)
                .create();

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnDelete.setOnClickListener(v -> {
            viewModel.deleteDevice(device.getSerialNumber());
            dialog.dismiss();
        });
        dialog.show();

    }


    private void showAssignEmployeeDialog(Device device) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.assign_device_to_employee_dialog, null);

        AutoCompleteTextView employeeDropdown = dialogView.findViewById(R.id.employeeDropdown);
        MaterialButton assignButton = dialogView.findViewById(R.id.assignButton);
        MaterialButton cancelButton = dialogView.findViewById(R.id.cancelButton);

        selectedEmployeeId = null;

        employeeViewModel.getEmployees().observe(getViewLifecycleOwner(), employees -> {
            if (employees != null && !employees.isEmpty()) {
                List<String> employeeNames = employees.stream()
                        .map(Employee::getName)
                        .collect(Collectors.toList());

                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                        android.R.layout.simple_dropdown_item_1line, employeeNames);
                employeeDropdown.setAdapter(adapter);

                employeeDropdown.setOnItemClickListener((parent, view, position, id) -> {
                    selectedEmployeeId = employees.get(position).getId();
                });
            }
        });

        if (employeeViewModel.getEmployees().getValue() == null || employeeViewModel.getEmployees().getValue().isEmpty()) {
            employeeViewModel.loadEmployees();
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();


        assignButton.setOnClickListener(v -> {
            if (selectedEmployeeId != null) {
                viewModel.assignDeviceToEmployee(device.getSerialNumber(), selectedEmployeeId);
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }

    private void showUnassignEmployeeDialog(Device device) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.unassign_device_from_employee_dialog, null);

        TextView tvEmployeeName = dialogView.findViewById(R.id.tvEmployeeName);
        MaterialButton btnCancel = dialogView.findViewById(R.id.btnCancel);
        MaterialButton btnUnassign = dialogView.findViewById(R.id.btnUnassign);



        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        employeeViewModel.getEmployees().observe(getViewLifecycleOwner(), employees -> {
            if (employees != null && !employees.isEmpty()) {
                Employee employee = employees.stream()
                        .filter(e -> e.getId().equals(device.getEmployeeId()))
                        .findFirst()
                        .orElse(null);

                if (employee != null) {
                    tvEmployeeName.setText(employee.getName());
                }
            }
        });

        if (employeeViewModel.getEmployees().getValue() == null || employeeViewModel.getEmployees().getValue().isEmpty()) {
            employeeViewModel.loadEmployees();
        }

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnUnassign.setOnClickListener(v -> {
            viewModel.unassignDeviceFromEmployee(device.getSerialNumber(), device.getEmployeeId());
            dialog.dismiss();
        });
        dialog.show();


    }


    
    private void showAddDeviceDialog() {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.create_device_dialog, null);

        TextInputEditText etSerialNumber = dialogView.findViewById(R.id.etDeviceSerialNumber);
        TextInputEditText etName = dialogView.findViewById(R.id.etDeviceName);
        TextInputEditText etType = dialogView.findViewById(R.id.etDeviceType);
        AutoCompleteTextView tvCompany = dialogView.findViewById(R.id.tvDeviceCompany);
        MaterialButton submitButton = dialogView.findViewById(R.id.btnSubmit);

        selectedCompanyId = null;

        // catch the rest api get companies request data to avoid sending another request
        companiesViewModel.getCompanies().observe(getViewLifecycleOwner(), companies -> {
            if (companies != null && !companies.isEmpty()) {
                List<String> names = companies.stream().map(Company::getName).collect(Collectors.toList());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, names);
                tvCompany.setAdapter(adapter);
                tvCompany.setOnItemClickListener((parent, view, position, id) -> {
                    selectedCompanyId = companies.get(position).getId();
                });
            }
        });

        // if this fails to catch the data just make a rest api call as backup
        if (companiesViewModel.getCompanies().getValue() == null || companiesViewModel.getCompanies().getValue().isEmpty()) {
            companiesViewModel.loadCompanies();
        }

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        submitButton.setOnClickListener(v -> {
            String serialNumber = etSerialNumber.getText().toString();
            String name = etName.getText().toString();
            String type = etType.getText().toString();
            
            if (name.isEmpty() || type.isEmpty()) {
                return;
            }

            CreateDeviceRequest request = CreateDeviceRequest.builder()
                    .serialNumber(serialNumber)
                    .name(name)
                    .type(type)
                    .companyId(selectedCompanyId)
                    .build();

            viewModel.createDevice(request);
            dialog.dismiss();
        });
        dialog.show();
    }

    private void showAssignCompanyDialog(Device device) {
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.assign_device_to_company_dialog, null);

        MaterialAutoCompleteTextView companyDropdownLayout = dialogView.findViewById(R.id.companyDropdown);
        MaterialButton assignButton = dialogView.findViewById(R.id.assignButton);
        MaterialButton cancelButton = dialogView.findViewById(R.id.cancelButton);

        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        selectedCompanyId = null;

        // catch the rest api get companies request data to avoid sending another request
        companiesViewModel.getCompanies().observe(getViewLifecycleOwner(), companies -> {
            if (companies != null && !companies.isEmpty()) {
                List<String> names = companies.stream().map(Company::getName).collect(Collectors.toList());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_dropdown_item_1line, names);
                companyDropdownLayout.setAdapter(adapter);
                companyDropdownLayout.setOnItemClickListener((parent, view, position, id) -> {
                    selectedCompanyId = companies.get(position).getId();
                });
            }
        });

        // if this fails to catch the data just make a rest api call as backup
        if (companiesViewModel.getCompanies().getValue() == null || companiesViewModel.getCompanies().getValue().isEmpty()) {
            companiesViewModel.loadCompanies();
        }

        assignButton.setOnClickListener(v -> {
            if (selectedCompanyId != null) {
                viewModel.assignDeviceToCompany(device.getSerialNumber(), selectedCompanyId);
                dialog.dismiss();
            }
        });

        cancelButton.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }

    private void showUnassignCompanyDialog(Device device){
        LayoutInflater inflater = LayoutInflater.from(requireContext());
        View dialogView = inflater.inflate(R.layout.unassign_device_from_company_dialog, null);

        TextView tvCompanyName = dialogView.findViewById(R.id.tvCompanyName);
        MaterialButton btnCancel = dialogView.findViewById(R.id.btnCancel);
        MaterialButton btnUnassign = dialogView.findViewById(R.id.btnUnassign);



        AlertDialog dialog = new MaterialAlertDialogBuilder(requireContext())
                .setView(dialogView)
                .create();

        companiesViewModel.getCompanies().observe(getViewLifecycleOwner(), companies -> {
            if (companies != null && !companies.isEmpty()) {
                companies.stream()
                        .filter(e -> e.getId().equals(device.getCompanyId()))
                        .findFirst().ifPresent(company -> tvCompanyName.setText(company.getName()));
            }
        });

        if (companiesViewModel.getCompanies().getValue() == null || companiesViewModel.getCompanies().getValue().isEmpty()) {
            companiesViewModel.loadCompanies();
        }


        btnCancel.setOnClickListener(v -> dialog.dismiss());
        btnUnassign.setOnClickListener(v -> {
            viewModel.unassignDeviceFromCompany(device.getSerialNumber(), device.getCompanyId());
            dialog.dismiss();
        });
        dialog.show();

    }
}
