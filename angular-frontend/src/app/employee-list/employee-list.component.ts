import { Component } from '@angular/core';
import { Employee } from '../employee';
import { EmployeeService } from '../employee.service';

@Component({
  selector: 'app-employee-list',
  templateUrl: './employee-list.component.html',
  styleUrls: ['./employee-list.component.css']
})
export class EmployeeListComponent {

  employees!: Employee[];

  constructor(private employeeService: EmployeeService) {}

  /*ngOnInit(): void {
    this.employees = [
      {
        "id":1 ,
        "firstName": "ramesh",
        "lastName": "kumar",
        "emailId": "ramesh@gmail.com"
      },
      {
        "id": 2,
        "firstName": "raghava",
        "lastName": "dudi",
        "emailId": "raghava@gmail.com"
      },
      {
        "id": 3,
        "firstName": "rakesh",
        "lastName": "dudi",
        "emailId": "rakesh@gmail.com"
      }];
  }*/
  
  ngOnInit(): void {
    this.getEmployees();
  }
  
  private getEmployees() {
    this.employeeService.getEmployeesList().subscribe((data:any) => {
      this.employees = data;
    });
  }

}
