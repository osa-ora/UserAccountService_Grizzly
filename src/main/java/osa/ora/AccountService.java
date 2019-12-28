package osa.ora;

import osa.ora.beans.User;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import osa.ora.customer.exception.InvalidRequestException;
import osa.ora.customer.exception.JsonMessage;
import osa.ora.customer.persistence.AccountPersistence;

@Path("/V1/accounts")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class AccountService {

    private final AccountPersistence accountPersistence = new AccountPersistence();

    @GET
    @Path("/all")
    @Produces(MediaType.APPLICATION_JSON)
    public User[] getAllCustomers() {
        System.out.println("Load all accounts..");
        User[] allAccounts=accountPersistence.findAll();
        for(User account:allAccounts){
            //remove the password
            account.setPassword("*******");
        }
        return allAccounts;
    }

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public User getCustomer(@PathParam("id") long id) {
        User account = accountPersistence.findbyId(id);
        if (account != null) {
            System.out.println("Retireve account using: " + id);
            //remove the password
            account.setPassword("*******");
            return account;
        } else {
            throw new InvalidRequestException(new JsonMessage("Error", "Account "
                    + id + " not found"));
            
        }
    }
    //this methid just for testing purposes
    @GET
    @Path("/login/{login}/{password}")
    @Produces(MediaType.APPLICATION_JSON)
    @Deprecated
    public User loginAccount(@PathParam("login") String login,@PathParam("password") String password) {
        return login(login,password);
    }
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    public User loginAccount2(@QueryParam("login") String login,@QueryParam("password") String password) {
        return login(login,password);
    }
    @POST
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    public User loginAccount3(@FormParam("login") String login,@FormParam("password") String password) {
        return login(login,password);
    }
    @POST
    @Consumes("application/json")
    @Produces(MediaType.APPLICATION_JSON)
    public User loginAccount3(User user) {
        return login(user.getLogin(),user.getPassword());
    }
    private User login(String login, String password){
        System.out.println("Login....");
        User account = accountPersistence.loginUser(login,password);
        if (account != null) {
            System.out.println("Retireve account using: " + login);
            //remove the password
            account.setPassword("*******");
            return account;
        } else {
            throw new InvalidRequestException(new JsonMessage("Error", "Account "
                    + login + " not found"));
        }
    }
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    public Response addCustomer(User account) {
        JsonMessage jsonMessage = accountPersistence.save(account);
        if (jsonMessage.getType().equals("Success")) {
            System.out.println("Successfully added a new account");
            return Response.status(201).build();
        } else {
            throw new InvalidRequestException(jsonMessage);
        }
    }

    @PUT
    @Path("{id}/update")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCustomer(User account, @PathParam("id") long id) {
        account.setId(id);
        User cust = accountPersistence.findbyId(account.getId());
        if (cust != null) {
            JsonMessage jsm = accountPersistence.update(account);
            if (jsm.getType().equals("Success")) {
                System.out.println("Successfully updated account with id=" + id);
                return Response.status(Response.Status.OK).build();
            } else {
                throw new InvalidRequestException(jsm);
            }
        } else {
            throw new InvalidRequestException(new JsonMessage("Error", "Account "
                    + account.getId() + " not found"));
        }
    }

    @DELETE
    @Path("/remove/{id}")
    public Response deleteCustomer(@PathParam("id") long id) {
        User cust = accountPersistence.findbyId(id);
        if (cust != null) {
            JsonMessage jsm = accountPersistence.delete(id);
            if (jsm.getType().equals("Success")) {
                System.out.println("Successfully deleted account with id=" + id);
                return Response.status(Response.Status.OK).build();
            } else {
                throw new InvalidRequestException(jsm);
            }
        } else {
            throw new InvalidRequestException(new JsonMessage("Error", "Account "
                    + id + " not found"));
        }
    }

}
