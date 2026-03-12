import { Component, signal } from '@angular/core';
import { SidebarComponent } from "./shared/components/sidebar.component/sidebar.component";

@Component({
  selector: 'app-root',
  imports: [SidebarComponent],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('finalFrontendApplication');
}
