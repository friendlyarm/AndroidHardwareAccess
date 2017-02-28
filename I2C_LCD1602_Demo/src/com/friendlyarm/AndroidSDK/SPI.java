package com.friendlyarm.AndroidSDK;
import android.util.Log;
import android.widget.Toast;

import com.friendlyarm.AndroidSDK.HardwareControler;
import com.friendlyarm.AndroidSDK.SPIEnum;
import com.friendlyarm.AndroidSDK.GPIOEnum;
import com.friendlyarm.AndroidSDK.FileCtlEnum;

public class SPI {
	private static final String TAG = "com.friendlyarm.AndroidSDK.SPI";
	private int spi_mode = 0;
	private int spi_bits = 8;
	private int spi_delay = 0;
	private int spi_speed = 500000;
	private int spi_byte_order = SPIEnum.LSBFIRST;
	
    private static final String devName = "/dev/spidev1.0";
    private int spi_fd = -1;
	
	public void begin() {
		spi_fd = HardwareControler.open( devName, FileCtlEnum.O_RDWR );
		if (spi_fd >= 0) {
			Log.d(TAG, "open " + devName + "ok!");
	
			/* spi init */
			HardwareControler.setSPIWriteBitsPerWord( spi_fd, spi_bits );
			HardwareControler.setSPIReadBitsPerWord( spi_fd, spi_bits );
		} else {
			Log.d(TAG, "open " + devName + "failed!");
			spi_fd = -1;
		}
	}
	
	public void end() {
    	if (spi_fd != -1) {
    	    HardwareControler.close(spi_fd);
    	    spi_fd = -1;
    	}
	}
	
	public void setBitOrder(int order) {
		if (spi_fd < 0) {
			return ;
		}
		spi_byte_order = SPIEnum.MSBFIRST;
		if(spi_byte_order == SPIEnum.LSBFIRST) {
			spi_mode |=  SPIEnum.SPI_LSB_FIRST;
		} else {
			spi_mode &= ~SPIEnum.SPI_LSB_FIRST;
		}
		HardwareControler.setSPIBitOrder( spi_fd, spi_byte_order );

	}
	
	public void setClockDivider(int divider) {
		if (spi_fd < 0) {
			return ;
		}
    	spi_speed = 66666666/(2*(divider+1));
    	if(spi_speed > 500000) {	
    		spi_speed = 500000;
    	}
		HardwareControler.setSPIClockDivider( spi_fd, divider);
	}
	
	public void setDataMode(int mode) {
		if (spi_fd < 0) {
			return ;
		}
		switch(mode)
		{
	        case SPIEnum.SPI_MODE0:
	            spi_mode &= ~(SPIEnum.SPI_CPHA|SPIEnum.SPI_CPOL);
	            break;
	        case SPIEnum.SPI_MODE1:
	            spi_mode &= ~(SPIEnum.SPI_CPOL);
	            spi_mode |= (SPIEnum.SPI_CPHA);
	            break;
	        case SPIEnum.SPI_MODE2:
	            spi_mode |= (SPIEnum.SPI_CPOL);
	            spi_mode &= ~(SPIEnum.SPI_CPHA);
	            break;
	        case SPIEnum.SPI_MODE3:
	            spi_mode |= (SPIEnum.SPI_CPHA|SPIEnum.SPI_CPOL);
	            break;
	        default:
	            Log.e(TAG, "error data mode");
		}
		
		HardwareControler.setSPIDataMode( spi_fd, spi_mode );
	}
	
	public void setChipSelectPolarity(int cs, int active) {
		
	}
	
	public void chipSelect(int cs) {
		
	}
	
	public byte transfer(int value) {
		if (spi_fd < 0) {
			return 0;
		}
		return (byte) HardwareControler.SPItransferOneByte(spi_fd, (byte) value, spi_delay, spi_speed, spi_bits);
	}
}
