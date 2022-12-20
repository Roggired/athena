package ru.yofik.athena.messenger.infrastructure.integration.auth;

import retrofit2.Call;
import retrofit2.http.*;
import ru.yofik.athena.common.api.AuthV1Response;

public interface AuthUserApi {
    @GET("/api/v2/users/{id}")
    Call<AuthV1Response> getUserById(
            @Path("id") long id,
            @Header("Authorization") String authorization
    );

    @PUT("/api/v2/users/{id}")
    Call<AuthV1Response> updateUserById(
            @Path("id") long id,
            @Body UpdateUserRequest request,
            @Header("Authorization") String authorization
    );

    @GET("/api/v2/users/my")
    Call<AuthV1Response> getMyUser(
            @Header("Authorization") String authorization
    );

    @POST("/api/v2/users/filtered")
    Call<AuthV1Response> getUsersFiltered(
            @Query("sequentialNumber") int pageNumber,
            @Query("size") int pageSize,
            @Body FilteredUsersRequest request,
            @Header("Authorization") String authorization
    );
}
