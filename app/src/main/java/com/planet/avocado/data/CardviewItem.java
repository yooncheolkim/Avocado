package com.planet.avocado.data;

public class CardviewItem {
    int userImage;
    String userId;
    int stuffImage;
    String point;
    String reviewText;

    public int getUserImage(){return this.userImage;}
    public String getUserId(){return this.userId;}
    public int getStuffImage(){return this.stuffImage;}
    public String getPoint(){return this.point;}
    public String getReviewText(){return this.reviewText;}

    public void setUserImage(int image){userImage = image;}
    public void setUserId(String id){userId = id;}
    public void setStuffImage(int stuffImage1){stuffImage = stuffImage1;}
    public void setPoint(String point1){point = point1;}
    public void setReviewText(String reviewText1){reviewText = reviewText1;}
}
