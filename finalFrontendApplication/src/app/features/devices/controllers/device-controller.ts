import { Injectable } from "@angular/core";
import { Observable } from "rxjs";
import { HttpClient } from "@angular/common/http";
import { Device } from "../entities/Device";



@Injectable({
    providedIn: 'root'
})
export class DeviceController {

    private url = "http://localhost:8080/devices";

    constructor(private http: HttpClient) {}



    getDevices(){
        return this.http.get<Device[]>(this.url);
    }

    getDeviceBySerialNumber(serialNumber: string): Observable<Device> {
        return this.http.get<Device>(`${this.url}/${serialNumber}`);
    }

    createDevice(device: Device): Observable<Device> {
        return this.http.post<Device>(this.url, device);
    }
    

    updateDevice(serialNumber: string, device: Device): Observable<Device> {
        return this.http.put<Device>(`${this.url}/${serialNumber}`, device);
    }

    deleteDevice(serialNumber: string): Observable<void> {
        return this.http.delete<void>(`${this.url}/${serialNumber}`);
    }



    getAllCompanyDevices(companyId: string): Observable<Device[]> {
        return this.http.get<Device[]>(`${this.url}/company/${companyId}`);

    }

    getAllEmployeeDevices(employeeId: string): Observable<Device[]> {
        return this.http.get<Device[]>(`${this.url}/employee/${employeeId}`);
    }


    assignDeviceToCompany(serialNumber: string, companyId: string): Observable<Device> {
        return this.http.put<Device>(`${this.url}/${serialNumber}/assign/company/${companyId}`, {});

    }

    assignDeviceToEmployee(serialNumber: string, employeeId: string): Observable<void> {
        return this.http.post<void>(`${this.url}/assign/${serialNumber}/employee/${employeeId}`, {});

    }
    unassignDeviceFromEmployee(serialNumber: string, employeeId: string): Observable<void> {
        return this.http.post<void>(`${this.url}/unassign/${serialNumber}/employee/${employeeId}`, {});
    }

    unassignDeviceFromCompany(serialNumber: string, companyId: string): Observable<Device> {
        return this.http.put<Device>(`${this.url}/unassign/${serialNumber}/company/${companyId}`, {});
    }

    getAllDevicesAssignedToEmployee(employeeId: string): Observable<Device[]> {
        return this.http.get<Device[]>(`${this.url}/employee/${employeeId}`);

    }

    getAllDevicesAssignedToCompany(companyId: string): Observable<Device[]> {
        return this.http.get<Device[]>(`${this.url}/company/${companyId}`);
    }

    unassignAllDevicesFromEmployee(employeeId: string): Observable<void> {
        return this.http.put<void>(`${this.url}/employee/${employeeId}/unassignAllDevices`, {});
    }

    unassignAllDevicesFromCompany(companyId: string): Observable<void> {
        return this.http.put<void>(`${this.url}/company/${companyId}/unassignAllDevices`, {});
    }

}