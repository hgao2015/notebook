import { User } from './user';

describe('User', () => {
  
  let user = null;

  /**
  * beforeEach will create fresh copy of User object
  * before each unit test. We also can define User object once 
  * and run unit tests against it, but in general it's a good practice
  * to work on a new instance of an object to avoid modifications from
  * the previous test run.
  */
  beforeEach(() => {
    user = new User();
	user.id = 1;
	user.username = 'admin';
	user.email = 'admin@email.com';
  });

  it('should be initialized', () => {
    expect(user).toBeTruthy();
  });
  
  it('id should be 1', () => {
    expect(user.id).toEqual(1);
  });
  
  it(`Username should be 'test'`, () => {
    expect(user.username).toEqual('admin');
  });
  
  it(`email should be 'admin@email.com'`, () => {
    expect(user.email).toEqual('admin@email.com');
  });
});