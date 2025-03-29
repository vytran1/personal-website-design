import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AuthServiceService } from '../../../services/authentication/services/auth-service.service';
import { ActivatedRoute, Router } from '@angular/router';
import { RefreshTokenService } from '../../../services/authentication/services/refresh-token.service';

@Component({
  selector: 'app-google-login-handler',
  standalone: true,
  imports: [],
  templateUrl: './google-login-handler.component.html',
  styleUrl: './google-login-handler.component.css',
})
export class GoogleLoginHandlerComponent implements OnInit, OnDestroy {
  subscriptions: Subscription[] = [];

  constructor(
    private authService: AuthServiceService,
    private refreshTokenService: RefreshTokenService,
    private route: ActivatedRoute,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.route.queryParams.subscribe((params) => {
      const code = params['code'];

      if (code) {
        this.authService.processGoogleLogin(code).subscribe({
          next: (response) => {
            console.log('Response from google login process call back API');
            console.log(response);

            const accessToken = response.body.accessToken;
            const refreshToken = response.body.refreshToken;

            this.authService.saveToken(accessToken);
            this.refreshTokenService.saveRefreshToken(refreshToken);

            this.router.navigateByUrl('/candidate');
          },
          error: (err) => {
            console.log('Error from google login process call back API');
            console.log(err);

            this.router.navigateByUrl('/login');
          },
          complete: () => {
            console.log('Complete Google Login Process');
          },
        });
      }
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }
}
