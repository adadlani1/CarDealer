package com.car.roles;

import java.io.FileNotFoundException;

//abstract class with abstract methods that Admin and Staff have in common
abstract class Employee extends User {
    abstract void sellCars() throws FileNotFoundException;

    abstract void addCars();

    abstract void addACar();

    abstract void calculateRevenue();

    abstract void printCars();
}
