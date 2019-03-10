import {Component, OnInit} from '@angular/core';
import {Router, ActivatedRoute} from '@angular/router';
import {FormControl, FormGroupDirective, FormBuilder, FormGroup, NgForm, Validators} from '@angular/forms';

import {ApiService} from '../api.service';

@Component({
  selector: 'app-customer-edit',
  templateUrl: './customer-edit.component.html',
  styleUrls: ['./customer-edit.component.css']
})
export class CustomerEditComponent implements OnInit {

  customerForm: FormGroup;
  id = '';
  name: object = {
    first: <string>'',
    last: <string>''
  };
  gender = '';
  birthday = '';
  lastContact = '';
  customerLifetimeValue = '';

  constructor(private router: Router, private route: ActivatedRoute, private api: ApiService, private formBuilder: FormBuilder) {
  }

  ngOnInit() {
    this.getCustomer(this.route.snapshot.params['id']);
    this.customerForm = this.formBuilder.group({
      'firstName': [null, Validators.required],
      'lastName': [null, Validators.required],
      'gender': [null, Validators.required],
      'birthday': [null, Validators.required],
      'lastContact': [null, Validators.required],
      'customerLifetimeValue': [null, Validators.required]
    });
  }

  getCustomer(id) {
    this.api.getCustomer(id).subscribe(data => {
      this.id = data._id;
      this.customerForm.setValue({
        firstName: data.name.first,
        lastName: data.name.last,
        gender: data.gender,
        birthday: data.birthday,
        lastContact: data.lastContact,
        customerLifetimeValue: data.customerLifetimeValue
      });
    });
  }

  // onReset(form: NgForm) {

  onFormSubmit(form: NgForm) {

      alert("erllo");
    // onFormSubmit(customerForm.value)
    this.api.updateCustomer(this.id, form)
      .subscribe(res => {
          let id = res['_id'];
          this.router.navigate(['/customer-details', id]);
        }, (err) => {
          console.log(err);
        }
      );
  }

  customerDetails() {
    this.router.navigate(['/customer-details', this.id]);
  }
}
