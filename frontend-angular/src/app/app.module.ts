/// <reference path="../../node_modules/@types/node/index.d.ts" />

import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule }    from '@angular/forms';

import { RouterModule } from '@angular/router';
import { HttpClientModule, HTTP_INTERCEPTORS } from '@angular/common/http';

import { AppComponent } from './app.component';
import { routing }        from './app.routing';
import { AuthGuard } from './guards/index';
import { JwtInterceptor } from './helpers/index';

import { AuthenticationService, UserService, NoteService } from './services/index';

import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { NotebookComponent } from './notebook/notebook.component';

@NgModule({
  declarations: [
    AppComponent,
    HomeComponent,
    LoginComponent,
    NotebookComponent
  ],
  imports: [
    BrowserModule,
	FormsModule,
    HttpClientModule,
	RouterModule,
    routing
  ],
  providers: [
		AuthGuard,
        AuthenticationService,
        UserService,
		NoteService,
		HttpClientModule,
        {
            provide: HTTP_INTERCEPTORS,
            useClass: JwtInterceptor,
            multi: true
        },  
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
