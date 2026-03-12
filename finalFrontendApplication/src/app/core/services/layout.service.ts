import { Injectable, signal } from "@angular/core";



export type SelectionMenuItem = "employees" | "devices" | "companies";




@Injectable({ providedIn: "root" })
export class LayoutService {

    selectedMenuItem = signal<SelectionMenuItem>("employees");


    selectMenuItem(menuItem: SelectionMenuItem) {
        this.selectedMenuItem.set(menuItem);
    }



}