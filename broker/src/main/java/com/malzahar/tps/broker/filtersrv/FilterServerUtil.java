package com.malzahar.tps.broker.filtersrv;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FilterServerUtil {

    private final static Logger log = LoggerFactory.getLogger(FilterServerManager.class);

    public static void callShell(final String shellString) {
        Process process = null;
        try {
            String[] cmdArray = splitShellString(shellString);
            process = Runtime.getRuntime().exec(cmdArray);
            process.waitFor();
            log.info("CallShell: <{}> OK", shellString);
        } catch (Throwable e) {
            log.error("CallShell: readLine IOException, {}", shellString, e);
        } finally {
            if (null != process)
                process.destroy();
        }
    }

    private static String[] splitShellString(final String shellString) {
        return shellString.split(" ");
    }
}
