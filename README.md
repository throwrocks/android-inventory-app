# android-inventory-app

I developed this project as part of the Udacity Code Reviewer training.

This is an inventory app that implementes a [Realm](https://realm.io/) database. It allows you to create item records, view them in a list, and modify their quantities on a detail Layout. On the detail Layout, you can sell the item by clicking on the Fab button, modify the quantities on hand, send an email to the vendor for reorder, or delete the item.

I implemented two kinds of intent.

* ACTION_PICK intent on the uploadImage method so you can select an image from your Gallery/Documents folder and store it in the app
* ACTION_SENDTO intent on the composeEmail method so you can send an email to item's vendor to purchase more of the item when you're running low
* 
I also implemented a RecyclerView.Adapter to bind the Realm data to the item's list.

You can see some screenshots below.

![ScreenShot](https://github.com/throwrocks/android-project-images/blob/master/inventory-app/inventory-app-list.png)
![ScreenShot](https://github.com/throwrocks/android-project-images/blob/master/inventory-app/inventory-app-data-entry.png)
![ScreenShot](https://github.com/throwrocks/android-project-images/blob/master/inventory-app/inventory-app-detail.png)
