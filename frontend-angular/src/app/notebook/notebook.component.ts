import { Component, OnInit } from '@angular/core';
import { Location } from '@angular/common';
import { NgForm } from '@angular/forms';
import { Router, ActivatedRoute, Params } from '@angular/router';
import 'rxjs/add/operator/first';

import { NoteService } from '../services/index';
import { Note } from '../models/index';

@Component({
  moduleId: module.id,
  selector: 'app-notebook',
  templateUrl: './notebook.component.html'
})
export class NotebookComponent implements OnInit {

    model: any = {};
    loading = false;
	addAction: boolean = false; 
	searchAction: boolean = false;
	searchOption: string = "searchById";
	notes: Note[] = [];
	message = '';
	
    constructor(
        private route: ActivatedRoute,
        private router: Router,
		private location: Location,
        private noteService: NoteService) { }

    ngOnInit() {		
		let currentUrl = this.route.snapshot.url[0].path;
		if (currentUrl == 'search') {
			this.searchAction = true;
		} else {
			this.addAction = true;
		}	
    }

	add(f:NgForm) { 	
        this.loading = true;
		this.message = '';
		this.noteService.add(this.model.title, this.model.content)
			  .first()
			  .subscribe(
				  data => {
					  //
				  },
				  error => {
					this.message = error.error.message;
				  },
				  () => {
					  this.message = 'Note added';
					  f.resetForm();
				  }
			  );		
		this.loading = false;		
    }
	
	search(f:NgForm) { 
		this.loading = true;
		this.notes = [];
		this.message = '';
		
		if (this.searchOption == 'searchById') {
			this.noteService.findById(this.model.id)
				.first()
				.subscribe(note => {										
					this.notes[0] = note;
				},
				error => {					
					this.message = error.error.message;
				}
			);	
		}
		
		if (this.searchOption == 'searchByTitle') {
				f.form.controls.id.reset();
				this.noteService.findByTitle(this.model.searchtitle)
					.first()
					.subscribe(notes => {
						this.notes = notes;
					},
					error => {
						this.message = error.error.message;
					}
				);
		}
		this.loading = false;
	}
	
	backClicked() {
		this.location.back();
	}
	
	titleOptionSelected(f:NgForm) {
		this.message='';
		this.notes = [];
		f.form.controls.searchtitle.enable();
		f.form.controls.id.reset();
		f.form.controls.id.disable();
	}
    
	idOptionSelected(f:NgForm) {
		this.message= '';
		this.notes = [];
		f.form.controls.id.enable();
		f.form.controls.searchtitle.reset();
		f.form.controls.searchtitle.disable();
	}
}
