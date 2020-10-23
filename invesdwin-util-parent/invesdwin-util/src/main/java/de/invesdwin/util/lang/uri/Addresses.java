package de.invesdwin.util.lang.uri;

import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class Addresses {

    public static final int PORT_MIN = 1;
    public static final int PORT_MAX = 65535;
    public static final List<Integer> ALL_PORTS;

    private static final Pattern BYTE_PATTERN = Pattern.compile("(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)");
    private static final Pattern IP_PATTERN = Pattern
            .compile(BYTE_PATTERN + "\\." + BYTE_PATTERN + "\\." + BYTE_PATTERN + "\\." + BYTE_PATTERN);

    static {
        final List<Integer> ports = new ArrayList<Integer>(PORT_MAX);
        for (int port = PORT_MIN; port <= PORT_MAX; port++) {
            ports.add(port);
        }
        ALL_PORTS = Collections.unmodifiableList(ports);
    }

    private Addresses() {}

    public static boolean isPort(final String port) {
        try {
            return isPort(Integer.parseInt(port));
        } catch (final NumberFormatException e) {
            return false;
        }
    }

    public static boolean isPort(final int port) {
        return port >= PORT_MIN && port <= PORT_MAX;
    }

    public static boolean isIp(final String address) {
        if (address == null) {
            return false;
        }
        return IP_PATTERN.matcher(address).matches();
    }

    /**
     * E.g.: 127.0.0.1:8080
     */
    public static boolean isIpWithPort(final String ipMitPort) {
        final String[] parts = ipMitPort.split(":");
        if (parts.length != 2) {
            return false;
        } else {
            return isIp(parts[0]) && isPort(parts[1]);
        }
    }

    public static InetAddress asAddress(final String host) {
        try {
            return InetAddress.getByName(host); //SUPPRESS CHECKSTYLE singleline
        } catch (final UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    public static InetSocketAddress asAddress(final String host, final int port) {
        final InetAddress addr = asAddress(host);
        return addr == null ? null : new InetSocketAddress(addr, port);
    }

    public static boolean isConnectionPossible(final String host, final int port) {
        try (Socket ignored = new Socket(host, port)) {
            return true;
        } catch (final Throwable e) {
            return false;
        }
    }

}
