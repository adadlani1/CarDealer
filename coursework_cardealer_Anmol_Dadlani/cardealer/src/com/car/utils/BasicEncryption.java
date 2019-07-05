package com.car.utils;
//class that encrypts and decrypts the password
public class BasicEncryption implements Encryption {

    //method for encrypting the password
    @Override
    public byte[] encrypt(byte[] data) {
        byte[] enc = new byte[data.length];

        for (int i = 0; i < data.length; i++) {
            enc[i] = (byte) ((i % 2 == 0 )? data[i] - 1 : data[i] + 1);

        }

        return enc;
    }

    //method for decrypting the method
    @Override
    public byte[] decrypt(byte[] data) {
        byte[] enc = new byte[data.length];

        for (int i = 0; i < data.length; i++) {
            enc[i] = (byte) ((i % 2 == 0) ? data[i] + 1 : data[i] - 1);

        }

        return enc;
    }
}
