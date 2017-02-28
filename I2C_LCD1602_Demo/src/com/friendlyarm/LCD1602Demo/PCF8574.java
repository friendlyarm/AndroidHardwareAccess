package com.friendlyarm.LCD1602Demo;

import com.friendlyarm.AndroidSDK.HardwareControler;

public class PCF8574 {
	static final byte RS = 0x01;
	static final byte En = 0x04;
	static final byte BL = 0x08;
    
    static int pulseEnable(int devFD, byte data) throws InterruptedException
    {
		if (HardwareControler.I2CWriteByte(devFD, (byte)(data | En), 1) != 0) {
			return -1;
		}
		if (HardwareControler.I2CWriteByte(devFD, (byte)(data & ~En), 0) != 0) {
			return -1;
		}
        return 0;
    }
    
    static int writeCmd4(int devFD, byte command) throws InterruptedException
    {
		if (HardwareControler.I2CWriteByte(devFD, (byte)(command | BL), 0) != 0) {
			return -1;
		}
		if (pulseEnable(devFD, (byte)(command | BL)) != 0) {
			return -1;
		}
        return 0;
    }

    static int writeCmd8(int devFD, byte command) throws InterruptedException
    {
        byte command_H = (byte) (command & 0xf0);
        byte command_L = (byte) ((command << 4) & 0xf0);
        if (writeCmd4(devFD, command_H) == -1) {
            return -1;
        }

        if (writeCmd4(devFD, command_L) == -1) {
            return -1;
        }
        return 0;
    }

    static int writeData4(int devFD, byte data) throws InterruptedException
    {
		if (HardwareControler.I2CWriteByte(devFD, (byte)(data | RS | BL), 0) != 0) {
			return -1;
		}
        if (pulseEnable(devFD, (byte)(data | RS | BL)) == -1) {
            return -1;
        }
        return 0;
    }

    static int writeData8(int devFD, byte data) throws InterruptedException
    {
        byte data_H = (byte) (data & 0xf0);
        byte data_L = (byte) ((data << 4) & 0xf0);
        if (writeData4(devFD, data_H) == -1) {
            return -1;
        }
        if (writeData4(devFD, data_L) == -1) {
            return -1;
        }
        return 0;
    }
}
