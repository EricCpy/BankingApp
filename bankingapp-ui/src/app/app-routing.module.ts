import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { LogoutComponent } from './logout/logout.component';
import { BankingOverviewComponent } from './banking-overview/banking-overview.component';
import { AccountOverviewComponent } from './account-overview/account-overview.component';

const routes: Routes = [
  {
    path: '',
    title: "Banking - Welcome",
    children: [
      { path: 'accounts', component: AccountOverviewComponent, title: "Online Banking" },
      { path: 'accounts/banking/:iban', component: BankingOverviewComponent, title: "Online Banking" },
    ]
  },
  { path: 'logout', component: LogoutComponent, title: "Banking - Register" }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
