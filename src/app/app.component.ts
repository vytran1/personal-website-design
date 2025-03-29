import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { CandidateComponent } from './components/candidate/candidate.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet],
  templateUrl: './app.component.html',
  styleUrl: './app.component.css',
})
export class AppComponent {
  title = 'personal-information-frontend';
}
