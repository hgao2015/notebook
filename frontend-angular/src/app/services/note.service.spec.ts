import { TestBed, inject } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { NoteService } from './note.service';
import { Note } from '../models/index';

describe('NoteService', () => {
  beforeEach(() => {
    TestBed.configureTestingModule({
	  imports: [HttpClientTestingModule],	
      providers: [NoteService]
    });
  });

  
  it('should be created', inject([NoteService], (service: NoteService) => {
    expect(service).toBeTruthy();
  }));
  
  it(
    'should retrieve note by id correctly',
    inject(
      [NoteService, HttpTestingController],
      (noteService: NoteService, backend: HttpTestingController) => {
        noteService.findById(9001)
			.first()
			.subscribe(
				(data: any) => {
					expect(data).toBeDefined();					
					expect(data.id).toBeDefined();
					expect(data.id).toBe(9001);
					expect(data.title).toBeDefined();
					expect(data.title).toBe('Title-1');					
					expect(data.content).toBeDefined();
					expect(data.content).toBe('Content-11111111111111111');					

			},
          (error: any) => {}
        );

        backend
          .expectOne({
            url: 'http://localhost:8080/notes/id/9001'
          })
          .flush({
			 'id': 9001, 'title': 'Title-1', 'content': 'Content-11111111111111111'
          });
      }
    )
  );
  
  it(
    'should retrieve note by id correctly, not return other id',
    inject(
      [NoteService, HttpTestingController],
      (noteService: NoteService, backend: HttpTestingController) => {
        noteService.findById(9001)
			.first()
			.subscribe(
				(data: any) => {
					expect(data).toBeDefined();					
					expect(data.id).toBeDefined();
					expect(data.id).not.toBe(9002);
					expect(data.title).toBeDefined();
					expect(data.title).toBe('Title-1');					
					expect(data.content).toBeDefined();
					expect(data.content).toBe('Content-11111111111111111');					

			},
          (error: any) => {}
        );

        backend
          .expectOne({
            url: 'http://localhost:8080/notes/id/9001'
          })
          .flush({
			 'id': 9001, 'title': 'Title-1', 'content': 'Content-11111111111111111'
          });
      }
    )
  );  
  
  it(
    'should return one note by title correctly',
    inject(
      [NoteService, HttpTestingController],
      (noteService: NoteService, backend: HttpTestingController) => {
        noteService.findByTitle("Title")
			.first()
			.subscribe(
				(data: any) => {
					expect(data).toBeDefined();					
					expect(data.length).toBe(1);										
					expect(data[0].id).toBeDefined();
					expect(data[0].id).toBe(9001);
					expect(data[0].title).toBeDefined();
					expect(data[0].title).toBe('Title-1');					
					expect(data[0].content).toBeDefined();
					expect(data[0].content).toBe('Content-11111111111111111');					
			},
          (error: any) => {}
        );

        backend
          .expectOne({
            url: 'http://localhost:8080/notes/title/Title'
          })
          .flush(
			[{'id': 9001, 'title': 'Title-1', 'content': 'Content-11111111111111111'}]
          );
      }
    )
  );
  
  it(
    'should return two notes by title correctly',
    inject(
      [NoteService, HttpTestingController],
      (noteService: NoteService, backend: HttpTestingController) => {
        noteService.findByTitle("Title")
			.first()
			.subscribe(
				(data: any) => {
					expect(data).toBeDefined();					
					expect(data.length).toBe(2);										
					expect(data[0].id).toBeDefined();
					expect(data[0].id).toBe(9001);
					expect(data[0].title).toBeDefined();
					expect(data[0].title).toBe('Title-1');					
					expect(data[0].content).toBeDefined();
					expect(data[0].content).toBe('Content-11111111111111111');					
					expect(data[1].id).toBeDefined();
					expect(data[1].id).toBe(9002);
					expect(data[1].title).toBeDefined();
					expect(data[1].title).toBe('Title-2');					
					expect(data[1].content).toBeDefined();
					expect(data[1].content).toBe('Content-22222222222222222');					
			},
          (error: any) => {}
        );

        backend
          .expectOne({
            url: 'http://localhost:8080/notes/title/Title'
          })
          .flush(
			[{'id': 9001, 'title': 'Title-1', 'content': 'Content-11111111111111111'},
			 {'id': 9002, 'title': 'Title-2', 'content': 'Content-22222222222222222'}
			]
          );
      }
    )
  );
  
  //
  it(
    'should create a new note',
    inject(
      [NoteService, HttpTestingController],
      (noteService: NoteService, backend: HttpTestingController) => {
        noteService.add('Title-10', 'Content-100000000000')
			.first()
			.subscribe(
			  (data: any) => {
				 expect(JSON.stringify(data)).toBe(JSON.stringify({}));
			  },
			  (error: any) => {}
			);

        backend
          .expectOne({
            url: 'http://localhost:8080/notes/create'
          })
          .flush({});
      }
    )
  );
  
});
