import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { FormsModule }   from '@angular/forms';
import { RouterModule, ActivatedRoute } from '@angular/router'; 
import { RouterTestingModule } from "@angular/router/testing"; 
import { HttpClientTestingModule } from '@angular/common/http/testing'; 
import {Observable} from 'rxjs/Rx';

import { NotebookComponent } from './notebook.component';

describe('NotebookComponent', () => {
  let component: NotebookComponent;
  let fixture: ComponentFixture<NotebookComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
		imports: [ RouterModule, HttpClientTestingModule, RouterTestingModule, FormsModule ],
      declarations: [ NotebookComponent ],
	  providers: [
		 { provide: ActivatedRoute, 
		   useValue: {
			   data: Observable.of({x: 100}), 
			   snapshot: 
			   {url: [{path: 'special'}]} 
			}
		 }
		]
    })
    .compileComponents();
  }));

  beforeEach(() => {
	TestBed.configureTestingModule({
		declarations: [NotebookComponent]
    });  
    fixture = TestBed.createComponent(NotebookComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should be created', () => {
	  expect(component).toBeTruthy();
	  expect(component.message).toBe('');
	  expect(component.notes.length).toBe(0);
	  expect(component.searchOption).toBe('searchById');
  });
  
});
