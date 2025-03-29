import { Component, OnDestroy, OnInit } from '@angular/core';
import { CandidateServiceService } from '../../services/candidate-service.service';
import { Subscription } from 'rxjs';
import { Candidate } from '../../models/candidate/candidate.model';
import { ActivatedRoute, Router } from '@angular/router';
import { CommonModule } from '@angular/common';
import { CertificateService } from '../../services/certificate.service';
import { Certificate } from '../../models/certificate';

@Component({
  selector: 'app-candidate',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './candidate.component.html',
  styleUrl: './candidate.component.css',
})
export class CandidateComponent implements OnInit, OnDestroy {
  subscriptions: Subscription[] = [];
  candidate: Candidate | null = null;
  certificate: Certificate[] | null = null;
  isLoading: boolean = false;

  constructor(
    private candidateService: CandidateServiceService,
    private certificateService: CertificateService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.subscriptions.push(
      this.candidateService.getAllInformation().subscribe({
        next: (response) => {
          console.log(response.status);
          console.log(response.body);
          if (response.status === 200) {
            this.candidate = response.body;

            //Calling API to get Certificates belonging to current user
            this.certificateService.getCertificateByCandidateId().subscribe({
              next: (response) => {
                console.log('Certificate Response Status ' + response.status);
                console.log('Certificate Response body ', response.body);
                this.certificate = response.body;
              },
              error: (err) => {
                console.log('Error From Certificate API calling ');
                console.log(err);
              },
              complete: () => {
                console.log('Complete API calling');
              },
            });
          }
        },
        error: (err) => {
          console.log(err);
        },
        complete: () => {
          console.log('Complete Request');
        },
      })
    );
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }
}
