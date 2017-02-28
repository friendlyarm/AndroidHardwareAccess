package com.friendlyarm.AndroidSDK;
public class SPIEnum {

// SPIBitOrder
	public final static int LSBFIRST = 0;  ///< LSB First
	public final static int MSBFIRST = 1;   ///< MSB First

// SPIMode
	public final static int SPI_MODE0 = 0;  ///< CPOL = 0, CPHA = 0
	public final static int SPI_MODE1 = 1;  ///< CPOL = 0, CPHA = 1
	public final static int SPI_MODE2 = 2;  ///< CPOL = 1, CPHA = 0
	public final static int SPI_MODE3 = 3;  ///< CPOL = 1, CPHA = 1
	
	
	public final static int SPI_CPHA = 0x01;
	public final static int SPI_CPOL = 0x02;
	public final static int SPI_CS_HIGH = 0x04;
	public final static int SPI_LSB_FIRST = 0x08;
	public final static int SPI_3WIRE = 0x10;
	public final static int SPI_LOOP = 0x20;
	public final static int SPI_NO_CS = 0x40;
	public final static int SPI_READY = 0x80;

// SPIClockDivider
	public final static int SPI_CLOCK_DIV65536 = 0;       ///< 65536 = 256us = 4kHz
	public final static int SPI_CLOCK_DIV32768 = 32768;   ///< 32768 = 126us = 8kHz
	public final static int SPI_CLOCK_DIV16384 = 16384;   ///< 16384 = 64us = 15.625kHz
	public final static int SPI_CLOCK_DIV8192  = 8192;    ///< 8192 = 32us = 31.25kHz
	public final static int SPI_CLOCK_DIV4096  = 4096;    ///< 4096 = 16us = 62.5kHz
	public final static int SPI_CLOCK_DIV2048  = 2048;    ///< 2048 = 8us = 125kHz
	public final static int SPI_CLOCK_DIV1024  = 1024;    ///< 1024 = 4us = 250kHz
	public final static int SPI_CLOCK_DIV512   = 512;     ///< 512 = 2us = 500kHz
	public final static int SPI_CLOCK_DIV256   = 256;     ///< 256 = 1us = 1MHz
	public final static int SPI_CLOCK_DIV128   = 128;     ///< 128 = 500ns = = 2MHz
	public final static int SPI_CLOCK_DIV64    = 64;      ///< 64 = 250ns = 4MHz
	public final static int SPI_CLOCK_DIV32    = 32;      ///< 32 = 125ns = 8MHz
	public final static int SPI_CLOCK_DIV16    = 16;      ///< 16 = 50ns = 20MHz
	public final static int SPI_CLOCK_DIV8     = 8;       ///< 8 = 25ns = 40MHz
	public final static int SPI_CLOCK_DIV4     = 4;       ///< 4 = 12.5ns 80MHz
	public final static int SPI_CLOCK_DIV2     = 2;       ///< 2 = 6.25ns = 160MHz
	public final static int SPI_CLOCK_DIV1     = 1;       ///< 0 = 256us = 4kHz
 }