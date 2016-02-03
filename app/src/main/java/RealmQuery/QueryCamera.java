package RealmQuery;

import android.content.Context;

import java.util.List;

import RealmModel.RealmCamera;
import activity.CameraFragment;
import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import product.Camera;
import product.Product;
import  RealmModel.RealmCamera;

/**
 * Created by philip on 29/1/2016.
 */
public class QueryCamera   {
    Realm realm;
    public QueryCamera(Context context)
    {

         realm = Realm.getInstance(context);
    }

   public void insertCameraToDb(List<Camera> camera )
    {
        realm.beginTransaction();
      for (Camera tempCamera: camera)
      {
          if (realm.where(RealmCamera.class).equalTo("name",tempCamera.name).count() > 0)
          {
           System.out.println("exsite data");
          }
          else
          {

              //        private String name;
//        private String price;
//        private String warranty;
//        private String place;
//        private int photoid;
//        private String seller;
//        private String newPrice;
//        private String phonNo;
//        private String email;

              RealmCamera realmCamera  = new RealmCamera();
              realmCamera.setName(tempCamera.name);
              realmCamera.setNewPrice(tempCamera.newPrice);
              realmCamera.setWarranty(tempCamera.warranty);
              realmCamera.setPrice(tempCamera.price);
              realmCamera.setSeller(tempCamera.seller);
              realmCamera.setEmail(tempCamera.email);
              realmCamera.setPhonNo(tempCamera.phonNo);
              realmCamera.setPlace(tempCamera.place);
              realm.copyToRealm(realmCamera);
          }
      }
     System.out.println("Finsh to insert!");
        realm.commitTransaction();

    }

    public RealmQuery<RealmCamera> retrieveCameraByName(String name)
    {
        RealmQuery<RealmCamera> realmCameras = realm.where(RealmCamera.class).equalTo("name", name, Case.INSENSITIVE);
        System.out.println("realmCameras.count()" + realmCameras.count());
        return realmCameras;
    }

    public RealmResults<RealmCamera>
    retrieveCameraByAllField(String name, String price, String warranty, String place, int photoid, String seller, String newPrice, String phonNo,String email
    )
    {
//        private String name;
//        private String price;
//        private String warranty;
//        private String place;
//        private int photoid;
//        private String seller;
//        private String newPrice;
//        private String phonNo;
//        private String email;
        final RealmResults<RealmCamera> r = realm.where(RealmCamera.class).beginGroup()
                .contains("name", name, Case.INSENSITIVE)  //implicit AND
                .or()
                .contains("price", price, Case.INSENSITIVE)
                .or()
                .contains("warranty", warranty, Case.INSENSITIVE).or().contains("place", place, Case.INSENSITIVE).
                        or().equalTo("photoid", photoid).or().contains("seller", seller, Case.INSENSITIVE).or()
                .contains("newPrice",newPrice,Case.INSENSITIVE).or().contains("phonNo",phonNo,Case.INSENSITIVE).or()
                .contains("email",email,Case.INSENSITIVE)
                        .endGroup()
                        .findAll();



//       callback = new callBackFinishInsert() {
//           @Override
//           public String retriveCameraRealmList() {
//
//               return  "je";
//           }
//       };
//        callback.retriveCameraRealmList("good job");
//        System.out.println("callback oj" + callback);
        return  r;
    }
}



