package devarthur.post.gitrepos;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import devarthur.post.gitrepos.model.ItemDataModel;

public class ItemResponse {

    @SerializedName("items")
    @Expose

    private List<ItemDataModel> items;

    public List<ItemDataModel> getItems(){
        return items;
    }

    public void setItems(List<ItemDataModel>items){
        this.items = items;
    }


}