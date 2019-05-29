package fr.ig2i.chat.api;

import androidx.annotation.NonNull;

import fr.ig2i.chat.model.Conversation;
import fr.ig2i.chat.model.Message;
import fr.ig2i.chat.model.User;
import fr.ig2i.chat.model.parameter.LoginParameters;
import fr.ig2i.chat.model.result.ConversationListResult;
import fr.ig2i.chat.model.result.ConversationResult;
import fr.ig2i.chat.model.result.IdResult;
import fr.ig2i.chat.model.result.LoginResult;
import fr.ig2i.chat.model.result.Result;
import fr.ig2i.chat.model.result.MessageListResult;
import fr.ig2i.chat.model.result.MessageResult;
import fr.ig2i.chat.model.result.UserListResult;
import fr.ig2i.chat.model.result.UserResult;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {

    // Users
    @NonNull
    @GET("user/")
    Call<UserListResult> listUsers(@Header("Authorization") @NonNull String token);

    @NonNull
    @GET("user/{id}")
    Call<UserResult> getUser(@Header("Authorization") @NonNull String token, @Path("id") int id);

    @NonNull
    @POST("user/")
    Call<IdResult> createUser(@Header("Authorization") @NonNull String token, @Body @NonNull User newUser);

    @NonNull
    @PATCH("user/{id}")
    Call<Result> updateUser(@Header("Authorization") @NonNull String token, @Path("id") int id, @Body @NonNull User mergedUser);

    @NonNull
    @POST("user/{id}")
    Call<Result> deleteUser(@Header("Authorization") @NonNull String token, @Path("id") int id);

    // Conversations
    @NonNull
    @GET("conversation/")
    Call<ConversationListResult> listConversations(@Header("Authorization") @NonNull String token);

    @NonNull
    @GET("conversation/{id}")
    Call<ConversationResult> getConversation(@Header("Authorization") @NonNull String token, @Path("id") int id);

    @NonNull
    @POST("conversation/")
    Call<IdResult> createConversation(@Header("Authorization") @NonNull String token, @Path("id") int id, @Body @NonNull Conversation newConversation);

    @NonNull
    @PATCH("conversation/{id}")
    Call<Result> updateConversation(@Header("Authorization") @NonNull String token, @Path("id") int id, @Body @NonNull Conversation mergedConversation);

    @NonNull
    @POST("conversation/{id}")
    Call<Result> deleteConversation(@Header("Authorization") @NonNull String token, @Path("id") int id);

    // Message
    @NonNull
    @GET("conversation/{idconv}/message")
    Call<MessageListResult> listMessages(@Header("Authorization") @NonNull String token, @Path("idconv") int idconv);

    @NonNull
    @GET("conversation/{idconv}/message/{id}")
    Call<MessageResult> getMessage(@Header("Authorization") @NonNull String token, @Path("idconv") int idconv, @Path("id") int id);

    @NonNull
    @POST("conversation/{idconv}/message/")
    Call<IdResult> createMessage(@Header("Authorization") @NonNull String token, @Path("idconv") int idconv, @Body @NonNull Message newMessage);

    @NonNull
    @PATCH("conversation/{idconv}/message/{id}")
    Call<Result> updateMessage(@Header("Authorization") @NonNull String token, @Path("idconv") int idconv, @Path("id") int id, @Body @NonNull Message mergedMessage);

    @NonNull
    @DELETE("conversation/{idconv}/message/{id}")
    Call<Result> deleteMessage(@Header("Authorization") @NonNull String token, @Path("idconv") int idconv, @Path("id") int id);

    // Login
    @NonNull
    @POST("login")
    Call<LoginResult> login(@Body @NonNull LoginParameters body);
}
