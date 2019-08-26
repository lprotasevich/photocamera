public class myMap {
    private String latitude;//широта
    private String longitude;//долгота

    public myMap(String str){
        String[] mass = str.split(", ");
        latitude = mass[0];
        longitude = mass[1];
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude(){
        return longitude;
    }
}
