package adapter;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.user.secondhandtradingplatform.R;

import java.util.ArrayList;
import java.util.List;

import RealmModel.RealmCamera;
import RealmModel.RealmGadget;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {
    private static final int TYPE_INFO = 1;
    private static final int TYPE_POST = 2;
    List<RealmGadget> gadgets = new ArrayList<>();
    String os, mon, camera, pName, price, image;

    public ProductAdapter(List<RealmGadget> gadgets, String pName, String price, String os, String mon, String camera, String image) {
        this.gadgets = gadgets;
        this.pName = pName;
        this.price = price;
        this.os = os;
        this.mon = mon;
        this.camera = camera;
        this.image = image;
    }

    @Override
    public int getItemViewType(int position) {
        // Choose Product Info layout for the 1st card
        // Choose Seller post layout for the rest of the card
        return (position == 0 ? TYPE_INFO : TYPE_POST);
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate different layout
        switch (viewType) {
            case TYPE_INFO:
                return new InfoViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_product_info, parent, false));
            case TYPE_POST:
                return new PostViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_posted_item, parent, false));
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        if (holder.getItemViewType() == TYPE_INFO) {
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            InfoViewHolder iHolder = (InfoViewHolder) holder;
            iHolder.image.setImageBitmap(bitmap);
            iHolder.pName.setText(pName);
            iHolder.mon.setText(mon + "吋");
            iHolder.os.setText(os);
            iHolder.price.setText("HK$" + price);
            iHolder.camera.setText(camera + "萬像素");

        } else {
            PostViewHolder pHolder = (PostViewHolder) holder;
            if(gadgets != null){
                RealmGadget gadget = gadgets.get(position-1);
                pHolder.sellerName.setText(gadget.getSeller());
                pHolder.sellingPrice.setText("HK$" + gadget.getPrice());
                pHolder.tradePlace.setText(gadget.getSeller_location());
            }

        }

    }

    @Override
    public int getItemCount() {
        if (gadgets != null) {
            return gadgets.size() + 1;
        } else {
            return 1;
        }

    }

    public class InfoViewHolder extends ProductViewHolder {
        TextView pName, price, os, mon, camera;
        ImageView image;

        public InfoViewHolder(View itemView) {
            super(itemView);
            image = (ImageView) itemView.findViewById(R.id.image);
            pName = (TextView) itemView.findViewById(R.id.pName);
            price = (TextView) itemView.findViewById(R.id.price);
            os = (TextView) itemView.findViewById(R.id.etOS);
            mon = (TextView) itemView.findViewById(R.id.etMon);
            camera = (TextView) itemView.findViewById(R.id.etCam);
        }
    }

    public class PostViewHolder extends ProductViewHolder {
        TextView sellerName, sellingPrice, tradePlace;
        ImageView productPhoto;

        public PostViewHolder(View itemView) {
            super(itemView);
            productPhoto = (ImageView) itemView.findViewById(R.id.product_photo);
            sellerName = (TextView) itemView.findViewById(R.id.sellername);
            sellingPrice = (TextView) itemView.findViewById(R.id.sellingPrice);
            tradePlace = (TextView) itemView.findViewById(R.id.tradePlace);
        }
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        ProductViewHolder(View itemView) {
            super(itemView);

        }
    }
}
