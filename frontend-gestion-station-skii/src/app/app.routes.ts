import { Routes } from '@angular/router';
import { SkiersComponent } from './components/skiers.component';
import { CoursesComponent } from './components/courses.component';
import { PistesComponent } from './components/pistes.component';
import { InstructorsComponent } from './components/instructors.component';

export const routes: Routes = [
  { path: '', redirectTo: 'skiers', pathMatch: 'full' },
  { path: 'skiers', component: SkiersComponent },
  { path: 'courses', component: CoursesComponent },
  { path: 'pistes', component: PistesComponent },
  { path: 'instructors', component: InstructorsComponent }
];
