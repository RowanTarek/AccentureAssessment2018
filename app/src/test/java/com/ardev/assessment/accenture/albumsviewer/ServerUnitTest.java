package com.ardev.assessment.accenture.albumsviewer;

import org.junit.Test;

import java.io.IOException;

import com.google.gson.GsonBuilder;

import java.util.List;
import java.util.Objects;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static junit.framework.TestCase.assertTrue;

public class ServerUnitTest {
    @Test
    public void testServiceExecution() throws IOException {
        MockWebServer mockWebServer = new MockWebServer();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(mockWebServer.url("").toString())
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().create()))
                .build();

        mockWebServer.enqueue(new MockResponse().setBody("[\n" +
                "  {\n" +
                "    \"userId\": 1,\n" +
                "    \"id\": 1,\n" +
                "    \"title\": \"First Album\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"userId\": 1,\n" +
                "    \"id\": 2,\n" +
                "    \"title\": \"Second Album\"\n" +
                "  },\n" +
                "  {\n" +
                "    \"userId\": 1,\n" +
                "    \"id\": 3,\n" +
                "    \"title\": \"Third Album\"\n" +
                "  }]"));

        RemoteServicesInterface service = retrofit.create(RemoteServicesInterface.class);

        Call<List<Album>> call = service.getAlbums();
        Response<List<Album>> response = call.execute();
        assertTrue(call.isExecuted());
        assertTrue(response != null);
        assertTrue(Objects.requireNonNull(response.body()).size() == 3);

        mockWebServer.shutdown();
    }
}
