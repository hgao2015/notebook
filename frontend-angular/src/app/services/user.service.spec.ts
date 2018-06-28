import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { UserService } from './user.service';
import { User } from '../models/index';


describe('UserService', () => {
  
  beforeEach(() => {
    TestBed.configureTestingModule({
	  imports: [HttpClientTestingModule],	
      providers: [UserService]
    });
  });

  
  it('should be created', inject([UserService], (service: UserService) => {
    expect(service).toBeTruthy();
  }));
  
  it(
    'should retrieve current user(me) correctly',
    inject(
      [UserService, HttpTestingController],
      (userService: UserService, backend: HttpTestingController) => {
        userService.getMyUserAccount()
			.first()
			.subscribe(
				(data: any) => {
					expect(data).toBeDefined();
					expect(data.username).toBeDefined();
					expect(data.email).toBeDefined();
					expect(data.username).toBe('testUser');
					expect(data.email).toBe('testUser@email.com');	
			},
          (error: any) => {}
        );

        backend
          .expectOne({
            url: 'http://localhost:8080/users/me'
          })
          .flush({
			 'username': 'testUser', 'email': 'testUser@email.com'
          });
      }
    )
  );
 
  it(
    'should retrieve current user(me) correctly, not other user',
    inject(
      [UserService, HttpTestingController],
      (userService: UserService, backend: HttpTestingController) => {
        userService.getMyUserAccount()
			//.map(user => user)
			.first()
			.subscribe(
				(data: any) => {
					expect(data).toBeDefined();
					expect(data.username).toBeDefined();
					expect(data.email).toBeDefined();
					expect(data.username).not.toBe('testUser111');
					expect(data.email).toBe('testUser@email.com');	
			},
          (error: any) => {}
        );

        backend
          .expectOne({
            url: 'http://localhost:8080/users/me'
          })
          .flush({
			 'username': 'testUser', 'email': 'testUser@email.com'
          });
      }
    )
  );
});
