import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Subscription {
  numSub?: number;
  startDate: string;
  endDate?: string;
  price: number;
  typeSub: 'ANNUAL' | 'MONTHLY' | 'SEMESTRIEL';
}

export interface Skier {
  numSkier?: number;
  firstName: string;
  lastName: string;
  dateOfBirth: string;
  city: string;
  subscription?: Subscription;
}

export interface Course {
  numCourse?: number;
  level: number;
  typeCourse: 'COLLECTIVE_CHILDREN' | 'COLLECTIVE_ADULT' | 'INDIVIDUAL';
  support: 'SKI' | 'SNOWBOARD';
  price: number;
  timeSlot: number;
}

export interface Piste {
  numPiste?: number;
  namePiste: string;
  color: 'GREEN' | 'BLUE' | 'RED' | 'BLACK';
  length: number;
  slope: number;
}

export interface Instructor {
  numInstructor?: number;
  firstName: string;
  lastName: string;
  dateOfHire: string;
}

export function getDefaultBackendUrl(): string {
  if (typeof window !== 'undefined') {
    const host = window.location.hostname;
    if (host === 'localhost' || host === '127.0.0.1') {
      return 'http://localhost:8087/stationSki';
    }
  }
  return 'http://54.242.143.76:8087/stationSki';
}

@Injectable({ providedIn: 'root' })
export class ApiService {
  baseUrl = getDefaultBackendUrl();

  constructor(private http: HttpClient) {}

  setBackendUrl(url: string) {
    this.baseUrl = url;
  }

  // --- Skieurs ---
  getSkiers(): Observable<Skier[]> {
    return this.http.get<Skier[]>(`${this.baseUrl}/skier/all`);
  }
  addSkier(skier: Skier): Observable<Skier> {
    return this.http.post<Skier>(`${this.baseUrl}/skier/add`, skier);
  }
  deleteSkier(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/skier/delete/${id}`);
  }

  // --- Cours ---
  getCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.baseUrl}/course/all`);
  }
  addCourse(course: Course): Observable<Course> {
    return this.http.post<Course>(`${this.baseUrl}/course/add`, course);
  }

  // --- Pistes ---
  getPistes(): Observable<Piste[]> {
    return this.http.get<Piste[]>(`${this.baseUrl}/piste/all`);
  }
  addPiste(piste: Piste): Observable<Piste> {
    return this.http.post<Piste>(`${this.baseUrl}/piste/add`, piste);
  }
  deletePiste(id: number): Observable<void> {
    return this.http.delete<void>(`${this.baseUrl}/piste/delete/${id}`);
  }

  // --- Moniteurs ---
  getInstructors(): Observable<Instructor[]> {
    return this.http.get<Instructor[]>(`${this.baseUrl}/instructor/all`);
  }
  addInstructor(instructor: Instructor): Observable<Instructor> {
    return this.http.post<Instructor>(`${this.baseUrl}/instructor/add`, instructor);
  }
  addInstructorAndAssign(instructor: Instructor, numCourse: number): Observable<Instructor> {
    return this.http.put<Instructor>(`${this.baseUrl}/instructor/addAndAssignToCourse/${numCourse}`, instructor);
  }
}
