import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Piste } from '../services/api.service';

@Component({
  selector: 'app-pistes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="row g-4">
      <div class="col-md-4">
        <div class="card shadow-sm">
          <div class="card-header bg-dark text-white fw-bold">Ajouter une Piste</div>
          <div class="card-body">
            <form (ngSubmit)="save()">
              <div class="mb-2">
                <label class="form-label">Nom de la piste</label>
                <input class="form-control" [(ngModel)]="form.namePiste" name="namePiste" required>
              </div>
              <div class="mb-2">
                <label class="form-label">Couleur (Difficulté)</label>
                <select class="form-select" [(ngModel)]="form.color" name="color">
                  <option value="GREEN">GREEN (Verte)</option>
                  <option value="BLUE">BLUE (Bleue)</option>
                  <option value="RED">RED (Rouge)</option>
                  <option value="BLACK">BLACK (Noire)</option>
                </select>
              </div>
              <div class="mb-2">
                <label class="form-label">Longueur (m)</label>
                <input type="number" class="form-control" [(ngModel)]="form.length" name="length" required>
              </div>
              <div class="mb-3">
                <label class="form-label">Pente (%)</label>
                <input type="number" class="form-control" [(ngModel)]="form.slope" name="slope" required>
              </div>
              <button class="btn btn-dark w-100" type="submit">Enregistrer</button>
            </form>
            <div *ngIf="message" class="alert alert-info mt-3 py-2 mb-0">{{ message }}</div>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div class="card shadow-sm">
          <div class="card-header d-flex justify-content-between align-items-center bg-white fw-bold">
            <span>Liste des Pistes ({{ pistes.length }})</span>
            <button class="btn btn-sm btn-outline-secondary" (click)="load()">Actualiser</button>
          </div>
          <div class="table-responsive">
            <table class="table table-hover mb-0">
              <thead class="table-light">
                <tr>
                  <th>ID</th>
                  <th>Nom</th>
                  <th>Couleur</th>
                  <th>Longueur</th>
                  <th>Pente</th>
                  <th>Action</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let p of pistes">
                  <td>{{ p.numPiste }}</td>
                  <td>{{ p.namePiste }}</td>
                  <td>
                    <span class="badge"
                          [class.bg-success]="p.color === 'GREEN'"
                          [class.bg-primary]="p.color === 'BLUE'"
                          [class.bg-danger]="p.color === 'RED'"
                          [class.bg-dark]="p.color === 'BLACK'">
                      {{ p.color }}
                    </span>
                  </td>
                  <td>{{ p.length }} m</td>
                  <td>{{ p.slope }} %</td>
                  <td>
                    <button class="btn btn-sm btn-outline-danger" (click)="remove(p.numPiste!)">Supprimer</button>
                  </td>
                </tr>
                <tr *ngIf="pistes.length === 0">
                  <td colspan="6" class="text-center text-muted py-4">Aucune piste enregistrée.</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  `
})
export class PistesComponent implements OnInit {
  pistes: Piste[] = [];
  form: Piste = { namePiste: '', color: 'BLUE', length: 1200, slope: 20 };
  message = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.api.getPistes().subscribe({
      next: (data) => (this.pistes = data),
      error: () => (this.message = 'Erreur de connexion au backend.')
    });
  }

  save() {
    this.api.addPiste(this.form).subscribe({
      next: () => {
        this.message = 'Piste ajoutée !';
        this.form = { namePiste: '', color: 'BLUE', length: 1200, slope: 20 };
        this.load();
      },
      error: () => (this.message = "Erreur lors de l'ajout.")
    });
  }

  remove(id: number) {
    this.api.deletePiste(id).subscribe({
      next: () => this.load(),
      error: () => (this.message = 'Erreur lors de la suppression.')
    });
  }
}
