package ms.cs.countries_quiz;

public class Continents {
    private long   id;
    private String country_name;
    private String continent_name;

    public Continents(){
        this.id = -1;
        this.country_name = null;
        this.continent_name = null;
    }

    public Continents(String country_name, String continent_name){
        this.id = -1;
        this.country_name = country_name;
        this.continent_name = continent_name;
    }

    public long getId()
    {
        return id;
    }

    public void setId(long id)
    {
        this.id = id;
    }

    public String getCountryName(){
        return country_name;
    }

    public void setCountryName(String country_name){
        this.country_name = country_name;
    }

    public String getContinentName(){
        return continent_name;
    }

    public void setContinent_name(String continent_name){
        this.continent_name = continent_name;
    }

    public String toString(){
        return id + ": " + country_name + " " + continent_name;
    }

}
