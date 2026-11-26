import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ApiService, Course } from '../services/api.service';

@Component({
  selector: 'app-courses',
  standalone: true,
  imports: [CommonModule, FormsModule],
  template: `
    <div class="row g-4">
      <div class="col-md-4">
        <div class="card shadow-sm">
          <div class="card-header bg-success text-white fw-bold">Ajouter un Cours</div>
          <div class="card-body">
            <form (ngSubmit)="save()">
              <div class="mb-2">
                <label class="form-label">Niveau</label>
                <input type="number" class="form-control" [(ngModel)]="form.level" name="level" required>
              </div>
              <div class="mb-2">
                <label class="form-label">Type de cours</label>
                <select class="form-select" [(ngModel)]="form.typeCourse" name="typeCourse">
                  <option value="COLLECTIVE_CHILDREN">COLLECTIVE_CHILDREN</option>
                  <option value="COLLECTIVE_ADULT">COLLECTIVE_ADULT</option>
                  <option value="INDIVIDUAL">INDIVIDUAL</option>
                </select>
              </div>
              <div class="mb-2">
                <label class="form-label">Support</label>
                <select class="form-select" [(ngModel)]="form.support" name="support">
                  <option value="SKI">SKI</option>
                  <option value="SNOWBOARD">SNOWBOARD</option>
                </select>
              </div>
              <div class="mb-2">
                <label class="form-label">Prix</label>
                <input type="number" class="form-control" [(ngModel)]="form.price" name="price" required>
              </div>
              <div class="mb-3">
                <label class="form-label">Créneau (heure)</label>
                <input type="number" class="form-control" [(ngModel)]="form.timeSlot" name="timeSlot" required>
              </div>
              <button class="btn btn-success w-100" type="submit">Ajouter</button>
            </form>
            <div *ngIf="message" class="alert alert-info mt-3 py-2 mb-0">{{ message }}</div>
          </div>
        </div>
      </div>
      <div class="col-md-8">
        <div class="card shadow-sm">
          <div class="card-header d-flex justify-content-between align-items-center bg-white fw-bold">
            <span>Liste des Cours ({{ courses.length }})</span>
            <button class="btn btn-sm btn-outline-secondary" (click)="load()">Actualiser</button>
          </div>
          <div class="table-responsive">
            <table class="table table-hover mb-0">
              <thead class="table-light">
                <tr>
                  <th>ID</th>
                  <th>Niveau</th>
                  <th>Type</th>
                  <th>Support</th>
                  <th>Prix</th>
                  <th>Créneau</th>
                </tr>
              </thead>
              <tbody>
                <tr *ngFor="let c of courses">
                  <td>{{ c.numCourse }}</td>
                  <td>{{ c.level }}</td>
                  <td><span class="badge bg-info text-dark">{{ c.typeCourse }}</span></td>
                  <td>{{ c.support }}</td>
                  <td>{{ c.price }} DT</td>
                  <td>{{ c.timeSlot }}h</td>
                </tr>
                <tr *ngIf="courses.length === 0">
                  <td colspan="6" class="text-center text-muted py-4">Aucun cours disponible.</td>
                </tr>
              </tbody>
            </table>
          </div>
        </div>
      </div>
    </div>
  `
})
export class CoursesComponent implements OnInit {
  courses: Course[] = [];
  form: Course = { level: 1, typeCourse: 'INDIVIDUAL', support: 'SKI', price: 120, timeSlot: 2 };
  message = '';

  constructor(private api: ApiService) {}

  ngOnInit() {
    this.load();
  }

  load() {
    this.api.getCourses().subscribe({
      next: (data) => (this.courses = data),
      error: () => (this.message = 'Erreur de connexion au backend.')
    });
  }

  save() {
    this.api.addCourse(this.form).subscribe({
      next: () => {
        this.message = 'Cours ajouté avec succès !';
        this.load();
      },
      error: () => (this.message = "Erreur lors de l'ajout.")
    });
  }
}
