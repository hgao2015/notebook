import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule,  HttpTestingController} from '@angular/common/http/testing';
import { AuthenticationService } from './authentication.service';

import 'rxjs/add/operator/first';

describe('AuthenticationService', () => {
	
  beforeEach(() => {
    TestBed.configureTestingModule({
	  imports: [HttpClientTestingModule],	
      providers: [AuthenticationService]
    });
  });

  
  it('should be created', inject([AuthenticationService], (service: AuthenticationService) => {
    expect(service).toBeTruthy();
  }));
  
  
  it(
    'should perform login and return token correctly',
    inject(
      [AuthenticationService, HttpTestingController],
      (authService: AuthenticationService, backend: HttpTestingController) => {
        const username = 'test';
		const password = 'password';
        authService.login(username, password)
			.first()
			.subscribe(
          (data: any) => {
			 expect(data.token).toBeDefined();
			 expect(data.token).toBe('xxx.yyy.zzz');
          },
          (error: any) => {}
        );

        backend
          .expectOne({
            url: 'http://localhost:8080/users/signin'
          })
          .flush({
			 token: 'xxx.yyy.zzz'
          });
      }
    )
  );
 
  it(
    'should perform login correctly',
    inject(
      [AuthenticationService, HttpTestingController],
      (authService: AuthenticationService, backend: HttpTestingController) => {
        const username = 'test';
		const password = 'password';
        authService.login(username, password)
			.first()
			.subscribe(
          (data: any) => {
			 expect(data.token).not.toBe('xxx.yyy.zzz4');
          },
          (error: any) => {}
        );

        backend
          .expectOne({
            url: 'http://localhost:8080/users/signin'
          })
          .flush({
			 token: 'xxx.yyy.zzz'
          });
      }
    )
  );
});
