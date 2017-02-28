package com.friendlyarm.AndroidSDK;

/*
GPIO signals have paths like /sys/class/gpio/gpio42/ (for GPIO #42)
and have the following read/write attributes:

    /sys/class/gpio/gpioN/

        "direction" ... reads as either "in" or "out".  This value may
                normally be written.  Writing as "out" defaults to
                initializing the value as low.  To ensure glitch free
                operation, values "low" and "high" may be written to
                configure the GPIO as an output with that initial value.

                Note that this attribute *will not exist* if the kernel
                doesn't support changing the direction of a GPIO, or
                it was exported by kernel code that didn't explicitly
                allow userspace to reconfigure this GPIO's direction.

        "value" ... reads as either 0 (low) or 1 (high).  If the GPIO
                is configured as an output, this value may be written;
                any nonzero value is treated as high.

                If the pin can be configured as interrupt-generating interrupt
                and if it has been configured to generate interrupts (see the
                description of "edge"), you can poll(2) on that file and
                poll(2) will return whenever the interrupt was triggered. If
                you use poll(2), set the events POLLPRI and POLLERR. If you
                use select(2), set the file descriptor in exceptfds. After
                poll(2) returns, either lseek(2) to the beginning of the sysfs
                file and read the new value or close the file and re-open it
                to read the value.
 */

public class GPIOEnum {
	//Direction
	public final static int IN = 1; 
	public final static int OUT = 2; 
	
	//Value
	public final static int LOW = 0;
	public final static int HIGH = 1;
}
