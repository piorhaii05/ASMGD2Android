package com.example.shopping_online.Layout;

import com.example.shopping_online.ClothesModel;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {
//    public String DOMAIN = "http://10.24.46.240:3000/";
    public String DOMAIN = "http://192.168.100.189:3000/";

    //Lấy danh sách
    @GET("/api/list")
    Call<ArrayList<ClothesModel>> getClothes();

    //Thêm
    @POST("/api/add_xe")
    Call<ArrayList<ClothesModel>> addClothes(@Body ClothesModel clothes);

    //Xóa
    @DELETE("/api/delete/{id}") // Sử dụng path
    Call<Void> deleteClothes(@Path("id") String id);

    //Sửa
    @PUT("/api/update")
    Call<ArrayList<ClothesModel>> updateClothes(@Body ClothesModel clothesModel);

}
