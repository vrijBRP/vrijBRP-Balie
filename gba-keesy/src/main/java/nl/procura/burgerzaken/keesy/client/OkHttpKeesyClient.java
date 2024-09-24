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

package nl.procura.burgerzaken.keesy.client;

import java.io.IOException;
import java.time.Duration;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import org.jetbrains.annotations.NotNull;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import nl.procura.burgerzaken.keesy.api.ApiClient;
import nl.procura.burgerzaken.keesy.api.ApiClientConfig;
import nl.procura.burgerzaken.keesy.api.ApiResponse;
import nl.procura.burgerzaken.keesy.api.model.InwonerAppError;

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

public class OkHttpKeesyClient extends ApiClient {

  private final ApiClientConfig config;
  private final OkHttpClient    client;
  private final Gson            gson;

  public OkHttpKeesyClient(final ApiClientConfig config, Duration duration) {
    super(config);
    this.config = config;
    OkHttpClient.Builder builder = new OkHttpClient.Builder();
    if (config.debug()) {
      builder.addInterceptor(new HttpLoggingInterceptor());
    }
    client = builder
        .connectTimeout(duration.toMillis(), TimeUnit.MILLISECONDS)
        .readTimeout(duration.toMillis(), TimeUnit.MILLISECONDS)
        .callTimeout(duration.toMillis(), TimeUnit.MILLISECONDS)
        .writeTimeout(duration.toMillis(), TimeUnit.MILLISECONDS)
        .build();
    gson = new GsonBuilder().create();
  }

  @Override
  public <T, R> ApiResponse<R> get(final Request<T> request, final Class<R> type) {
    Builder requestBuilder = getRequestBuilder(request).get();
    okhttp3.Request okhttpRequest = requestBuilder.build();
    return sendCall(type, okhttpRequest);
  }

  @Override
  public <T, R> ApiResponse<R> post(final Request<T> request, final Class<R> type) {
    String postBody = gson.toJson(request.getBody());
    RequestBody requestBody = RequestBody.create(postBody, MediaType.parse("application/json"));
    Builder requestBuilder = getRequestBuilder(request).post(requestBody);
    okhttp3.Request okhttpRequest = requestBuilder.build();
    return sendCall(type, okhttpRequest);
  }

  private <R> ApiResponse<R> sendCall(Class<R> type, okhttp3.Request okhttpRequest) {
    final Call call = client.newCall(okhttpRequest);
    try (Response response = call.execute()) {
      ApiResponse<R> apiResponse = new ApiResponse<>();
      apiResponse.setHttpCode(response.code());
      apiResponse.setSuccessful(response.isSuccessful());
      if (response.isSuccessful()) {
        assert response.body() != null;
        if (type == byte[].class) {
          apiResponse.setEntity(type.cast(response.body().bytes()));
        } else {
          String jsonResponse = response.body().string();
          apiResponse.setEntity(gson.fromJson(jsonResponse, type));
        }
      } else {
        if (response.code() == 500) {
          throw new RuntimeException("Inwoner.app geeft een foutmelding");

        } else if (response.code() == 503) {
          throw new RuntimeException("Inwoner.app (tijdelijk) niet beschikbaar");
        }
        assert response.body() != null;
        String jsonResponse = response.body().string();
        apiResponse.setError(gson.fromJson(jsonResponse, InwonerAppError.class));
      }
      return apiResponse;
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private <T> Builder getRequestBuilder(Request<T> request) {
    HttpUrl httpUrl = HttpUrl.parse(config.baseUrl() + request.getPath());
    HttpUrl.Builder urlBuilder = Objects.requireNonNull(httpUrl).newBuilder();
    request.getParameters()
        .forEach(parameter -> urlBuilder.addQueryParameter(parameter.getName(), parameter.getValue()));
    Builder requestBuilder = new Builder().url(urlBuilder.build());
    request.getHeaders().forEach(requestBuilder::header);
    return requestBuilder;
  }

  private class HttpLoggingInterceptor implements Interceptor {

    @NotNull
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
          if (Objects.requireNonNull(responseBody.contentType()).toString().toLowerCase().contains("json")) {
            System.out.println("Response Body: " + gson.toJson(gson.fromJson(new String(bytes), Object.class)));
          }
          responseBody = ResponseBody.create(bytes, responseBody.contentType());
        }

        return response.newBuilder().body(responseBody).build();
      }
    }
  }
}
