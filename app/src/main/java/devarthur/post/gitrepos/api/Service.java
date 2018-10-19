package devarthur.post.gitrepos.api;

import devarthur.post.gitrepos.model.ItemResponse;
import retrofit2.Call;
import retrofit2.http.GET;

public interface Service {

    @GET("/search/repositories?q=language:Java&sort=stars&page=1")
    Call<ItemResponse> getItems();
}
