import { Component, OnInit } from '@angular/core';
import 'rxjs/add/operator/first';
import { UserService } from '../services/index';

import { Router } from '@angular/router';


@Component({
  moduleId: module.id, //?
  selector: 'app-home',
  templateUrl: './home.component.html'
})
export class HomeComponent implements OnInit {
    constructor(private userService: UserService) { }

    ngOnInit() {        
        this.userService.getMyUserAccount()
            .first()
            .subscribe(user => {                
                if (user) {
					localStorage.setItem('myEmail', user.email);
					localStorage.setItem('myUserName', user.username);
                }
            });
    }
	
	get userEmail(): any {
		return localStorage.getItem('myEmail');
	}

	
	get userName(): any {
		return localStorage.getItem('myUserName');
	}

}
