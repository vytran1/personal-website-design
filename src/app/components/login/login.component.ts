import { Component, OnDestroy, OnInit } from '@angular/core';
import {
  FormBuilder,
  FormGroup,
  ReactiveFormsModule,
  Validators,
} from '@angular/forms';
import { Router } from '@angular/router';
import { AuthServiceService } from '../../services/authentication/services/auth-service.service';
import { Account } from '../../services/authentication/model/account.model';
import { RefreshTokenService } from '../../services/authentication/services/refresh-token.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'app-login',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './login.component.html',
  styleUrl: './login.component.css',
})
export class LoginComponent implements OnInit, OnDestroy {
  subscriptions: Subscription[] = [];
  loginForm: FormGroup = new FormGroup({});
  isLoading: boolean = false;
  isGoogleLoginProcess: boolean = false;
  messageFail: string | null = null;

  constructor(
    private formBuilder: FormBuilder,
    private router: Router,
    private authService: AuthServiceService,
    private refreshTokenService: RefreshTokenService
  ) {}

  ngOnInit(): void {
    this.subscriptions.push(
      this.authService.isLogged().subscribe((isLoggedIn) => {
        if (isLoggedIn) {
          this.router.navigateByUrl('/candidate');
        }
      })
    );

    this.loginForm = this.formBuilder.group({
      email: [
        '',
        {
          validators: [Validators.required, Validators.email],
        },
      ],
      password: [
        '',
        {
          validators: [Validators.required],
        },
      ],
    });
  }

  submit() {
    console.log('Submitted');
    if (this.loginForm.valid) {
      this.isLoading = true;
      const accountInfo: Account = this.loginForm.value;
      console.log(accountInfo);

      this.subscriptions.push(
        this.authService.login(accountInfo).subscribe({
          next: (response) => {
            if (response.status == 200) {
              console.log(response);
              const token = response.body.accessToken;
              const refreshToken = response.body.refreshToken;
              console.log('Access Token ' + token);
              console.log('Refresh Token ' + refreshToken);

              this.authService.saveToken(token);
              this.refreshTokenService.saveRefreshToken(refreshToken);
              this.authService.loadToken();

              if (
                this.authService.isAdminAccount() ||
                this.authService.isSuperAdminAccount()
              ) {
                this.router.navigateByUrl('/admin');
              } else {
                this.router.navigateByUrl('/candidate');
              }
            }
          },
          error: (err) => {
            console.log(err);
            console.log(err.status);
            console.log(err.error);
            this.isLoading = false;
            this.messageFail = err.error;
          },
          complete: () => {
            this.isLoading = false;
          },
        })
      );
    }
  }

  redirectToGoogleLoginPage() {
    this.isGoogleLoginProcess = true;
    this.subscriptions.push(
      this.authService.getGoogleLoginLink().subscribe({
        next: (response) => {
          console.log('Response From Google Get Link API');
          console.log(response);
          const url = response.body.url;
          console.log('Url From Backend', url);
          window.location.href = url;
        },
        error: (err) => {
          this.isGoogleLoginProcess = false;
          console.log('Error From Google Get Link API');
          console.log(err);
        },
        complete: () => {
          this.isGoogleLoginProcess = false;
        },
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }
}
