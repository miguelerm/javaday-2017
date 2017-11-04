import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { DatePipe } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { RouterModule, Routes } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { AppComponent } from './app.component';
import { EmployeesService } from './employees.service';
import { EditComponent } from './edit/edit.component';
import { ListComponent } from './list/list.component';
import { DetailsComponent } from './details/details.component';
import { PageNotFoundComponent } from './page-not-found/page-not-found.component';
import { DeleteComponent } from './delete/delete.component';

import { NgDatepickerModule } from 'ng2-datepicker';

const appRoutes: Routes = [
  { path: 'list', component: ListComponent },
  { path: 'details/:id', component: DetailsComponent },
  { path: 'edit/:id', component: EditComponent },
  { path: 'delete/:id', component: DeleteComponent },
  { path: '', redirectTo: '/list', pathMatch: 'full' },
  { path: '**', component: PageNotFoundComponent }
];

@NgModule({
  declarations: [
    AppComponent,
    EditComponent,
    ListComponent,
    DetailsComponent,
    PageNotFoundComponent,
    DeleteComponent
  ],
  imports: [
    RouterModule.forRoot(
      appRoutes
    ),
    BrowserModule,
    HttpClientModule,
    FormsModule,
    NgDatepickerModule
  ],
  providers: [DatePipe, EmployeesService],
  bootstrap: [AppComponent]
})
export class AppModule { }
