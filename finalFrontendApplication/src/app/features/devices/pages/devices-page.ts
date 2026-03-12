import { Component, inject, OnInit, signal, computed } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { DeviceTable } from '../components/device-table/device-table';
import { DeviceService } from '../services/device-service';
import { CompanyService } from '../../companies/services/company-service';
import { MatDialog } from '@angular/material/dialog';
import { CreateDeviceDialog } from '../components/dialogs/create-device-dialog/create-device-dialog';

@Component({
  selector: 'app-devices-page',
  standalone: true,
  imports: [MatButtonModule, MatIconModule, DeviceTable],
  template: `
    <div class="devices-page">
        
      <div class="header">
        <button mat-icon-button color="primary" (click)="goBack()">
          <mat-icon>arrow_back</mat-icon>
        </button>
        <h2>Devices for {{ companyName() }}</h2>
        <button mat-flat-button color="primary" (click)="createDevice()">
          Add Device
        </button>
      </div>
      <app-device-table [companyId]="companyId()" />
    </div>
  `,
  styles: [`
    .devices-page {
      padding: 24px;
      min-height: 100vh;
      background: linear-gradient(135deg, #f1f5f9 0%, #f8fafc 100%);
    }

    .header {
      display: flex;
      align-items: center;
      gap: 16px;
      margin-bottom: 32px;
      padding: 16px 0;
      border-bottom: 1px solid #e2e8f0;
    }

    h2 {
      margin: 0;
      color: #0f172a;
      font-size: 1.75rem;
      font-weight: 700;
      letter-spacing: -0.5px;
      flex: 1;
    }

    button[mat-icon-button] {
      color: #64748b;
      transition: color 0.2s ease;
    }

    button[mat-icon-button]:hover {
      color: #2563eb;
    }
  `]
})
export class DevicesPage implements OnInit {



  private route = inject(ActivatedRoute);
  private deviceService = inject(DeviceService);
  private companyService = inject(CompanyService);
  dialog = inject(MatDialog);

  


  companyId = signal<string>('');
  companyName = computed(() => {
    const companies = this.companyService.companies();
    const company = companies.find(c => c.id === this.companyId());
    return company?.name || 'Unknown Company';
  });

  ngOnInit() {
    const id = this.route.snapshot.paramMap.get('companyId') || '';
    this.companyId.set(id);
    this.deviceService.loadDevices(id);
    this.companyService.loadCompanies();
  }

  goBack() {
    window.history.back();
  }

  createDevice() {
    const dialogRef = this.dialog.open(CreateDeviceDialog, {
      width: '400px'
    });

    dialogRef.afterClosed().subscribe(result => {
        if (result) {
            const newDevice = { ...result, companyId: this.companyId() };
            this.deviceService.createDevice(newDevice, this.companyId());
        }
    });
  }


  
}
