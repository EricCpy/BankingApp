import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LogoutComponent } from './logout/logout.component';
import { BankingOverviewComponent } from './banking-overview/banking-overview.component';

const routes: Routes = [
  {
    path: '',
    title: "Banking - Welcome",
    children: [
      { path: 'banking', component: BankingOverviewComponent, title: "Online Banking" },
    ]
  },
  { path: 'logout', component: LogoutComponent, title: "Banking - Register" }
  //{ path: '**', redirectTo: '' }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
