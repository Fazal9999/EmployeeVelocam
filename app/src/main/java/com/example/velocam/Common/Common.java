package com.example.velocam.Common;

import com.example.velocam.Model.User;
import com.example.velocam.Retrofit.IVelocamAPI;
import com.example.velocam.Retrofit.RetrofitClient;

public class Common {
    //10.0.2.2
   public static final String API_RESTAURANT_ENDPOINT ="https://fazaldrinkshop.000webhostapp.com/" ;
    //public static final String API_RESTAURANT_ENDPOINT ="https://10.0.2.2/velocam/" ;
    //public static final String API_KEY ="Fazal4545";
    public static User currentUser;
    //variable to hold data of this menu menu
//    public static Category currentCategory=null;
//    //load specific menu drinks to checck boxes for selections
//    public static final String TOPPING_MENU_ID="7";
//    public static List<Drink> toppingList=new ArrayList<>();
    //calculate price
    //show list of price
    //check all required field

    public static int sizeOfcup=-1;// -1 no choose error 0 fro M,1 For L

    public static int sizeOfsugar=-1 ;// -1 no choose error

    public static int ice=-1 ;// -1 no choose error


//
//    public static CartDatabase cartDatabase;
//    public static CartRepository cartRepository;
//
//    public static double toppingPrice=0.0;
//    public static List<String> toppingAdded=new ArrayList<>();
//


    public static IVelocamAPI getApi()
    {

        return RetrofitClient.getInstance(API_RESTAURANT_ENDPOINT).create(IVelocamAPI.class);
    }


}
