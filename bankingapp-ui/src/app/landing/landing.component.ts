import { Component, OnInit } from '@angular/core';
import { RegisterFormComponent } from '../register-form/register-form.component';
import { MatDialog } from '@angular/material/dialog';
import { ActivatedRoute, Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { SnackbarService } from '../services/snackbar.service';
import { environment } from 'src/environments/environment';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'app-landing',
  templateUrl: './landing.component.html',
  styleUrls: ['./landing.component.scss']
})
export class LandingComponent implements OnInit {

  constructor(private matDialog: MatDialog,
    private route: ActivatedRoute,
    private router: Router,
    private http: HttpClient,
    private snackbarService: SnackbarService) { }

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const token = params['token'];
      if (!token) return;
      const url = environment.backendUrl + 'registration/verify?token=' + token;
      return this.http.get(url, { responseType: 'text' })
        .pipe(finalize(() => this.router.navigate([''])))
        .subscribe({
          next: () => {
            this.snackbarService.openSnackBar("Email verified, please login!", 15, false);
          }
        })
    })
  }

  register() {
    this.matDialog.open(RegisterFormComponent, {
      position: {
        top: "6em"
      },
      enterAnimationDuration: 250,
      exitAnimationDuration: 100
    });
  }
}
