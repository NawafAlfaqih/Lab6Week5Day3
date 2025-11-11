package org.example.employeemanagementsystemlab6.Controller;

import jakarta.validation.Valid;
import org.example.employeemanagementsystemlab6.ApiResponse.ApiResponse;
import org.example.employeemanagementsystemlab6.Model.Employee;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/employee")
public class EmployeeController {

    ArrayList<Employee> employees = new ArrayList<>();

    @GetMapping("/get")
    public ResponseEntity<?> getEmployees(){
        return ResponseEntity.status(200).body(employees);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addEmployee(@RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()){
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        employees.add(employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee ID: '"+employee.getID()+"' added successfully"));
    }

    @PutMapping("/update/{index}")
    public ResponseEntity<?> updateEmployee(@PathVariable int index, @RequestBody @Valid Employee employee, Errors errors) {
        if (errors.hasErrors()) {
            String message = errors.getFieldError().getDefaultMessage();
            return ResponseEntity.status(400).body(new ApiResponse(message));
        }
        if (index >= employees.size())
            return ResponseEntity.status(400).body(new ApiResponse("The Employee doesn't exist"));

        employees.set(index, employee);
        return ResponseEntity.status(200).body(new ApiResponse("Employee ID: '"+employee.getID()+"' updated successfully"));
    }

    @DeleteMapping("/delete/{index}")
    public ResponseEntity<?> deleteEmployee(@PathVariable int index) {
        if (employees.isEmpty())
            return ResponseEntity.status(400).body(new ApiResponse("The Employees List is empty"));
        if (index >= employees.size())
            return ResponseEntity.status(400).body(new ApiResponse("The Employee doesn't exist"));

        String deletedID = employees.get(index).getID();
        employees.remove(index);
        return ResponseEntity.status(200).body("Employee ID: '"+deletedID+"' added successfully");
    }

    @GetMapping("/get/position/{position}")
    public ResponseEntity<?> searchEmployeesByPosition(@PathVariable String position) {
        if (!(position.equals("supervisor") || position.equals("coordinator")))
            return ResponseEntity.status(400).body(new ApiResponse("The position entered must be (supervisor or coordinator) "));

        ArrayList<Employee> employees1 = new ArrayList<>(employees);
        employees1.removeIf(e -> !(position.equals(e.getPosition())));
        if (employees1.isEmpty())
            return ResponseEntity.status(400).body("No employees with the position '"+position+"' ");

        return ResponseEntity.status(200).body(employees1);
    }

    @GetMapping("/get/age/{minAge}/{maxAge}")
    public ResponseEntity<?> getEmployeesByAgeRange(@PathVariable int minAge, @PathVariable int maxAge) {
        if ((minAge < 26) || (minAge > maxAge))
            return ResponseEntity.status(400).body(new ApiResponse("Age Range is not valid"));

        ArrayList<Employee> employees1 = new ArrayList<>(employees);
        employees1.removeIf(e -> (e.getAge() < minAge) || (e.getAge() > maxAge));
        if (employees1.isEmpty())
            return ResponseEntity.status(400).body(new ApiResponse("No employees in range"));

        return ResponseEntity.status(200).body(employees1);
    }

    @PutMapping("/update/leave/{index}")
    public ResponseEntity<?> applyForAnnualLeave(@PathVariable int index){
        if(index >= employees.size() || index < 0)
            return ResponseEntity.status(400).body(new ApiResponse("Employee doesn't exist"));

        Employee employee = employees.get(index);

        if (employee.isOnLeave())
            return ResponseEntity.status(400).body(new ApiResponse("Employee is already on leave"));
        if (employee.getAnnualLeave() == 0)
            return ResponseEntity.status(400).body(new ApiResponse("Employee have '0' days remaining, can't apply for leave"));

        employee.setOnLeave(true);
        employee.setAnnualLeave(employee.getAnnualLeave() - 1);
        return ResponseEntity.status(200).body(new ApiResponse("Leave Application for Employee ID: "+employee.getID()+" is done"));
    }

    @GetMapping("/get/no-leave")
    public ResponseEntity<?> getEmployeesWithNoAnnualLeave() {
        ArrayList<Employee> employees1 = new ArrayList<>(employees);
        employees1.removeIf(e -> e.getAnnualLeave() > 0);
        if (employees1.isEmpty())
            return ResponseEntity.status(400).body(new ApiResponse("No employees"));

        return ResponseEntity.status(200).body(employees1);
    }

    @PutMapping("/promote/{userIndex}/{promotedID}")
    public ResponseEntity<?> promoteEmployee(@PathVariable int userIndex, @PathVariable String promotedID) {
        if(userIndex >= employees.size() || userIndex < 0)
            return ResponseEntity.status(400).body(new ApiResponse("Promoter (User) doesn't exist"));

        Employee employee;
        for(int i = 0;; i++){
            if(employees.get(i).getID().equals(promotedID)){
                employee = employees.get(i);
                break;
            }
            if (i == employees.size() - 1 || employees.isEmpty())
                return ResponseEntity.status(400).body(new ApiResponse("Employee ID doesn't exist"));
        }

        if (!"supervisor".equals(employees.get(userIndex).getPosition()))
            return ResponseEntity.status(400).body(new ApiResponse("Promoter (User) is not 'supervisor' "));
        if (employee.getAge() < 30)
            return ResponseEntity.status(400).body(new ApiResponse("Employee must be at least 30 years old"));
        if (employee.isOnLeave())
            return ResponseEntity.status(400).body(new ApiResponse("Employee is currently on leave, can't be promoted"));

        employee.setPosition("supervisor");
        return ResponseEntity.status(200).body(new ApiResponse("Employee ID '"+employee.getID()+"' promoted to 'supervisor'!!"));
    }

}
