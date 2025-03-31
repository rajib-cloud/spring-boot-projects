package com.rajib.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "emptab")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "eid")
    private Integer empId;

    @NotBlank(message = "Employee name is required")
    @Size(min = 2, max = 50, message = "Employee name must be between 2 and 50 characters")
    @Column(name = "ename", nullable = false)
    private String empName;

    @NotBlank(message = "Gender is required")
    @Pattern(regexp = "Male|Female|Other", message = "Gender must be Male, Female, or Other")
    @Column(name = "egen", nullable = false)
    private String empGen;

    @NotNull(message = "Salary is required")
    @Min(value = 10000, message = "Salary must be at least 10,000")
    @Column(name = "esal", nullable = false)
    private Double empSal;

    @NotBlank(message = "Department is required")
    @Column(name = "edept", nullable = false)
    private String empDept;

    @NotBlank(message = "Address is required")
    @Column(name = "eaddr", nullable = false)
    private String empAddr;
}
