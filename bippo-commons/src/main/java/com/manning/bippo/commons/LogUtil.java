package com.manning.bippo.commons;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

public final class LogUtil
{
    public static String expand(Throwable t)
    {
        if (t.getCause() == null) {
            return t.getMessage();
        }

        final StringBuilder sb = new StringBuilder(t.getMessage());

        while ((t = t.getCause()) != null) {
            sb.append(": ").append(t.getMessage());
        }

        return sb.toString();
    }

    public static void debug(Object obj)
    {
        if (obj == null)
        {
            getLogger().debug("null");
        } else
        {
            getLogger().debug(obj.toString());
        }
    }

    private static Logger getLogger()
    {
        StackTraceElement myCaller = Thread.currentThread().getStackTrace()[3];
        return LoggerFactory.getLogger(myCaller.getClassName());
    }

    public static void debug(String message, Object... args)
    {
        if (isDebugEnabled())
        {
            getLogger().debug(message, args);
        }
    }

    public static void debug(String message, Throwable ex)
    {
        if (isDebugEnabled())
        {
            getLogger().debug(message, ex);
        }
    }

    public static void debug(Marker marker, String msg, Object... args)
    {
        if (isDebugEnabled(marker))
        {
            getLogger().debug(marker, msg, args);
        }
    }

    public static void debug(Marker marker, String msg, Throwable exception)
    {
        if (isDebugEnabled(marker))
        {
            getLogger().debug(marker, msg, exception);
        }
    }

    public static void error(String msg, Object... args)
    {
        if (isErrorEnabled())
        {
            getLogger().error(msg, args);
        }
    }

    public static void error(String msg, Throwable exception)
    {
        if (isErrorEnabled())
        {
            getLogger().error(msg, exception);
        }
    }

    public static void error(Marker marker, String msg, Object... args)
    {
        if (isErrorEnabled(marker))
        {
            getLogger().error(marker, msg, args);
        }
    }

    public static void error(Marker marker, String msg, Throwable exception)
    {
        if (isErrorEnabled(marker))
        {
            getLogger().error(marker, msg, exception);
        }
    }

    public String getName()
    {
        return "LogUtil";
    }

    public static void info(String msg, Object... args)
    {
        if (isInfoEnabled())
        {
            getLogger().info(msg, args);
        }
    }

    public static void info(String msg, Throwable exception)
    {
        if (isInfoEnabled())
        {
            getLogger().info(msg, exception);
        }
    }

    public static void info(Marker marker, String msg, Object... args)
    {
        getLogger().info(marker, msg, args);
    }

    public static void info(Marker marker, String msg, Throwable exception)
    {
        if (isInfoEnabled(marker))
        {
            getLogger().info(marker, msg, exception);
        }
    }

    public static boolean isDebugEnabled()
    {
        return getLogger().isDebugEnabled();
    }


    public static boolean isDebugEnabled(Marker marker)
    {
        return getLogger().isDebugEnabled(marker);
    }


    public static boolean isErrorEnabled()
    {
        return getLogger().isErrorEnabled();
    }


    public static boolean isErrorEnabled(Marker marker)
    {
        return getLogger().isErrorEnabled(marker);
    }


    public static boolean isInfoEnabled()
    {
        return getLogger().isInfoEnabled();
    }


    public static boolean isInfoEnabled(Marker marker)
    {
        return getLogger().isInfoEnabled(marker);
    }


    public static boolean isTraceEnabled()
    {
        return getLogger().isTraceEnabled();
    }


    public static boolean isTraceEnabled(Marker marker)
    {
        return getLogger().isTraceEnabled(marker);
    }


    public static boolean isWarnEnabled()
    {
        return getLogger().isWarnEnabled();
    }


    public static boolean isWarnEnabled(Marker marker)
    {
        return getLogger().isWarnEnabled(marker);
    }

    public static void trace(String msg, Object... args)
    {
        if (isTraceEnabled())
        {
            getLogger().trace(msg, args);
        }
    }

    public static void trace(String msg, Throwable exception)
    {
        if (isTraceEnabled())
        {
            getLogger().trace(msg, exception);
        }
    }

    public static void trace(Marker marker, String msg, Object... args)
    {
        if (isTraceEnabled(marker))
        {
            getLogger().trace(marker, msg, args);
        }
    }

    public static void trace(Marker marker, String msg, Throwable exception)
    {
        if (isTraceEnabled(marker))
        {
            getLogger().trace(marker, msg, exception);
        }
    }

    public static void warn(String msg, Object... args)
    {
        if (isWarnEnabled())
        {
            getLogger().warn(msg, args);
        }
    }


    public static void warn(String msg, Throwable exception)
    {
        if (isWarnEnabled())
        {
            getLogger().warn(msg, exception);
        }
    }

    public static void warn(Marker marker, String msg, Object... args)
    {
        if (isWarnEnabled(marker))
        {
            getLogger().warn(marker, msg, args);
        }
    }

    public static void warn(Marker marker, String msg, Throwable exception)
    {
        if (isWarnEnabled(marker))
        {
            getLogger().warn(marker, msg, exception);
        }
    }
}
