package com.objectedge.payzoop.model;

import android.util.Log;

import com.objectedge.payzoop.OCCApplication;
import com.objectedge.payzoop.event.CartEvent;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import de.greenrobot.event.EventBus;

/**
 * Created by deepak.verma on 26-08-2016.
 */
public class Cart {
    private static final String TAG = Cart.class.getSimpleName();
    @Inject
    EventBus mEventBus;

    int TotalItemCount;

    public Cart(){
        OCCApplication.getRootComponent().inject(this);}

    public List<ProductModel> products = new ArrayList<ProductModel>();

    private void addProduct(ProductModel product){
        if(products.contains(product)){
            ProductModel productInCart = null;
            for (ProductModel item:products) {
                if(item.id == product.id){
                    productInCart = item;
                    break;
                }
            }
            increamentProductCount(productInCart);
        }else {
            products.add(product);
        }
        mEventBus.post(new CartEvent.UpdateCart(getTotalItemsInCart()));
    }

    private void increamentProductCount(ProductModel productInCart) {
        productInCart.quantity += 1;
        //Integer num = Integer.parseInt(productInCart.getHolder().elegantNumberButton.getNumber());
       // productInCart.getHolder().elegantNumberButton.setNumber(productInCart.getQuantity().toString());
    }

    private void removeProduct(ProductModel product){
        if(products.contains(product)){
            ProductModel productInCart = null;
            for (ProductModel item:products) {
                if(item.id == product.id){
                    productInCart = item;
                    Log.d(TAG, String.format("Product found in cart for removal. Product_id : %s",product.getId() ));
                    break;
                }
            }
            if(productInCart.quantity <= 1){
                products.remove(productInCart);
                Log.d(TAG, String.format("Product  removed from cart. Product_id : %s",product.getId() ));
            }else {
                productInCart.quantity -= 1;
                Log.d(TAG, String.format("Product  count decreased in cart. Product_id : %s",product.getId() ));
            }
        }else {
            Log.e(TAG, String.format("Cannot remove a product from cart which is not in the cart already. Product_id : %s",product.getId() ));
        }
        mEventBus.post(new CartEvent.UpdateCart(getTotalItemsInCart()));
    }

    private int getTotalItemsInCart(){
        int count = 0;
        for (ProductModel item:products) {
                count += item.getQuantity();
        }
        TotalItemCount = count;
        return TotalItemCount;
    }

    public void increamentCartCountForProduct(ProductModel product){
        addProduct(product);
    }

    public void decreamentCartCountForProduct(ProductModel product){
        removeProduct(product);
    }


    public Double getTotalSum(){
        Double sum = 0.0;
        for (ProductModel item:products) {
            sum += (Double.parseDouble(item.getListPrice()) * item.quantity);
        }
        return sum;
    }

    public String addToCart(ProductModel product){
        for (ProductModel prod : products) {
            if(product.equals(prod)){
                increamentProductCount(prod);
                mEventBus.post(new CartEvent.UpdateCart(getTotalItemsInCart()));
                return "PRODUCT_IN_CART_ALREADY";
            }
        }
        addProduct(product);
        mEventBus.post(new CartEvent.UpdateCart(getTotalItemsInCart()));
        return "PRODUCT_ADDED_TO_CART";
        //miniCartItemCountView.setText(String.valueOf(cart.products.size()));

    }
}

