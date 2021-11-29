package com.albares.clothes.api;

import com.albares.clothes.db.Product;
import com.albares.clothes.utils.Db;
import com.albares.clothes.utils.Response;
import com.albares.clothes.utils.ResponseCodes;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Path("/admin")

public class AdminService {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addProduct(Product newProducto) throws NoSuchAlgorithmException {
        Response r = new Response();
        try {
            Db myDb = new Db();
            myDb.connect();
            newProducto.insertAndGetId_DB(myDb);
            myDb.disconnect();

            r.setResponseCode(ResponseCodes.OK);
        } catch (SQLException e) {
            r.setResponseCode(ResponseCodes.EXCEPTION);
        }
        return r;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProducts() {
        Response r = new Response();
        Product p = new Product();
        try {
            Db myDb = new Db();
            myDb.connect();
            r.setProducts(Product.selectProducts_DB(myDb));
            myDb.disconnect();

            r.setResponseCode(ResponseCodes.OK);
        } catch (SQLException e) {
            r.setResponseCode(ResponseCodes.EXCEPTION);
        }
        return r;
    }

    @PUT
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateProduct(Product updateProduct) {
        Response r = new Response();
        try {
            Db myDb = new Db();
            myDb.connect();
            updateProduct.updateProduct_DB(myDb);
            myDb.disconnect();

            r.setResponseCode(ResponseCodes.OK);
        } catch (SQLException e) {
            r.setResponseCode(ResponseCodes.EXCEPTION);
        }
        return r;
    }

}
