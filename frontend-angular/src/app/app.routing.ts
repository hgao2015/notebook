import { Routes, RouterModule } from '@angular/router';

import { HomeComponent } from './home/home.component';
import { LoginComponent } from './login/login.component';
import { NotebookComponent } from './notebook/notebook.component';

import { AuthGuard } from './guards/index';

const appRoutes: Routes = [
    { path: 'login', component: LoginComponent },
	{ path: 'add', component: NotebookComponent },
	{ path: 'search', component: NotebookComponent },
    { path: '', component: HomeComponent, canActivate: [AuthGuard] },
    { path: '**', redirectTo: '' }
];

export const routing = RouterModule.forRoot(appRoutes);