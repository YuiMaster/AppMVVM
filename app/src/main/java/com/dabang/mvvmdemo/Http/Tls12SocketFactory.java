package com.dabang.mvvmdemo.Http;

/**
 * Created by Jane on 2017/7/20.
 */

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * author: heshantao
 * data: 2017/3/12.
 * 自定义SocketFactory 使得 Android4.x打开对TLS1.1、TLS1.2的支持
 */

public class Tls12SocketFactory extends SSLSocketFactory {
    private static final String[] TLS_SUPPORT_VERSION = {"TLSv1.1", "TLSv1.2"};

    final SSLSocketFactory delegate;

    public Tls12SocketFactory(SSLSocketFactory base) {
        this.delegate = base;
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return delegate.getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return delegate.getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException {
        return patch(delegate.createSocket(socket, host, port, autoClose));
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException {
        return patch(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress inetAddress, int localPort) throws IOException {
        return patch(delegate.createSocket(host, port, inetAddress, localPort));
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return patch(delegate.createSocket(host, port));
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return patch(delegate.createSocket(address, port, localAddress, localPort));
    }

    private Socket patch(Socket s) {
        if (s instanceof SSLSocket) {
            ((SSLSocket) s).setEnabledProtocols(TLS_SUPPORT_VERSION);
        }
        return s;
    }
}
