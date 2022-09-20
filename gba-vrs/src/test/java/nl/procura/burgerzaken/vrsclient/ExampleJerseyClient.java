package nl.procura.burgerzaken.vrsclient;

import static com.sun.jersey.api.json.JSONConfiguration.FEATURE_POJO_MAPPING;

import java.net.URI;
import java.time.LocalDate;
import java.time.OffsetDateTime;

import javax.ws.rs.ext.ContextResolver;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.deser.std.FromStringDeserializer;
import org.codehaus.jackson.map.module.SimpleModule;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import com.sun.jersey.client.urlconnection.URLConnectionClientHandler;

/**
 * Example client implemented with Jersey 1.x and Jackson 1.x
 */
public class ExampleJerseyClient extends ApiClient {

  private final Client client;

  public ExampleJerseyClient(final String basePath) {
    super(basePath);
    final DefaultClientConfig config = new DefaultClientConfig();
    config.getFeatures().put(FEATURE_POJO_MAPPING, true);
    config.getSingletons().add(new ObjectMapperContextResolver());
    client = new Client(new URLConnectionClientHandler(), config);
    client.setConnectTimeout(1_000);
    client.setReadTimeout(1_000);
  }

  @Override
  public <T, R> R get(final Request<T> request, final Class<R> type) {
    final WebResource.Builder builder = client.resource(URI.create(getBaseUri() + request.path)).getRequestBuilder();
    request.headers.forEach(builder::header);
    return builder.get(type);
  }

  @Override
  public <T, R> R post(final Request<T> request, final Class<R> type) {
    final WebResource.Builder builder = client.resource(URI.create(getBaseUri() + request.path)).getRequestBuilder();
    request.headers.forEach(builder::header);
    return builder.post(type, request.body);
  }

  private static class ObjectMapperContextResolver implements ContextResolver<ObjectMapper> {

    private final ObjectMapper objectMapper;

    public ObjectMapperContextResolver() {
      objectMapper = new ObjectMapper();
      final SimpleModule module = new SimpleModule("simple", new Version(1, 0, 0, ""));
      module.addDeserializer(OffsetDateTime.class, new OffsetDateTimeDeserializer());
      module.addDeserializer(LocalDate.class, new LocalDateTimeDeserializer());
      objectMapper.registerModule(module);
    }

    @Override
    public ObjectMapper getContext(final Class<?> type) {
      return objectMapper;
    }
  }

  private static class OffsetDateTimeDeserializer extends FromStringDeserializer<OffsetDateTime> {

    protected OffsetDateTimeDeserializer() {
      super(OffsetDateTime.class);
    }

    @Override
    protected OffsetDateTime _deserialize(final String value, final DeserializationContext ctxt) {
      return OffsetDateTime.parse(value);
    }
  }

  private static class LocalDateTimeDeserializer extends FromStringDeserializer<LocalDate> {

    protected LocalDateTimeDeserializer() {
      super(LocalDate.class);
    }

    @Override
    protected LocalDate _deserialize(final String value, final DeserializationContext ctxt) {
      return LocalDate.parse(value);
    }
  }
}
