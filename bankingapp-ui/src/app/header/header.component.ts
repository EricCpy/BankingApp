import { Component } from '@angular/core';
import { NavigationEnd, Router } from '@angular/router';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent {
  logoutScreen: boolean = false;
  username: string = "My profile";
  caption: string = "Welcome";
  topRoute: string = 'false';
  headers: Map<string, string> = new Map([
    ['/', 'Welcome'],
    ['/banking', 'Banking'],
    ['/logout', 'Logout']
  ])


  constructor(private router: Router) {
    this.router.events.subscribe(
      (event) => {
        if (event instanceof NavigationEnd) {
          let top = event.url.substring(event.url.indexOf('/'), event.url.indexOf('/', 2) != -1 ? event.url.indexOf('/', 2) : event.url.length);
          if (this.topRoute != top) {
            this.logoutScreen = true;
          }
          this.caption = this.headers.get(top)!;
        }
      });
  }


  login() {

  }

  logout() {

  }

}
