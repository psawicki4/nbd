package edu.pja.nbd.s25882;

import com.basho.riak.client.api.RiakClient;
import com.basho.riak.client.api.commands.kv.DeleteValue;
import com.basho.riak.client.api.commands.kv.FetchValue;
import com.basho.riak.client.api.commands.kv.StoreValue;
import com.basho.riak.client.core.query.Location;
import com.basho.riak.client.core.query.Namespace;
import com.basho.riak.client.core.query.RiakObject;
import com.basho.riak.client.core.util.BinaryValue;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class Main {
    public static void main(String[] args) throws UnknownHostException, ExecutionException, InterruptedException {
        RiakClient client = RiakClient.newClient("127.0.0.1");
        Namespace ns = new Namespace("default", "s25882");
        Location location = new Location(ns, "ringworld");


        RiakObject riakObject = new RiakObject();
        riakObject.setContentType("application/json")
                .setValue(BinaryValue.create("{\n" +
                        "  \"title\": \"Ringworld\",\n" +
                        "  \"author\": \"Larry Niven\",\n" +
                        "  \"pages\": 342,\n" +
                        "  \"available\": true\n" +
                        "}"));
        StoreValue store = new StoreValue.Builder(riakObject)
                .withLocation(location).build();
        StoreValue.Response response = client.execute(store);
        System.out.println("Added: " + response.toString());
        FetchValue fv = new FetchValue.Builder(location).build();
        FetchValue.Response fvResponse = client.execute(fv);
        System.out.println(fvResponse.isNotFound() ? "Not found: " + fvResponse
                : "Fetched: " + fvResponse.getValue(RiakObject.class).getValue().toString());


        riakObject = new RiakObject();
        riakObject.setContentType("application/json")
                .setValue(BinaryValue.create("{\n" +
                        "  \"title\": \"Ringworld\",\n" +
                        "  \"author\": \"Larry Niven\",\n" +
                        "  \"pages\": 342,\n" +
                        "  \"available\": false\n" +
                        "}"));
        store = new StoreValue.Builder(riakObject)
                .withLocation(location).build();
        response = client.execute(store);
        System.out.println("Modified: " + response.toString());
        fvResponse = client.execute(fv);
        System.out.println(fvResponse.isNotFound() ? "Not found: " + fvResponse
                : "Fetched: " + fvResponse.getValue(RiakObject.class).getValue().toString());


        DeleteValue deleteValue = new DeleteValue.Builder(location).build();
        client.execute(deleteValue);
        System.out.println("Deleted");
        fvResponse = client.execute(fv);
        System.out.println(fvResponse.isNotFound() ? "Not found: " + fvResponse
                : "Fetched: " + fvResponse.getValue(RiakObject.class).getValue().toString());


        client.shutdown();
    }
}