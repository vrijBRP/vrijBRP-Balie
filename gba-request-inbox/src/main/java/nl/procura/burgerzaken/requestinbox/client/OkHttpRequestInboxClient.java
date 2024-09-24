/*
 * Copyright 2024 - 2025 Procura B.V.
 *
 * In licentie gegeven krachtens de EUPL, versie 1.2
 * U mag dit werk niet gebruiken, behalve onder de voorwaarden van de licentie.
 * U kunt een kopie van de licentie vinden op:
 *
 *   https://github.com/vrijBRP/vrijBRP/blob/master/LICENSE.md
 *
 * Deze bevat zowel de Nederlandse als de Engelse tekst
 *
 * Tenzij dit op grond van toepasselijk recht vereist is of schriftelijk
 * is overeengekomen, wordt software krachtens deze licentie verspreid
 * "zoals deze is", ZONDER ENIGE GARANTIES OF VOORWAARDEN, noch expliciet
 * noch impliciet.
 * Zie de licentie voor de specifieke bepalingen voor toestemmingen en
 * beperkingen op grond van de licentie.
 */

package nl.procura.burgerzaken.requestinbox.client;

import java.io.IOException;
import java.time.Duration;
import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nl.procura.burgerzaken.requestinbox.api.ApiClient;
import nl.procura.burgerzaken.requestinbox.api.model.OffsetDateTimeDeserializer;

import okhttp3.Call;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request.Builder;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

public class OkHttpRequestInboxClient extends ApiClient {

  private final OkHttpClient client;
  private final Gson         gson;

  public OkHttpRequestInboxClient(final String basePath, Duration duration) {
    super(basePath);
    client = new OkHttpClient.Builder()
        //.addInterceptor(new HttpLoggingInterceptor())
        .connectTimeout(duration.toMillis(), TimeUnit.MILLISECONDS)
        .readTimeout(duration.toMillis(), TimeUnit.MILLISECONDS)
        .build();
    gson = new GsonBuilder()
        .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeDeserializer())
        .setPrettyPrinting().create();
  }

  @Override
  public <T, R> R get(final Request<T> request, final Class<R> type) {
    Builder requestBuilder = getRequestBuilder(request).get();
    okhttp3.Request okhttpRequest = requestBuilder.build();
    return sendCall(type, okhttpRequest);
  }

  @Override
  public <T, R> R post(final Request<T> request, final Class<R> type) {
    String postBody = gson.toJson(request.getBody());
    RequestBody requestBody = RequestBody.create(postBody, MediaType.parse("application/json"));
    Builder requestBuilder = getRequestBuilder(request).post(requestBody);
    okhttp3.Request okhttpRequest = requestBuilder.build();
    return sendCall(type, okhttpRequest);
  }

  @Override
  public <T, R> R patch(final Request<T> request, final Class<R> type) {
    String postBody = gson.toJson(request.getBody());
    RequestBody requestBody = RequestBody.create(postBody, MediaType.parse("application/merge-patch+json"));
    Builder requestBuilder = getRequestBuilder(request).patch(requestBody);
    okhttp3.Request okhttpRequest = requestBuilder.build();
    return sendCall(type, okhttpRequest);
  }

  private <R> R sendCall(Class<R> type, okhttp3.Request okhttpRequest) {
    final Call call = client.newCall(okhttpRequest);
    try (Response response = call.execute()) {
      if (response.isSuccessful()) {
        assert response.body() != null;
        if (type == byte[].class) {
          return type.cast(response.body().bytes());
        }
        String jsonResponse = response.body().string();
        return gson.fromJson(jsonResponse, type);
      } else {
        throw new RuntimeException("Foutmelding bij bevragen van de verzoeken service",
            new RuntimeException("Error: " + response.code() + " " + response.message()));
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> Builder getRequestBuilder(Request<T> request) {
    HttpUrl httpUrl = HttpUrl.parse(getBaseUri() + request.getPath());
    HttpUrl.Builder urlBuilder = Objects.requireNonNull(httpUrl).newBuilder();
    request.getParameters()
        .forEach(parameter -> urlBuilder.addQueryParameter(parameter.getName(), parameter.getValue()));
    Builder requestBuilder = new Builder().url(urlBuilder.build());
    request.getHeaders().forEach(requestBuilder::header);
    return requestBuilder;
  }

  private class HttpLoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
      okhttp3.Request request = chain.request();
      System.out.println("\nREQUEST");
      System.out.println("Method: " + request.method());
      System.out.println("URL: " + request.url());

      if (request.body() != null) {
        Buffer buffer = new Buffer();
        request.body().writeTo(buffer);
        System.out.println("Request Body: " + buffer.readUtf8());
      }

      try (Response response = chain.proceed(request)) {
        System.out.println("\nRESPONSE");
        System.out.println("Status: " + response.code() + " " + response.message());
        System.out.println("Headers: " + response.headers());

        ResponseBody responseBody = response.body();
        if (responseBody != null) {
          byte[] bytes = responseBody.bytes();
          if (Objects.requireNonNull(responseBody.contentType()).type().toLowerCase().contains("json")) {
            System.out.println("Response Body: " + gson.toJson(gson.fromJson(new String(bytes), Object.class)));
          }
          responseBody = ResponseBody.create(bytes, responseBody.contentType());
        }

        return response.newBuilder().body(responseBody).build();
      }
    }
  }
}
