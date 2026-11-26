import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Instructor } from '../services/api.service';

@Component({
  selector: 'app-instructors',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="row g-4">
      <div class="col-md-4">
        <div class="card shadow-sm">
          <div class="card-header bg-warning fw-bold">Ajouter un Moniteur</div>
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
              <div class="mb-3">
                <label class="form-label">Date d'embauche</label>
                <input type="date" class="form-control" [(ngModel)]="form.dateOfHire" name="dateOfHire" required>
              </div>
              <button class="btn btn-warning w-100 fw-bold" type="submit">Ajouter</button>
            </form>
            <div *ngIf="message" class="alert alert-info mt-3 py-2 mb-0">{{ message }}</div>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div class="card shadow-sm">
          <div class="card-header d-flex justify-content-between align-items-center bg-white fw-bold">
            <span>Liste des Moniteurs ({{ instructors.length }})</span>
            <button class="btn btn-sm btn-outline-secondary" (click)="load()">Actualiser</button>
          </div>
          <div class="table-responsive">
            <table class="table table-hover mb-0">
              <thead class="table-light">
                <tr>
                  <th>ID</th>
                  <th>Nom complet</th>
                  <th>Date d'embauche</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let i of instructors">
                  <td>{{ i.numInstructor }}</td>
                  <td>{{ i.firstName }} {{ i.lastName }}</td>
                  <td>{{ i.dateOfHire }}</td>
                </tr>
                <tr *ngIf="instructors.length === 0">
                  <td colspan="3" class="text-center text-muted py-4">Aucun moniteur enregistré.</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  `
})
export class InstructorsComponent implements OnInit {
  instructors: Instructor[] = [];
  form: Instructor = { firstName: '', lastName: '', dateOfHire: '2026-01-01' };
  message = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.api.getInstructors().subscribe({
      next: (data) => (this.instructors = data),
      error: () => (this.message = 'Erreur de connexion au backend.')
    });
  }

  save() {
    this.api.addInstructor(this.form).subscribe({
      next: () => {
        this.message = 'Moniteur ajouté !';
        this.form = { firstName: '', lastName: '', dateOfHire: '2026-01-01' };
        this.load();
      },
      error: () => (this.message = "Erreur lors de l'ajout.")
    });
  }
}
