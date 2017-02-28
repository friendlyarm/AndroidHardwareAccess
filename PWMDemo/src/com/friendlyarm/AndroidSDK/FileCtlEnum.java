package com.friendlyarm.AndroidSDK;

public class FileCtlEnum {
    /* File Flag */
    public final static int O_ACCMODE	= 00000003;
    public final static int O_RDONLY	= 00000000;
    public final static int O_WRONLY	= 00000001;
    public final static int O_RDWR		= 00000002;
    public final static int O_CREAT		= 00000100;	/* not fcntl */
    public final static int O_EXCL		= 00000200;	/* not fcntl */
    public final static int O_NOCTTY	= 00000400;	/* not fcntl */
    public final static int O_TRUNC		= 00001000;	/* not fcntl */
    public final static int O_APPEND	= 00002000;
    public final static int O_NONBLOCK	= 00004000;
    public final static int O_DSYNC		= 00010000;	/* used to be O_SYNC, see below */
    public final static int FASYNC		= 00020000;	/* fcntl, for BSD compatibility */
    public final static int O_DIRECT	= 00040000;	/* direct disk access hint */
    public final static int O_LARGEFILE	= 00100000;
    public final static int O_DIRECTORY	= 00200000;	/* must be a directory */
    public final static int O_NOFOLLOW	= 00400000;	/* don't follow links */
    public final static int O_NOATIME	= 01000000;
    public final static int O_CLOEXEC	= 02000000;	/* set close_on_exec */
}
