import { Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { Subscription } from 'rxjs';
import { RoleService } from '../../../../services/role.service';
import { isRoleNameUnique } from '../../../../validators/role.validator';
import { Role } from '../../../../models/role/role.model';
import { DialogService } from '../../../shared/services/dialog.service';
import { Router } from '@angular/router';

@Component({
  selector: 'app-role-creating-form',
  standalone: true,
  imports: [ReactiveFormsModule],
  templateUrl: './role-creating-form.component.html',
  styleUrl: './role-creating-form.component.css',
})
export class RoleCreatingFormComponent implements OnInit, OnDestroy {
  subscriptions: Subscription[] = [];
  form: any;
  isProcessing: boolean = false;

  constructor(
    private formBuilder: FormBuilder,
    private roleService: RoleService,
    private dialogService: DialogService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.form = this.formBuilder.group({
      //Name
      name: [
        '',
        {
          validators: [Validators.required, Validators.minLength(5)],
          asyncValidators: [isRoleNameUnique(this.roleService)],
          updateOn: 'blur',
        },
      ],
      //Description
      description: [
        '',
        {
          validator: [Validators.required, Validators.minLength(10)],
        },
      ],
    });
  }

  ngOnDestroy(): void {
    this.subscriptions.forEach((subscription) => subscription.unsubscribe());
  }

  onSubmit() {
    // form is valid
    console.log('Submit');
    // console.log(this.form.value);

    const requestBody: Role = this.form.value;
    console.log('Request Body', requestBody);
    this.isProcessing = true;
    this.subscriptions.push(
      this.roleService.createNewRole(requestBody).subscribe({
        next: (response) => {
          console.log('Response', response);
          this.dialogService
            .openSuccessDialog('Successfully Creating New Role')
            .subscribe((result) => this.successHandler());
        },
        error: (err) => {
          this.isProcessing = false;
          this.dialogService.openErrorDialog('Creating New Role Fail');
        },
        complete: () => {
          this.isProcessing = false;
        },
      })
    );
  }

  successHandler() {
    this.router.navigateByUrl('/admin/roles');
  }
}
