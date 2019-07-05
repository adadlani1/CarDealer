package com.car.utils;

//interface for the encryption and contains the methods required for encryption
public interface Encryption {

    byte[] encrypt(byte[] data);

    byte[] decrypt(byte[] data);
}
