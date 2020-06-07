package de.invesdwin.util.lang.uri.connect.apache;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.URI;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import javax.annotation.concurrent.NotThreadSafe;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequest;
import org.apache.hc.client5.http.async.methods.SimpleHttpRequests;
import org.apache.hc.client5.http.async.methods.SimpleHttpResponse;
import org.apache.hc.client5.http.async.methods.SimpleRequestProducer;
import org.apache.hc.client5.http.async.methods.SimpleResponseConsumer;
import org.apache.hc.client5.http.config.RequestConfig;
import org.apache.hc.client5.http.impl.async.CloseableHttpAsyncClient;
import org.apache.hc.client5.http.impl.async.HttpAsyncClientBuilder;
import org.apache.hc.client5.http.impl.nio.PoolingAsyncClientConnectionManagerBuilder;
import org.apache.hc.core5.concurrent.FutureCallback;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpHost;
import org.apache.hc.core5.http.nio.AsyncResponseConsumer;
import org.apache.hc.core5.io.CloseMode;
import org.apache.hc.core5.pool.PoolConcurrencyPolicy;
import org.apache.hc.core5.reactor.IOReactorConfig;
import org.apache.hc.core5.util.TimeValue;

import de.invesdwin.util.concurrent.Executors;
import de.invesdwin.util.concurrent.future.Futures;
import de.invesdwin.util.lang.Closeables;
import de.invesdwin.util.lang.Strings;
import de.invesdwin.util.lang.uri.Addresses;
import de.invesdwin.util.lang.uri.URIs;
import de.invesdwin.util.lang.uri.connect.IURIsConnect;
import de.invesdwin.util.lang.uri.connect.InputStreamHttpResponse;
import de.invesdwin.util.shutdown.IShutdownHook;
import de.invesdwin.util.shutdown.ShutdownHookManager;
import de.invesdwin.util.time.duration.Duration;
import de.invesdwin.util.time.fdate.FTimeUnit;

@NotThreadSafe
public final class URIsConnectApacheAsync implements IURIsConnect {

    public static final int MAX_CONNECTIONS = 1000;
    public static final TimeValue EVICT_IDLE_CONNECTIONS_TIMEOUT = TimeValue.of(1, TimeUnit.MINUTES);
    private static CloseableHttpAsyncClient httpClient;
    private static IShutdownHook shutdownHook;
    private static RequestConfig defaultRequestConfig = RequestConfig.DEFAULT;

    private final URI uri;
    private Duration networkTimeout = URIs.getDefaultNetworkTimeout();
    private Proxy proxy = null;

    private Map<String, String> headers;

    public URIsConnectApacheAsync(final URI url) {
        this.uri = url;
    }

    public static CloseableHttpAsyncClient getHttpClient() {
        if (httpClient == null) {
            synchronized (URIsConnectApacheAsync.class) {
                if (httpClient == null) {
                    final CloseableHttpAsyncClient client = HttpAsyncClientBuilder.create()
                            .useSystemProperties() //use system proxy etc
                            .evictExpiredConnections()
                            .evictIdleConnections(EVICT_IDLE_CONNECTIONS_TIMEOUT)
                            .setConnectionManager(PoolingAsyncClientConnectionManagerBuilder.create()
                                    .setMaxConnPerRoute(MAX_CONNECTIONS)
                                    .setMaxConnTotal(MAX_CONNECTIONS)
                                    .setPoolConcurrencyPolicy(PoolConcurrencyPolicy.LAX)
                                    .build())
                            .setIOReactorConfig(IOReactorConfig.custom()
                                    .setIoThreadCount(Executors.getCpuThreadPoolCount())
                                    .build())
                            .build();
                    client.start();
                    if (shutdownHook == null) {
                        shutdownHook = new IShutdownHook() {
                            @Override
                            public void shutdown() throws Exception {
                                resetHttpClient();
                            }
                        };
                        ShutdownHookManager.register(shutdownHook);
                    }
                    httpClient = client;
                }
            }
        }
        return httpClient;
    }

    public static void resetHttpClient() {
        synchronized (URIsConnectApacheAsync.class) {
            if (httpClient != null) {
                httpClient.close(CloseMode.GRACEFUL);
                httpClient = null;
            }
        }
    }

    private static RequestConfig.Builder applyNetworkTimeout(final RequestConfig.Builder config,
            final Duration networkTimeout) {
        return config
                .setConnectionRequestTimeout(networkTimeout.longValue(), networkTimeout.getTimeUnit().timeUnitValue())
                .setConnectTimeout(networkTimeout.longValue(), networkTimeout.getTimeUnit().timeUnitValue())
                .setResponseTimeout(networkTimeout.longValue(), networkTimeout.getTimeUnit().timeUnitValue());
    }

    private static RequestConfig.Builder applyProxy(final RequestConfig.Builder config, final Proxy proxy) {
        final InetSocketAddress address = (InetSocketAddress) proxy.address();
        final HttpHost httpHost = new HttpHost(address.getHostName(), address.getPort());
        return config.setProxy(httpHost);
    }

    @Override
    public URIsConnectApacheAsync withNetworkTimeout(final Duration networkTimeout) {
        this.networkTimeout = networkTimeout;
        return this;
    }

    @Override
    public Duration getNetworkTimeout() {
        return networkTimeout;
    }

    @Override
    public URIsConnectApacheAsync withProxy(final Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    @Override
    public Proxy getProxy() {
        return proxy;
    }

    @Override
    public URI getUri() {
        return uri;
    }

    /**
     * WARNING: connection pooling will share authentication between requests. If this is not desired, then use a
     * separate HttpClient where this information does not get shared!
     */
    @Override
    public URIsConnectApacheAsync withBasicAuth(final String username, final String password) {
        final String authString = username + ":" + password;
        final byte[] authEncBytes = Base64.encodeBase64(authString.getBytes());
        final String authStringEnc = new String(authEncBytes);
        withHeader(HttpHeaders.AUTHORIZATION, "Basic " + authStringEnc);
        return this;
    }

    @Override
    public URIsConnectApacheAsync withHeader(final String key, final String value) {
        if (headers == null) {
            headers = new HashMap<String, String>();
        }
        headers.put(key, value);
        return this;
    }

    @Override
    public boolean isServerResponding() {
        try {
            final Socket socket = new Socket();
            final int timeoutMillis = networkTimeout.intValue(FTimeUnit.MILLISECONDS);
            socket.setSoTimeout(timeoutMillis);
            socket.connect(Addresses.asAddress(uri.getHost(), uri.getPort()), timeoutMillis);
            socket.close();
            return true;
        } catch (final Throwable e) {
            return false;
        }
    }

    /**
     * Tries to open a connection to the specified url and checks if the content is available there.
     */
    @Override
    public boolean isDownloadPossible() {
        if (uri == null) {
            return false;
        }
        final Future<SimpleHttpResponse> future = openConnection(HEAD, SimpleResponseConsumer.create());
        try {
            final SimpleHttpResponse response = Futures.get(future);
            if (!URIs.isSuccessful(response)) {
                return false;
            }
            final String contentLengthStr = response.getFirstHeader(HttpHeaders.CONTENT_LENGTH).getValue();
            if (contentLengthStr != null) {
                final long contentLength = Long.parseLong(contentLengthStr);
                return contentLength > 0;
            } else {
                return true;
            }
        } catch (final Throwable e) {
            return false;
        }
    }

    @Override
    public long lastModified() {
        final Future<SimpleHttpResponse> future = openConnection(HEAD, SimpleResponseConsumer.create());
        try {
            final SimpleHttpResponse response = Futures.get(future);
            if (!URIs.isSuccessful(response)) {
                return -1;
            }
            final String lastModifiedStr = response.getFirstHeader(HttpHeaders.LAST_MODIFIED).getValue();
            if (lastModifiedStr == null) {
                return -1;
            }
            final Date lastModified = org.apache.hc.client5.http.utils.DateUtils.parseDate(lastModifiedStr);
            if (lastModified == null) {
                return -1;
            } else {
                return lastModified.getTime();
            }
        } catch (final Throwable e) {
            return -1;
        }
    }

    @Override
    public String download() {
        InputStream in = null;
        try {
            in = getInputStream();
            return IOUtils.toString(in, Charset.defaultCharset());
        } catch (final Throwable e) {
            return null;
        } finally {
            Closeables.closeQuietly(in);
        }
    }

    @Override
    public String downloadThrowing() throws IOException {
        InputStream in = null;
        try {
            in = getInputStream();
            final String response = IOUtils.toString(in, Charset.defaultCharset());
            if (response == null) {
                throw new IOException("response is null");
            }
            return response;
        } finally {
            Closeables.closeQuietly(in);
        }
    }

    public Future<InputStreamHttpResponse> openConnection() {
        return openConnection(GET, new InputStreamHttpResponseConsumerApache(), null);
    }

    public Future<InputStreamHttpResponse> openConnection(final String method) {
        return openConnection(method, new InputStreamHttpResponseConsumerApache(), null);
    }

    public <T> Future<T> openConnection(final String method, final AsyncResponseConsumer<T> responseConsumer) {
        return openConnection(method, responseConsumer, null);
    }

    public <T> Future<T> openConnection(final String method, final AsyncResponseConsumer<T> responseConsumer,
            final FutureCallback<T> callback) {
        final SimpleHttpRequest request = SimpleHttpRequests.create(method, uri);
        request.setConfig(getRequestConfig());
        if (headers != null) {
            for (final Entry<String, String> header : headers.entrySet()) {
                request.addHeader(header.getKey(), header.getValue());
            }
        }

        final SimpleRequestProducer requestProducer = SimpleRequestProducer.create(request);
        final Future<T> response = getHttpClient().execute(requestProducer, responseConsumer, callback);
        return response;
    }

    private RequestConfig getRequestConfig() {
        if (networkTimeout != URIs.getDefaultNetworkTimeout() || proxy != null) {
            RequestConfig.Builder builder = RequestConfig.copy(defaultRequestConfig);
            if (networkTimeout != URIs.getDefaultNetworkTimeout()) {
                builder = applyNetworkTimeout(builder, networkTimeout);
            }
            if (proxy != null) {
                builder = applyProxy(builder, proxy);
            }
            return builder.build();
        } else {
            return defaultRequestConfig;
        }
    }

    public InputStreamHttpResponse getInputStream(final String method) throws IOException {
        return getInputStream(openConnection(method), uri);
    }

    @Override
    public InputStreamHttpResponse getInputStream() throws IOException {
        return getInputStream(openConnection(), uri);
    }

    @Override
    public String toString() {
        return uri.toString();
    }

    public static InputStreamHttpResponse getInputStream(final Future<InputStreamHttpResponse> call, final URI uri)
            throws IOException {
        final InputStreamHttpResponse response;
        try {
            response = Futures.get(call);
        } catch (final InterruptedException e) {
            throw new IOException(e);
        }
        // https://stackoverflow.com/questions/613307/read-error-response-body-in-java
        final int responseCode = response.getResponse().getCode();
        if (URIs.isSuccessful(responseCode)) {
            return response;
        } else {
            if (responseCode == HttpURLConnection.HTTP_NOT_FOUND || responseCode == HttpURLConnection.HTTP_GONE) {
                throw new FileNotFoundException(uri.toString());
            } else {
                if (responseCode == HttpURLConnection.HTTP_INTERNAL_ERROR) {
                    final String errorStr = IOUtils.toString(response, Charset.defaultCharset());
                    throw new IOException("Server returned HTTP response code [" + responseCode + "] for URL ["
                            + uri.toString() + "] with error:\n*****************************\n"
                            + Strings.putSuffix(errorStr, "\n") + "*****************************");
                } else {
                    throw new IOException("Server returned HTTP response code [" + responseCode + "] for URL ["
                            + uri.toString() + "]");
                }
            }
        }
    }

}
