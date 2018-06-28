import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';
//import 'rxjs/add/operator/map';
import { map } from 'rxjs/operators';

import { CONFIG } from '../config/app.config';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {
	private baseUrl: string = CONFIG.apiUrl;
	
    constructor(private http: HttpClient) { }

    login(username: string, password: string) {
		return this.http.post<any>(this.baseUrl + '/users/signin', {username: username, password: password })
            .map(user => {
                if (user && user.token) {
                    localStorage.setItem('currentUser', JSON.stringify(user));
                }
				return user; //
            });
    }

    logout() {
        localStorage.removeItem('currentUser');
		localStorage.removeItem('myEmail');
		localStorage.removeItem('myUserName');
    }
}