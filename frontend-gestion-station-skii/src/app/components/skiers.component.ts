import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Skier } from '../services/api.service';

@Component({
  selector: 'app-skiers',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="row g-4">
      <div class="col-md-4">
        <div class="card shadow-sm">
          <div class="card-header bg-primary text-white fw-bold">Ajouter un Skieur</div>
          <div class="card-body">
            <form (ngSubmit)="save()">
              <div class="mb-2">
                <label class="form-label">Prénom</label>
                <input class="form-control" [(ngModel)]="form.firstName" name="firstName" required>
              </div>
              <div class="mb-2">
                <label class="form-label">Nom</label>
                <input class="form-control" [(ngModel)]="form.lastName" name="lastName" required>
              </div>
              <div class="mb-2">
                <label class="form-label">Date de naissance</label>
                <input type="date" class="form-control" [(ngModel)]="form.dateOfBirth" name="dateOfBirth" required>
              </div>
              <div class="mb-2">
                <label class="form-label">Ville</label>
                <input class="form-control" [(ngModel)]="form.city" name="city" required>
              </div>
              <hr class="my-2">
              <div class="fw-semibold small text-muted mb-2">Abonnement (Subscription)</div>
              <div class="mb-2">
                <label class="form-label">Type d'abonnement</label>
                <select class="form-select" [(ngModel)]="form.subscription!.typeSub" name="typeSub">
                  <option value="ANNUAL">ANNUAL</option>
                  <option value="SEMESTRIEL">SEMESTRIEL</option>
                  <option value="MONTHLY">MONTHLY</option>
                </select>
              </div>
              <div class="mb-2">
                <label class="form-label">Date début</label>
                <input type="date" class="form-control" [(ngModel)]="form.subscription!.startDate" name="startDate" required>
              </div>
              <div class="mb-3">
                <label class="form-label">Prix abonnement</label>
                <input type="number" class="form-control" [(ngModel)]="form.subscription!.price" name="price" required>
              </div>
              <button class="btn btn-primary w-100" type="submit">Enregistrer</button>
            </form>
            <div *ngIf="message" class="alert alert-info mt-3 py-2 mb-0">{{ message }}</div>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div class="card shadow-sm">
          <div class="card-header d-flex justify-content-between align-items-center bg-white fw-bold">
            <span>Liste des Skieurs ({{ skiers.length }})</span>
            <button class="btn btn-sm btn-outline-secondary" (click)="load()">Actualiser</button>
          </div>
          <div class="table-responsive">
            <table class="table table-hover mb-0">
              <thead class="table-light">
                <tr>
                  <th>ID</th>
                  <th>Nom complet</th>
                  <th>Date naissance</th>
                  <th>Ville</th>
                  <th>Abonnement</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let s of skiers">
                  <td>{{ s.numSkier }}</td>
                  <td>{{ s.firstName }} {{ s.lastName }}</td>
                  <td>{{ s.dateOfBirth }}</td>
                  <td>{{ s.city }}</td>
                  <td>
                    <span *ngIf="s.subscription" class="badge bg-info text-dark">
                      {{ s.subscription.typeSub }}
                    </span>
                  </td>
                  <td>
                    <button class="btn btn-sm btn-outline-danger" (click)="remove(s.numSkier!)">Supprimer</button>
                  </td>
                </tr>
                <tr *ngIf="skiers.length === 0">
                  <td colspan="6" class="text-center text-muted py-4">Aucun skieur trouvé.</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  `
})
export class SkiersComponent implements OnInit {
  skiers: Skier[] = [];
  form: Skier = this.createEmptyForm();
  message = '';

  constructor(private api: ApiService) {}

  createEmptyForm(): Skier {
    return {
      firstName: '',
      lastName: '',
      dateOfBirth: '2000-01-01',
      city: '',
      subscription: {
        typeSub: 'ANNUAL',
        startDate: '2026-01-01',
        price: 300
      }
    };
  }

  ngOnInit() {
    this.load();
  }

  load() {
    this.api.getSkiers().subscribe({
      next: (data) => (this.skiers = data),
      error: () => (this.message = 'Erreur de connexion au backend.')
    });
  }

  save() {
    this.api.addSkier(this.form).subscribe({
      next: () => {
        this.message = 'Skieur ajouté avec succès !';
        this.form = this.createEmptyForm();
        this.load();
      },
      error: () => (this.message = "Erreur lors de l'ajout.")
    });
  }

  remove(id: number) {
    this.api.deleteSkier(id).subscribe({
      next: () => this.load(),
      error: () => (this.message = 'Erreur lors de la suppression.')
    });
  }
}
