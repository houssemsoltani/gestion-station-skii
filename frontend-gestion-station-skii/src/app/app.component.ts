import { Component } from '@angular/core';
import { RouterModule } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { ApiService, getDefaultBackendUrl } from './services/api.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterModule, FormsModule],
  templateUrl: './app.component.html'
})
export class AppComponent {
  selectedBackend = getDefaultBackendUrl();

  constructor(private api: ApiService) {}

  onBackendChange() {
    this.api.setBackendUrl(this.selectedBackend);
  }
}
