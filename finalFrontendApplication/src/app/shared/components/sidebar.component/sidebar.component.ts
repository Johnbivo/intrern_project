import { Component, inject, output, signal } from '@angular/core';
import { LayoutService, SelectionMenuItem } from '../../../core/services/layout.service';
import { MatSidenavModule } from '@angular/material/sidenav';
import { MatListModule } from '@angular/material/list';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-sidebar',
  imports: [
    MatSidenavModule,
    MatListModule,
    MatIconModule,
    MatButtonModule,
    RouterModule
  ],
  templateUrl: './sidebar.component.html',
  styleUrl: './sidebar.component.css',
})
export class SidebarComponent {

  layoutService = inject(LayoutService);

  isOpen = signal(true);

  toggleSideBar() {
    this.isOpen.update(o => !o);
  }

  // output event to parent component to toggle sidebar
  menuToggle = output<void>();


  // emit event to parent component to toggle sidebar
  onMenuToggle() {
    this.menuToggle.emit();
  }


  // set global state for selected menu item
  selectMenuItem(menuItem: SelectionMenuItem) {
    this.layoutService.selectMenuItem(menuItem);
  }


}

