package com.car.roles;

import com.car.form.Search;

import java.io.IOException;

public class Customer extends User {

    public void search() throws IOException {
        Search.setVisible();
    }

}
