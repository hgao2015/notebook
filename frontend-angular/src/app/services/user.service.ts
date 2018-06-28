import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { CONFIG } from '../config/app.config';
import { User } from '../models/index';

@Injectable({
  providedIn: 'root'
})
export class UserService {
	private baseUrl: string = CONFIG.apiUrl;
	
    constructor(private http: HttpClient) { }

    getMyUserAccount() { 
		let url = this.baseUrl + '/users/me';
        return this.http.get<User>(url);
    }
}