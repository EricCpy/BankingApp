import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { MatDialog } from '@angular/material/dialog';
import { LoginFormComponent } from '../login-form/login-form.component';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  landing: boolean = true;
  logoutScreen: boolean = true;
  username: string = "My profile";
  caption: string = "Welcome";
  headers: Map<string, string> = new Map([
    ['/', 'Welcome'],
    ['/banking', 'Banking'],
    ['/logout', 'Logout']
  ])


  constructor(private router: Router, private authService: AuthService, private matDialog: MatDialog) {
    this.router.events.subscribe(
      (event) => {
        if (event instanceof NavigationEnd) {
          let top = event.url.substring(event.url.indexOf('/'), event.url.indexOf('/', 2) != -1 ? event.url.indexOf('/', 2) : event.url.length);
          this.caption = this.headers.get(top)!;
        }
      });
  }


  login() {
    const dialogRef = this.matDialog.open(LoginFormComponent, {
      position: {
        top: "6em"
      },
      enterAnimationDuration: 250,
      exitAnimationDuration: 100
    });
    dialogRef.afterClosed().subscribe(result => {
      if(result) {
        this.landing = false;
        this.logoutScreen = false;
        this.username = result;
      }
    })
  }

  logout() {
    this.logoutScreen = true;
    this.authService.logout();
  }

}
