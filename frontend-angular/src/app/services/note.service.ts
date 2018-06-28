import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
import 'rxjs/add/operator/map';
import { CONFIG } from '../config/app.config';

import { Note } from '../models/index';

@Injectable({
  providedIn: 'root'
})
export class NoteService {
	private baseUrl: string = CONFIG.apiUrl;

    constructor(private http: HttpClient) { }

    add(title: string, content: string) {
		return this.http.post<any>(this.baseUrl + '/notes/create', { title: title, content:content });
    }
	
	findById(id: number) {
		let url = this.baseUrl + '/notes/id/' + id;
        return this.http.get<Note>(url);
	}
	
	findByTitle(title: string) {
		let url = this.baseUrl + '/notes/title/' + title;
		return this.http.get<Note[]>(url);
	}
}