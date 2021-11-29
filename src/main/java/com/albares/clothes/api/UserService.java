package com.albares.clothes.api;

import com.albares.clothes.db.Product;
import com.albares.clothes.utils.Db;
import com.albares.clothes.utils.Response;
import com.albares.clothes.utils.ResponseCodes;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;

@Path("/user")

public class UserService {

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response buyProduct(Product product) throws NoSuchAlgorithmException {
        Response r = new Response();
        try {
            Db myDb = new Db();
            myDb.connect();
            //si logra restar devuelve un 1,equivale al numero de registros actualizados.
            if (product.updateStock(myDb,product) != 0) {
                r.setResponseCode(ResponseCodes.OK);

            } else {
                r.setResponseCode(ResponseCodes.NOT_FOUND);

            }
            myDb.disconnect();

        } catch (SQLException e) {
            r.setResponseCode(ResponseCodes.EXCEPTION);
        }
        return r;
    }

    @GET
    @Path("/{gender}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getProductsStock(@PathParam("gender") Integer gender) {
        Response r = new Response();
        Product p = new Product();
        try {
            p.setGender(gender);
            Db myDb = new Db();
            myDb.connect();
            r.setProducts(p.selectProductsStock_DB(myDb));
            myDb.disconnect();

            r.setResponseCode(ResponseCodes.OK);
        } catch (SQLException e) {
            r.setResponseCode(ResponseCodes.EXCEPTION);
        }
        return r;
    }
}
