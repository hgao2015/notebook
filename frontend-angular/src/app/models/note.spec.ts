import { Note } from './note';

describe('Note', () => {
  
  let note = null;

  /**
  * beforeEach will create fresh copy of User object
  * before each unit test. We also can define User object once 
  * and run unit tests against it, but in general it's a good practice
  * to work on a new instance of an object to avoid modifications from
  * the previous test run.
  */
  beforeEach(() => {
    note = new Note();
	note.id = 1;
	note.title = 'Title A';
	note.content = 'Content A';
  });

  it('should be initialized', () => {
    expect(note).toBeTruthy();
  });
  
  it('id should be 1', () => {
    expect(note.id).toEqual(1);
  });
  
  it(`title should be 'Title A'`, () => {
    expect(note.title).toEqual('Title A');
  });
  
  it(`content should be 'Content A'`, () => {
    expect(note.content).toEqual('Content A');
  });
});